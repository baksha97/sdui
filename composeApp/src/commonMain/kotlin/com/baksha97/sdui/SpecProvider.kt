package com.baksha97.sdui

/**
 * Interface for providing UI specifications.
 * This allows for a common approach to getting UI specs from either local or server sources.
 */
interface SpecProvider {
    /**
     * Get a screen payload by ID.
     * @param id The ID of the screen to get.
     * @return The screen payload, or null if not found.
     */
    suspend fun getScreen(id: String): ScreenPayload?

    /**
     * Get the token registry containing all available tokens.
     * @return The token registry.
     */
    suspend fun getRegistry(): TokenRegistry
}

/**
 * Implementation of SpecProvider that provides specs from local sources.
 */
class LocalSpecProvider : SpecProvider {
    private val screens = mapOf(
        "home" to improvedHomeScreenDSL,
        "enhanced_home" to improvedEnhancedScreenDSL
    )

    override suspend fun getScreen(id: String): ScreenPayload? {
        return screens[id]
    }

    override suspend fun getRegistry(): TokenRegistry {
        return improvedRegistryDSL
    }
}

/**
 * Implementation of SpecProvider that provides specs from a server.
 * This is a mock implementation that would be replaced with actual server communication.
 */
class ServerSpecProvider : SpecProvider {
    // In a real implementation, this would make network requests to fetch specs from a server
    // For now, we'll just use the same specs as the local provider for demonstration purposes
    private val localProvider = LocalSpecProvider()

    override suspend fun getScreen(id: String): ScreenPayload? {
        // Simulate a network request
        // In a real implementation, this would make an HTTP request to the server
        return localProvider.getScreen(id)
    }

    override suspend fun getRegistry(): TokenRegistry {
        // Simulate a network request
        // In a real implementation, this would make an HTTP request to the server
        return localProvider.getRegistry()
    }
}

/**
 * Factory for creating SpecProvider instances.
 */
object SpecProviderFactory {
    /**
     * Create a SpecProvider based on the specified source.
     * @param source The source of the specs (local or server).
     * @return A SpecProvider instance.
     */
    fun create(source: SpecSource): SpecProvider {
        return when (source) {
            SpecSource.LOCAL -> LocalSpecProvider()
            SpecSource.SERVER -> ServerSpecProvider()
        }
    }
}

/**
 * Enum representing the source of UI specifications.
 */
enum class SpecSource {
    LOCAL,
    SERVER
}
