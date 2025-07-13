package com.baksha97.sdui

/**
 * Examples of using the improved DSL with auto-generated IDs and type-safe prebuilt components.
 * This file demonstrates how to use the improved DSL to create UI components in a more concise and readable way.
 */

// Example of using the type-safe profileCard function
val improvedProfileCardDSL = profileCard(
    avatarUrl = "https://picsum.photos/56",
    username = "Travis",
    followers = 874
)

// Example of using the type-safe articleCard function
val improvedArticleCardDSL = articleCard(
    thumbUrl = "https://picsum.photos/400/250",
    headline = "Server-driven UI cuts release time"
)

// Example of using the type-safe enhancedCard function
val improvedEnhancedCardDSL = enhancedCard(
    title = "Enhanced Card Example",
    description = "This card demonstrates the new CardToken with styling and a button.",
    buttonText = "Learn More"
)

// Example of using the type-safe formExample function
val improvedFormExampleDSL = formExample(
    formTitle = "Contact Form",
    formDescription = "Fill out this form to get in touch with us."
)

// Example of using the type-safe sliderExample function
val improvedSliderExampleDSL = sliderExample(
    sliderTitle = "Local State Management",
    sliderDescription = "This slider demonstrates local state management. Move the slider and it will maintain its position even though the server doesn't know about the change."
)

// Example of using the type-safe lazyListExample function
val improvedLazyListExampleDSL = lazyListExample(
    itemCount = 5
)

// Example of a home screen using the improved DSL
val improvedHomeScreenDSL = screenPayload("home") {
    tokenRef(improvedProfileCardDSL.id) {
        // No need to bind data, as it's already provided in the component
    }

    tokenRef(improvedArticleCardDSL.id) {
        // No need to bind data, as it's already provided in the component
    }
}

// Example of an enhanced screen using the improved DSL
val improvedEnhancedScreenDSL = screenPayload("enhanced_home") {
    tokenRef(improvedEnhancedCardDSL.id) {
        // No need to bind data, as it's already provided in the component
    }

    tokenRef(improvedSliderExampleDSL.id) {
        // No need to bind data, as it's already provided in the component
    }

    tokenRef(improvedFormExampleDSL.id) {
        // No need to bind data, as it's already provided in the component
    }

    tokenRef(improvedLazyListExampleDSL.id) {
        // No need to bind data, as it's already provided in the component
    }
}

// Example of creating a registry using the improved DSL
val improvedRegistryDSL = TokenRegistry().apply {
    register(improvedProfileCardDSL)
    register(improvedArticleCardDSL)
    register(improvedEnhancedCardDSL)
    register(improvedFormExampleDSL)
    register(improvedSliderExampleDSL)
    register(improvedLazyListExampleDSL)
}