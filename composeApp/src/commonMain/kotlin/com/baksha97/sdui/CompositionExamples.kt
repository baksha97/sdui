package com.baksha97.sdui

import androidx.compose.ui.Alignment
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable

/**
 * Examples of using the new composition mechanism to create complex UIs by composing tokens together.
 * This file demonstrates how to use the new composition functions to create UIs in a more flexible way.
 */

// Example 1: Compose two cards side by side
val composedCards = compose(
    id = "composed_cards",
    improvedProfileCardDSL,
    improvedArticleCardDSL
)

// Example 2: Create a dashboard by composing multiple components
val dashboard = compose(
    id = "dashboard",
    // Header section
    text {
        text("Dashboard")
        style = TextStyle.HeadlineLarge
        margin {
            all = 16
        }
    },
    // Cards section
    compose(
        id = "dashboard_cards",
        improvedEnhancedCardDSL,
        improvedSliderExampleDSL
    ),
    // Form section
    improvedFormExampleDSL
)

// Example 3: Using the infix 'with' function for more readable composition
val userProfile = profileCard(
    avatarUrl = "https://picsum.photos/56",
    username = "John Doe",
    followers = 1234
) with text {
    text("Bio: Software developer and UI enthusiast")
    style = TextStyle.BodyMedium
    margin {
        top = 8
        horizontal = 16
    }
}

// Example 4: Composing multiple components with the 'with' extension function
val userDashboard = text {
    text("User Dashboard")
    style = TextStyle.HeadlineLarge
    margin {
        all = 16
    }
}.with(
    userProfile,
    divider {
        margin {
            vertical = 16
            horizontal = 16
        }
    },
    improvedLazyListExampleDSL
)

// Example 5: Creating a complex layout by composing components
val complexLayout = column {
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
}.with(
    // Add a divider and the user profile
    divider {
        margin {
            vertical = 16
        }
    },
    userProfile
)

// Example of creating a registry with composed tokens
val composedRegistry = TokenRegistry().apply {
    register(composedCards)
    register(dashboard)
    register(userProfile)
    register(userDashboard)
    register(complexLayout)
}

// Example of a screen using composed tokens
val composedScreenDSL = screenPayload("composed_screen") {
    tokenRef(composedCards.id)
    tokenRef(dashboard.id)
}

// Example of a user screen using composed tokens
val userScreenDSL = screenPayload("user_screen") {
    tokenRef(userDashboard.id)
}

// Example of a complex screen using composed tokens
val complexScreenDSL = screenPayload("complex_screen") {
    tokenRef(complexLayout.id)
}

@Preview
@Composable
fun ComposedScreenPreview() {
    MaterialTheme { Surface { RenderScreen(composedScreenDSL, composedRegistry) } }
}

@Preview
@Composable
fun UserScreenPreview() {
    MaterialTheme { Surface { RenderScreen(userScreenDSL, composedRegistry) } }
}

@Preview
@Composable
fun ComplexScreenPreview() {
    MaterialTheme { Surface { RenderScreen(complexScreenDSL, composedRegistry) } }
}