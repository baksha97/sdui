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
 * Adapter functions to convert between composeApp and shared-models types
 */

// Convert composeApp ScreenPayload to shared-models ScreenPayload
fun ScreenPayload.toSharedScreenPayload(): SharedScreenPayload {
    return SharedScreenPayload(
        id = id,
        tokens = tokens.map { tokenRef ->
            SharedTokenRef(
                id = tokenRef.id,
                bind = tokenRef.bind.mapValues { it.value.toString() }
            )
        }
    )
}

// Convert composeApp TokenRegistry to shared-models TokenRegistry
fun TokenRegistry.toSharedTokenRegistry(): SharedTokenRegistry {
    val sharedRegistry = SharedTokenRegistry()

    // For now, we'll just create an empty registry
    // In a real implementation, you would need to convert all tokens

    return sharedRegistry
}

// Convert shared-models ScreenPayload to composeApp ScreenPayload
fun SharedScreenPayload.toAppScreenPayload(): ScreenPayload {
    return ScreenPayload(
        id = id,
        tokens = tokens.map { tokenRef ->
            TokenRef(
                id = tokenRef.id,
                bind = tokenRef.bind.mapValues { it.value as Any }
            )
        }
    )
}

// Convert shared-models TokenRegistry to composeApp TokenRegistry
fun SharedTokenRegistry.toAppTokenRegistry(): TokenRegistry {
    val appRegistry = TokenRegistry()

    // For now, we'll just create an empty registry
    // In a real implementation, you would need to convert all tokens

    return appRegistry
}

/**
 * Implementation of SpecProvider that provides specs from local sources.
 */
class LocalSpecProviderImpl : SpecProvider {
    private val screens = mapOf(
        "home" to improvedHomeScreenDSL,
        "enhanced_home" to improvedEnhancedScreenDSL
    )

    override suspend fun getScreen(id: String): SharedScreenPayload? {
        val screen = screens[id] ?: return null
        return screen.toSharedScreenPayload()
    }

    override suspend fun getRegistry(): SharedTokenRegistry {
        return improvedRegistryDSL.toSharedTokenRegistry()
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
