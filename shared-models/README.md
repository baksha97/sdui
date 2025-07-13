# Context Parameters Migration Guide

## Overview

This document provides guidance on migrating from experimental context receivers to context parameters in Kotlin 2.2.0.

## Changes Made

1. Updated the compiler flag in `build.gradle.kts` from `-Xcontext-receivers` to `-Xcontext-parameters`.
2. Migrated DSL functions from using context receivers to using context parameters.
3. Updated the `ui` functions to use regular function parameters instead of context receivers.

## Using the Updated DSL

### Before (with context receivers)

```kotlin
ui {
    Column {
        Text {
            text("Hello, World!")
        }
    }
}
```

### After (with context parameters)

```kotlin
ui { scope ->
    Column(scope) {
        Text(scope) {
            text("Hello, World!")
        }
    }
}
```

## Why Context Parameters?

Context parameters are the successor to experimental context receivers in Kotlin 2.2.0. They provide a more stable and standardized way to achieve the same functionality.

Benefits include:
- Better IDE support
- Improved type inference
- More consistent behavior with other Kotlin features
- Future compatibility with Kotlin releases

For more details, see the context parameters proposal: https://kotl.in/context-parameters