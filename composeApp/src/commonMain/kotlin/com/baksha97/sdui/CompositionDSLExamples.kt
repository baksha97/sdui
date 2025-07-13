package com.baksha97.sdui

import androidx.compose.ui.Alignment
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable

/**
 * Examples of using the built-in composition mechanism in the DSL.
 * This file demonstrates how to compose tokens directly within the DSL without using the separate compose function.
 */

// Example 1: Compose two cards side by side in a row
val dslComposedCards = row("composed_cards") {
    // Add existing tokens directly to the row
    add(
        improvedProfileCardDSL,
        improvedArticleCardDSL
    )
}

// Example 2: Create a dashboard by composing multiple components
val dslDashboard = column("dashboard") {
    // Header section
    text {
        text("Dashboard")
        style = TextStyle.HeadlineLarge
        margin {
            all = 16
        }
    }
    
    // Cards section - add a row containing two cards
    row("dashboard_cards") {
        add(
            improvedEnhancedCardDSL,
            improvedSliderExampleDSL
        )
    }
    
    // Form section - add an existing form
    add(improvedFormExampleDSL)
}

// Example 3: Creating a complex layout by composing components
val dslComplexLayout = column {
    padding {
        all = 16
    }
    
    text {
        text("Welcome to the App")
        style = TextStyle.HeadlineMedium
        margin {
            bottom = 16
        }
    }
    
    // Compose cards in a row
    row {
        alignment = Alignment.CenterVertically
        padding {
            vertical = 8
        }
        
        card {
            padding {
                all = 12
            }
            elevation = 2
            
            text {
                text("Card 1")
                style = TextStyle.TitleMedium
            }
        }
        
        spacer {
            width = 16
        }
        
        card {
            padding {
                all = 12
            }
            elevation = 2
            
            text {
                text("Card 2")
                style = TextStyle.TitleMedium
            }
        }
    }
    
    // Add a divider
    divider {
        margin {
            vertical = 16
        }
    }
    
    // Add an existing profile card
    add(improvedProfileCardDSL)
}

// Example of creating a registry with composed tokens
val dslComposedRegistry = TokenRegistry().apply {
    register(dslComposedCards)
    register(dslDashboard)
    register(dslComplexLayout)
}

// Example of a screen using composed tokens
val dslComposedScreenDSL = screenPayload("dsl_composed_screen") {
    tokenRef(dslComposedCards.id)
    tokenRef(dslDashboard.id)
}

// Example of a complex screen using composed tokens
val dslComplexScreenDSL = screenPayload("dsl_complex_screen") {
    tokenRef(dslComplexLayout.id)
}

@Preview
@Composable
fun DslComposedScreenPreview() {
    MaterialTheme { Surface { RenderScreen(dslComposedScreenDSL, dslComposedRegistry) } }
}

@Preview
@Composable
fun DslComplexScreenPreview() {
    MaterialTheme { Surface { RenderScreen(dslComplexScreenDSL, dslComposedRegistry) } }
}