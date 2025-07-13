# Composition Guide: Building Complex UIs with the DSL

This guide explains how to compose UI components together using the DSL.

## Old Approach: Using the `compose` Function

Previously, to compose multiple tokens together, you would use the `compose` function or the `with` extension function:

```kotlin
// Using the compose function
val composedCards = compose(
    id = "composed_cards",
    improvedProfileCardDSL,
    improvedArticleCardDSL
)

// Using the with extension function
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
```

While this approach works, it feels less integrated with the DSL and requires using separate functions outside the normal DSL flow.

## New Approach: Direct Composition in the DSL

The DSL now supports direct composition of tokens within the builders themselves. This allows for a more natural and ergonomic way to compose components together.

### Using the `add` Method

You can use the `add` method to add existing tokens to any container builder:

```kotlin
// Compose two cards side by side in a row
val dslComposedCards = row("composed_cards") {
    // Add existing tokens directly to the row
    add(
        improvedProfileCardDSL,
        improvedArticleCardDSL
    )
}
```

### Nested Composition

You can also compose components at multiple levels:

```kotlin
// Create a dashboard by composing multiple components
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
```

### Mixing Inline and Existing Components

You can mix inline component definitions with existing components:

```kotlin
val dslComplexLayout = column {
    // Inline components
    text {
        text("Welcome to the App")
        style = TextStyle.HeadlineMedium
    }
    
    // Existing components
    add(improvedProfileCardDSL)
}
```

## Benefits of the New Approach

1. **More Ergonomic**: The composition is built directly into the DSL, making it feel more natural and integrated.
2. **Better Readability**: The code reads more like a hierarchical structure, which better reflects the UI structure.
3. **Flexibility**: You can easily mix inline components with existing components at any level of nesting.
4. **Consistency**: The same pattern is used for all container components (column, row, box, card, etc.).

## Supported Container Types

The following container types support direct composition:

- `column`
- `row`
- `box`
- `card`
- `lazyColumn`
- `lazyRow`

## Examples

For complete examples of how to use the new composition mechanism, see the `CompositionDSLExamples.kt` file.