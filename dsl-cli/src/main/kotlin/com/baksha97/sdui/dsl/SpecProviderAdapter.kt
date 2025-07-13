package com.baksha97.sdui.dsl

import com.baksha97.sdui.shared.models.SpecProvider
import com.baksha97.sdui.shared.models.ScreenPayload
import com.baksha97.sdui.shared.models.TokenRegistry
import com.baksha97.sdui.shared.models.ComponentContext
import com.baksha97.sdui.shared.models.SpecSource
import com.baksha97.sdui.shared.models.HorizontalAlignment

/**
 * Adapter for the SpecProvider interface from the shared-models module.
 * This allows the dsl-cli module to use the same spec provider interface
 * as the composeApp module, ensuring consistency between local and server specs.
 */
object SpecProviderAdapter {
    /**
     * Converts a dsl-cli TokenRegistry to a shared-models TokenRegistry.
     * This allows tokens created with the dsl-cli module to be used by the composeApp module.
     *
     * @param registry The dsl-cli TokenRegistry to convert.
     * @return A shared-models TokenRegistry containing the same tokens.
     */
    fun convertRegistry(registry: com.baksha97.sdui.dsl.TokenRegistry): TokenRegistry {
        val sharedRegistry = TokenRegistry()

        // For each token in the dsl-cli registry, convert it to a shared-models token
        // and add it to the shared registry
        registry.getAllTokens().values.forEach { token ->
            // This is a simplified conversion that only handles the token ID
            // In a real implementation, you would need to convert all properties
            // of each token type
            when (token) {
                is com.baksha97.sdui.dsl.ColumnToken -> {
                    val sharedToken = com.baksha97.sdui.shared.models.ColumnToken(
                        id = token.id,
                        version = token.version,
                        a11y = null, // Convert a11y if needed
                        padding = null, // Convert padding if needed
                        margin = null, // Convert margin if needed
                        background = null, // Convert background if needed
                        alignment = HorizontalAlignment.Start, // Convert alignment if needed
                        children = emptyList() // Convert children if needed
                    )
                    sharedRegistry.register(sharedToken)
                }
                // Add cases for other token types as needed
                else -> {
                    // For now, we'll just log a message for unsupported token types
                    println("Unsupported token type: ${token::class.simpleName}")
                }
            }
        }

        return sharedRegistry
    }

    /**
     * Converts a dsl-cli ScreenPayload to a shared-models ScreenPayload.
     * This allows screen payloads created with the dsl-cli module to be used by the composeApp module.
     *
     * @param screenPayload The dsl-cli ScreenPayload to convert.
     * @return A shared-models ScreenPayload containing the same token references.
     */
    fun convertScreenPayload(screenPayload: com.baksha97.sdui.dsl.ScreenPayload): ScreenPayload {
        return ScreenPayload(
            id = screenPayload.id,
            tokens = screenPayload.tokens.map { tokenRef ->
                com.baksha97.sdui.shared.models.TokenRef(
                    id = tokenRef.id,
                    bind = tokenRef.bind
                )
            }
        )
    }

    /**
     * Creates a shared-models SpecProvider that uses the dsl-cli module's tokens and screens.
     * This allows the composeApp module to use specs defined in the dsl-cli module.
     *
     * @param screens A map of screen IDs to dsl-cli ScreenPayloads.
     * @param registry The dsl-cli TokenRegistry containing the tokens referenced by the screens.
     * @return A shared-models SpecProvider that provides the converted screens and registry.
     */
    fun createSpecProvider(
        screens: Map<String, com.baksha97.sdui.dsl.ScreenPayload>,
        registry: com.baksha97.sdui.dsl.TokenRegistry
    ): SpecProvider {
        val sharedRegistry = convertRegistry(registry)

        return object : SpecProvider {
            override suspend fun getScreen(id: String): ScreenPayload? {
                val screen = screens[id] ?: return null
                return convertScreenPayload(screen)
            }

            override suspend fun getRegistry(): TokenRegistry {
                return sharedRegistry
            }
        }
    }

    /**
     * Creates a factory for creating SpecProvider instances.
     * This allows the composeApp module to use the same factory pattern for both local and server specs.
     *
     * @param localScreens A map of screen IDs to dsl-cli ScreenPayloads for local specs.
     * @param localRegistry The dsl-cli TokenRegistry containing the tokens referenced by local screens.
     * @param serverScreens A map of screen IDs to dsl-cli ScreenPayloads for server specs.
     * @param serverRegistry The dsl-cli TokenRegistry containing the tokens referenced by server screens.
     * @return A factory that creates SpecProvider instances based on the specified source.
     */
    fun createSpecProviderFactory(
        localScreens: Map<String, com.baksha97.sdui.dsl.ScreenPayload>,
        localRegistry: com.baksha97.sdui.dsl.TokenRegistry,
        serverScreens: Map<String, com.baksha97.sdui.dsl.ScreenPayload>,
        serverRegistry: com.baksha97.sdui.dsl.TokenRegistry
    ): SpecProviderFactory {
        return SpecProviderFactory(
            createSpecProvider(localScreens, localRegistry),
            createSpecProvider(serverScreens, serverRegistry)
        )
    }

    /**
     * Factory for creating SpecProvider instances.
     */
    class SpecProviderFactory(
        private val localProvider: SpecProvider,
        private val serverProvider: SpecProvider
    ) {
        /**
         * Create a SpecProvider based on the specified source.
         * @param source The source of the specs (local or server).
         * @return A SpecProvider instance.
         */
        fun create(source: SpecSource): SpecProvider {
            return when (source) {
                SpecSource.LOCAL -> localProvider
                SpecSource.SERVER -> serverProvider
            }
        }
    }
}
