package com.baksha97.sdui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

// Import improved DSL examples
import com.baksha97.sdui.improvedHomeScreenDSL
import com.baksha97.sdui.improvedEnhancedScreenDSL
import com.baksha97.sdui.improvedRegistryDSL

/**
 * Main App function that uses the improved DSL with auto-generated IDs and type-safe prebuilt components.
 */
@Composable
fun App() {
    MaterialTheme {
        var showEnhancedUI by remember { mutableStateOf(true) }
        var showActionLog by remember { mutableStateOf(false) }
        var lastAction by remember { mutableStateOf<String?>(null) }

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

            // SDUI Content using improved DSL examples
            if (showEnhancedUI) {
                // Create a custom registry with action handling
                val registry = remember {
                    improvedRegistryDSL
                }

                // Render the enhanced screen with the custom registry
                RenderScreen(
                    screen = improvedEnhancedScreenDSL,
                    registry = registry,
                    onMissingToken = { tokenId ->
                        println("Missing token: $tokenId")
                    },
                    onAction = actionHandler
                )
            } else {
                // Render the original screen
                RenderScreen(improvedHomeScreenDSL, improvedRegistryDSL)
            }
        }
    }
}

@Preview
@Composable
private fun AppPreview() {
    App()
}

