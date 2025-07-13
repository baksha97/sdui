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
        // Create enhanced_card token
        register(SharedCardToken(
            id = "enhanced_card",
            version = 1,
            children = listOf(
                SharedTextToken(
                    id = "enhanced_card.title",
                    version = 1,
                    text = com.baksha97.sdui.shared.models.TemplateString("{{title}}")
                ),
                SharedTextToken(
                    id = "enhanced_card.description", 
                    version = 1,
                    text = com.baksha97.sdui.shared.models.TemplateString("{{description}}")
                ),
                SharedButtonToken(
                    id = "enhanced_card.button",
                    version = 1,
                    text = com.baksha97.sdui.shared.models.TemplateString("{{buttonText}}"),
                    onClick = com.baksha97.sdui.shared.models.Action(
                        type = com.baksha97.sdui.shared.models.ActionType.Custom,
                        data = mapOf("target" to "enhanced_card")
                    )
                )
            )
        ))

        // Create slider_example token
        register(SharedCardToken(
            id = "slider_example",
            version = 1,
            children = listOf(
                SharedTextToken(
                    id = "slider_example.title",
                    version = 1,
                    text = com.baksha97.sdui.shared.models.TemplateString("{{sliderTitle}}")
                ),
                SharedTextToken(
                    id = "slider_example.description",
                    version = 1,
                    text = com.baksha97.sdui.shared.models.TemplateString("{{sliderDescription}}")
                ),
                SharedSliderToken(
                    id = "slider_example.slider",
                    version = 1,
                    initialValue = 0.5f,
                    valueRange = 0f..1f
                )
            )
        ))

        // Create form_example token
        register(SharedBoxToken(
            id = "form_example",
            version = 1,
            children = listOf(
                SharedColumnToken(
                    id = "form_example.content",
                    version = 1,
                    children = listOf(
                        SharedTextToken(
                            id = "form_example.title",
                            version = 1,
                            text = com.baksha97.sdui.shared.models.TemplateString("{{formTitle}}")
                        ),
                        SharedTextToken(
                            id = "form_example.description",
                            version = 1,
                            text = com.baksha97.sdui.shared.models.TemplateString("{{formDescription}}")
                        ),
                        SharedRowToken(
                            id = "form_example.buttons",
                            version = 1,
                            children = listOf(
                                SharedButtonToken(
                                    id = "form_example.cancel",
                                    version = 1,
                                    text = com.baksha97.sdui.shared.models.TemplateString("Cancel"),
                                    onClick = com.baksha97.sdui.shared.models.Action(
                                        type = com.baksha97.sdui.shared.models.ActionType.Custom,
                                        data = mapOf("action" to "cancel")
                                    )
                                ),
                                SharedButtonToken(
                                    id = "form_example.submit",
                                    version = 1,
                                    text = com.baksha97.sdui.shared.models.TemplateString("Submit"),
                                    onClick = com.baksha97.sdui.shared.models.Action(
                                        type = com.baksha97.sdui.shared.models.ActionType.Custom,
                                        data = mapOf("action" to "submit")
                                    )
                                )
                            )
                        )
                    )
                )
            )
        ))
    }

    private val screens = mapOf(
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

    override suspend fun getScreen(id: String): SharedScreenPayload? {
        return screens[id]
    }

    override suspend fun getRegistry(): SharedTokenRegistry {
        return sharedRegistry
    }
}

/**
 * Implementation of SpecProvider that provides specs from a server.
 * This is a mock implementation that would be replaced with actual server communication.
 */
class ServerSpecProviderImpl : SpecProvider {
    // In a real implementation, this would make network requests to fetch specs from a server
    // For now, we'll just use the same specs as the local provider for demonstration purposes
    private val localProvider = LocalSpecProviderImpl()

    override suspend fun getScreen(id: String): SharedScreenPayload? {
        // Simulate a network request
        // In a real implementation, this would make an HTTP request to the server
        return localProvider.getScreen(id)
    }

    override suspend fun getRegistry(): SharedTokenRegistry {
        // Simulate a network request
        // In a real implementation, this would make an HTTP request to the server
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
