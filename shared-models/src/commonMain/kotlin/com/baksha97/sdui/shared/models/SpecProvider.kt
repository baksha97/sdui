package com.baksha97.sdui.shared.models

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
 * Enum representing the source of UI specifications.
 */
enum class SpecSource {
    LOCAL,
    SERVER
}