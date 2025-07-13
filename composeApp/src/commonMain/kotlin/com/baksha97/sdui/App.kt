package com.baksha97.sdui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.baksha97.sdui.shared.models.SpecSource
import com.baksha97.sdui.shared.models.ScreenPayload
import com.baksha97.sdui.shared.models.TokenRegistry

/**
 * Main App function that uses the improved DSL with auto-generated IDs and type-safe prebuilt components.
 * Now supports both local and server-driven UI specs.
 */
@Composable
fun App() {
    MaterialTheme {
        var showEnhancedUI by remember { mutableStateOf(true) }
        var showActionLog by remember { mutableStateOf(false) }
        var lastAction by remember { mutableStateOf<String?>(null) }
        var specSource by remember { mutableStateOf(com.baksha97.sdui.shared.models.SpecSource.LOCAL) }

        // State for loading UI
        var isLoading by remember { mutableStateOf(false) }
        var screen by remember { mutableStateOf<ScreenPayload?>(null) }
        var registry by remember { mutableStateOf<TokenRegistry?>(null) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        // Create the spec provider based on the selected source
        val specProvider = remember(specSource) { 
            SpecProviderFactoryImpl.create(specSource)
        }

        // Coroutine scope for loading specs
        val coroutineScope = rememberCoroutineScope()

        // Function to load specs
        fun loadSpecs() {
            isLoading = true
            errorMessage = null
            coroutineScope.launch {
                try {
                    // Load registry first
                    registry = specProvider.getRegistry()

                    // Then load the appropriate screen
                    val screenId = if (showEnhancedUI) "enhanced_home" else "home"
                    screen = specProvider.getScreen(screenId)

                    if (screen == null) {
                        errorMessage = "Screen not found: $screenId"
                    }
                } catch (e: Exception) {
                    errorMessage = "Error loading specs: ${e.message}"
                } finally {
                    isLoading = false
                }
            }
        }

        // Load specs when source or UI mode changes
        LaunchedEffect(specSource, showEnhancedUI) {
            loadSpecs()
        }

        // Action handler for interactive elements
        val actionHandler: (Action, Map<String, Any>) -> Unit = { action, bindings ->
            lastAction = "Action: ${action.type}, Data: ${action.data}"
            showActionLog = true
            println(lastAction)
        }

        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize()
                .scrollable(rememberScrollState(), orientation = Orientation.Vertical),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // UI Mode Toggle
            Button(onClick = { showEnhancedUI = !showEnhancedUI }) {
                Text(if (showEnhancedUI) "Switch to Basic UI" else "Switch to Enhanced UI")
            }

            // Spec Source Toggle
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Spec Source:", modifier = Modifier.padding(end = 8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = specSource == com.baksha97.sdui.shared.models.SpecSource.LOCAL,
                        onClick = { specSource = com.baksha97.sdui.shared.models.SpecSource.LOCAL }
                    )
                    Text("Local", modifier = Modifier.padding(end = 16.dp))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = specSource == com.baksha97.sdui.shared.models.SpecSource.SERVER,
                        onClick = { specSource = com.baksha97.sdui.shared.models.SpecSource.SERVER }
                    )
                    Text("Server")
                }
            }

            // Action Log
            AnimatedVisibility(showActionLog) {
                Column(
                    Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = lastAction ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Button(onClick = { showActionLog = false }) {
                        Text("Dismiss")
                    }
                }
            }

            // Loading indicator
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }

            // Error message
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // SDUI Content
            if (!isLoading && errorMessage == null && screen != null && registry != null) {
                // Use shared-models types directly with the new overloaded RenderScreen function
                RenderScreen(
                    screen = screen!!,
                    registry = registry!!,
                    onMissingToken = { tokenId ->
                        println("Missing token: $tokenId")
                    },
                    onAction = actionHandler
                )
            }
        }
    }
}

@Preview
@Composable
private fun AppPreview() {
    App()
}
