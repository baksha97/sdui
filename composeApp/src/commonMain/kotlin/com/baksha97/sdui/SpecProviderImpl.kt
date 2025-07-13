package com.baksha97.sdui

import com.baksha97.sdui.shared.models.SpecProvider
import com.baksha97.sdui.shared.models.SpecSource
import com.baksha97.sdui.shared.models.TokenRef as SharedTokenRef
import com.baksha97.sdui.shared.models.ScreenPayload as SharedScreenPayload
import com.baksha97.sdui.shared.models.TokenRegistry as SharedTokenRegistry
import com.baksha97.sdui.shared.models.Token as SharedToken
import com.baksha97.sdui.shared.models.ColumnToken as SharedColumnToken
import com.baksha97.sdui.shared.models.RowToken as SharedRowToken
import com.baksha97.sdui.shared.models.BoxToken as SharedBoxToken
import com.baksha97.sdui.shared.models.CardToken as SharedCardToken
import com.baksha97.sdui.shared.models.TextToken as SharedTextToken
import com.baksha97.sdui.shared.models.ButtonToken as SharedButtonToken
import com.baksha97.sdui.shared.models.SpacerToken as SharedSpacerToken
import com.baksha97.sdui.shared.models.DividerToken as SharedDividerToken
import com.baksha97.sdui.shared.models.SliderToken as SharedSliderToken
import com.baksha97.sdui.shared.models.AsyncImageToken as SharedAsyncImageToken
import com.baksha97.sdui.shared.models.LazyColumnToken as SharedLazyColumnToken
import com.baksha97.sdui.shared.models.LazyRowToken as SharedLazyRowToken

/**
 * Note: Conversion functions have been removed as composeApp now uses shared-models types directly
 */


/**
 * Implementation of SpecProvider that provides specs from local sources.
 */
class LocalSpecProviderImpl : SpecProvider {
    internal val sharedRegistry = SharedTokenRegistry().apply {
        // Register child tokens for enhanced_card first
        val enhancedCardTitle = SharedTextToken(
            id = "enhanced_card.title",
            version = 1,
            text = com.baksha97.sdui.shared.models.TemplateString("{{title}}")
        )
        val enhancedCardDescription = SharedTextToken(
            id = "enhanced_card.description", 
            version = 1,
            text = com.baksha97.sdui.shared.models.TemplateString("{{description}}")
        )
        val enhancedCardButton = SharedButtonToken(
            id = "enhanced_card.button",
            version = 1,
            text = com.baksha97.sdui.shared.models.TemplateString("{{buttonText}}"),
            onClick = com.baksha97.sdui.shared.models.Action(
                type = com.baksha97.sdui.shared.models.ActionType.Custom,
                data = mapOf("target" to "enhanced_card")
            )
        )

        // Register child tokens individually
        register(enhancedCardTitle)
        register(enhancedCardDescription)
        register(enhancedCardButton)

        // Create enhanced_card token
        register(SharedCardToken(
            id = "enhanced_card",
            version = 1,
            children = listOf(enhancedCardTitle, enhancedCardDescription, enhancedCardButton)
        ))

        // Register child tokens for slider_example first
        val sliderExampleTitle = SharedTextToken(
            id = "slider_example.title",
            version = 1,
            text = com.baksha97.sdui.shared.models.TemplateString("{{sliderTitle}}")
        )
        val sliderExampleDescription = SharedTextToken(
            id = "slider_example.description",
            version = 1,
            text = com.baksha97.sdui.shared.models.TemplateString("{{sliderDescription}}")
        )
        val sliderExampleSlider = SharedSliderToken(
            id = "slider_example.slider",
            version = 1,
            initialValue = 0.5f,
            valueRange = 0f..1f
        )

        // Register child tokens individually
        register(sliderExampleTitle)
        register(sliderExampleDescription)
        register(sliderExampleSlider)

        // Create slider_example token
        register(SharedCardToken(
            id = "slider_example",
            version = 1,
            children = listOf(sliderExampleTitle, sliderExampleDescription, sliderExampleSlider)
        ))

        // Register child tokens for form_example first (deepest level first)
        val formExampleTitle = SharedTextToken(
            id = "form_example.title",
            version = 1,
            text = com.baksha97.sdui.shared.models.TemplateString("{{formTitle}}")
        )
        val formExampleDescription = SharedTextToken(
            id = "form_example.description",
            version = 1,
            text = com.baksha97.sdui.shared.models.TemplateString("{{formDescription}}")
        )
        val formExampleCancel = SharedButtonToken(
            id = "form_example.cancel",
            version = 1,
            text = com.baksha97.sdui.shared.models.TemplateString("Cancel"),
            onClick = com.baksha97.sdui.shared.models.Action(
                type = com.baksha97.sdui.shared.models.ActionType.Custom,
                data = mapOf("action" to "cancel")
            )
        )
        val formExampleSubmit = SharedButtonToken(
            id = "form_example.submit",
            version = 1,
            text = com.baksha97.sdui.shared.models.TemplateString("Submit"),
            onClick = com.baksha97.sdui.shared.models.Action(
                type = com.baksha97.sdui.shared.models.ActionType.Custom,
                data = mapOf("action" to "submit")
            )
        )

        // Register deepest level tokens first
        register(formExampleTitle)
        register(formExampleDescription)
        register(formExampleCancel)
        register(formExampleSubmit)

        // Register intermediate level tokens
        val formExampleButtons = SharedRowToken(
            id = "form_example.buttons",
            version = 1,
            children = listOf(formExampleCancel, formExampleSubmit)
        )
        register(formExampleButtons)

        val formExampleContent = SharedColumnToken(
            id = "form_example.content",
            version = 1,
            children = listOf(formExampleTitle, formExampleDescription, formExampleButtons)
        )
        register(formExampleContent)

        // Create form_example token
        register(SharedBoxToken(
            id = "form_example",
            version = 1,
            children = listOf(formExampleContent)
        ))
    }

    /**
     * Validate that all components are properly registered
     * This method is called automatically during initialization to guarantee component registration
     */
    private fun validateRegistration() {
        // Validate the registry itself
        val registryErrors = sharedRegistry.validateRegistry()
        if (registryErrors.isNotEmpty()) {
            val errorMessage = "Registry validation failed:\n${registryErrors.joinToString("\n")}"
            throw IllegalStateException(errorMessage)
        }

        // Validate all screens
        screens.values.forEach { screen ->
            val missingTokens = sharedRegistry.validateScreenPayload(screen)
            if (missingTokens.isNotEmpty()) {
                val errorMessage = "Screen '${screen.id}' references missing tokens: ${missingTokens.joinToString(", ")}"
                throw IllegalStateException(errorMessage)
            }
        }

        // Log successful validation
        val stats = sharedRegistry.getRegistrationStats()
        println("Component registration validation successful:")
        println("  Total tokens: ${stats["totalTokens"]}")
        println("  Token types: ${stats["tokensByType"]}")
        println("  Screens validated: ${screens.size}")
    }

    /**
     * Get validation report for debugging purposes
     * @return Map containing validation information
     */
    fun getValidationReport(): Map<String, Any> {
        val registryErrors = sharedRegistry.validateRegistry()
        val screenValidation = screens.mapValues { (_, screen) ->
            sharedRegistry.validateScreenPayload(screen)
        }

        return mapOf(
            "registryErrors" to registryErrors,
            "screenValidation" to screenValidation,
            "registrationStats" to sharedRegistry.getRegistrationStats(),
            "isValid" to (registryErrors.isEmpty() && screenValidation.values.all { it.isEmpty() })
        )
    }

    internal val screens = mapOf(
        "home" to SharedScreenPayload(
            id = "home",
            tokens = listOf(
                SharedTokenRef(
                    id = "enhanced_card",
                    bind = mapOf(
                        "title" to "Welcome to SDUI",
                        "description" to "This is a basic home screen",
                        "buttonText" to "Get Started"
                    )
                )
            )
        ),
        "enhanced_home" to SharedScreenPayload(
            id = "enhanced_home",
            tokens = listOf(
                SharedTokenRef(
                    id = "enhanced_card",
                    bind = mapOf(
                        "title" to "Enhanced SDUI",
                        "description" to "This is an enhanced home screen with more features",
                        "buttonText" to "Explore"
                    )
                ),
                SharedTokenRef(
                    id = "slider_example",
                    bind = mapOf(
                        "sliderTitle" to "Adjust Settings",
                        "sliderDescription" to "Use the slider to adjust your preferences"
                    )
                ),
                SharedTokenRef(
                    id = "form_example",
                    bind = mapOf(
                        "formTitle" to "User Form",
                        "formDescription" to "Please fill out the form below"
                    )
                )
            )
        )
    )

    // Initialize validation after all properties are created
    init {
        validateRegistration()
    }

    override suspend fun getScreen(id: String): SharedScreenPayload? {
        return screens[id]
    }

    override suspend fun getRegistry(): SharedTokenRegistry {
        return sharedRegistry
    }
}

/**
 * Implementation of SpecProvider that provides specs from a server.
 * This implementation simulates real server communication with network delays and error handling.
 */
class ServerSpecProviderImpl : SpecProvider {
    private val localProvider = LocalSpecProviderImpl()

    // Simulate network configuration
    private val networkDelayMs = 1000L // 1 second delay to simulate network
    private val failureRate = 0.1 // 10% chance of network failure

    override suspend fun getScreen(id: String): SharedScreenPayload? {
        // Simulate network delay
        kotlinx.coroutines.delay(networkDelayMs)

        // Simulate occasional network failures
        if (kotlin.random.Random.nextDouble() < failureRate) {
            throw RuntimeException("Network error: Failed to fetch screen from server")
        }

        // In a real implementation, this would make an HTTP request like:
        // val response = httpClient.get("https://api.example.com/screens/$id")
        // return response.body<SharedScreenPayload>()

        // For now, return local data with server-like behavior
        val screen = localProvider.getScreen(id)

        // Add server-specific metadata
        return screen?.copy(
            tokens = screen.tokens.map { tokenRef ->
                tokenRef.copy(
                    bind = tokenRef.bind + ("serverTimestamp" to "server_${kotlin.random.Random.nextInt()}")
                )
            }
        )
    }

    override suspend fun getRegistry(): SharedTokenRegistry {
        // Simulate network delay
        kotlinx.coroutines.delay(networkDelayMs)

        // Simulate occasional network failures
        if (kotlin.random.Random.nextDouble() < failureRate) {
            throw RuntimeException("Network error: Failed to fetch registry from server")
        }

        // In a real implementation, this would make an HTTP request like:
        // val response = httpClient.get("https://api.example.com/registry")
        // return response.body<SharedTokenRegistry>()

        // For now, return local registry with server-like behavior
        return localProvider.getRegistry()
    }
}

/**
 * Factory for creating SpecProvider instances.
 */
object SpecProviderFactoryImpl {
    /**
     * Create a SpecProvider based on the specified source.
     * @param source The source of the specs (local or server).
     * @return A SpecProvider instance.
     */
    fun create(source: SpecSource): SpecProvider {
        return when (source) {
            SpecSource.LOCAL -> LocalSpecProviderImpl()
            SpecSource.SERVER -> ServerSpecProviderImpl()
        }
    }
}
