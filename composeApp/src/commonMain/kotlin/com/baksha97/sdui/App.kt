package com.baksha97.sdui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import sdui.composeapp.generated.resources.Res
import sdui.composeapp.generated.resources.compose_multiplatform

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

            // SDUI Content
            if (showEnhancedUI) {
                // Create a custom registry with action handling
                val registry = remember {
                    enhancedRegistry
                }

                // Render the enhanced screen with the custom registry
                RenderScreen(
                    screen = enhancedScreen,
                    registry = registry,
                    onMissingToken = { tokenId ->
                        println("Missing token: $tokenId")
                    },
                    onAction = actionHandler
                )
            } else {
                // Render the original screen
                RenderScreen(homeScreen)
            }
        }
    }
}

@Preview
@Composable
private fun AppPreview() {
    App()
}
