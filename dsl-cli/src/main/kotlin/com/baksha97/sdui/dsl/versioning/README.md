# Semantic Versioning System

This package provides a simple yet flexible semantic versioning system for the SDUI project.

## Overview

The semantic versioning system replaces the previous integer-based versioning with a more expressive and flexible approach based on [Semantic Versioning 2.0.0](https://semver.org/).

Key components:

1. **SemanticVersion**: Represents a version in the format MAJOR.MINOR.PATCH
2. **VersionRegistry**: Central registry for managing versions across the project
3. **SemanticVersionManager**: Handles version compatibility and migration
4. **TokenAdapter**: Bridges between existing Token classes and the new versioning system

## Benefits

- **More expressive versioning**: Clearly distinguish between breaking changes (MAJOR), new features (MINOR), and bug fixes (PATCH)
- **Centralized version management**: Track all versions in one place
- **Flexible migration paths**: Define custom migration paths between specific versions
- **Backward compatibility**: Works with existing Token classes through the adapter

## Usage Examples

### Basic Usage

```kotlin
// Create a semantic version
val version = SemanticVersion(1, 0, 0)

// Or from a string
val versionFromString = SemanticVersion.fromString("1.2.3")

// Compare versions
if (version < versionFromString) {
    println("Version is older")
}

// Check compatibility
if (version.isCompatibleWith(versionFromString)) {
    println("Versions are compatible")
}
```

### Using the Version Registry

```kotlin
// Create a registry
val registry = VersionRegistry()

// Register component versions
registry.registerVersion("TextToken", "1.0.0")
registry.registerVersion("ButtonToken", SemanticVersion(1, 1, 0))

// Get a component version
val textVersion = registry.getVersion("TextToken")

// Update a component version
registry.updateVersion("TextToken", textVersion!!.incrementMinor())

// Register a migration path
registry.registerMigrationPath(
    "TextToken",
    SemanticVersion(1, 0, 0),
    SemanticVersion(1, 1, 0),
    MigrationPath(
        description = "Added text alignment property",
        isBreaking = false,
        migrationSteps = listOf("Copy all properties", "Set default alignment to Start")
    )
)
```

### Using the Version Manager

```kotlin
// Create a version manager with a registry
val registry = VersionRegistry()
val versionManager = SemanticVersionManager(registry)

// Check if a token is compatible with a client version
val isCompatible = versionManager.isCompatible(
    token = versionedToken,
    clientVersion = SemanticVersion(1, 0, 0)
)

// Migrate a token to a new version
val migratedToken = versionManager.migrateToken(
    token = versionedToken,
    targetVersion = SemanticVersion(1, 1, 0)
)
```

### Using the Token Adapter

```kotlin
// Convert an existing Token to a VersionedToken
val token = TextToken(
    id = "text1",
    version = 1,
    text = TemplateString("Hello World")
)
val versionedToken = TokenAdapter.toVersionedToken(token)

// Use the versioned token with the version manager
val migratedToken = versionManager.migrateToken(
    token = versionedToken,
    targetVersion = SemanticVersion(1, 1, 0)
)

// Convert back to a regular Token
val updatedToken = TokenAdapter.fromVersionedToken(migratedToken!!)
```

## Migration Strategy

When migrating between versions, the system follows these rules:

1. If the source and target versions are the same, return the token as is
2. If the source version is greater than the target version, migration is not possible
3. If a custom migration path is registered, use it
4. Otherwise, use default migration logic:
   - For container tokens, recursively migrate children
   - For simple tokens, just update the version

## Extending the System

You can extend the system by:

1. Creating custom migration paths for specific version transitions
2. Implementing the VersionedToken interface in your own token classes
3. Adding custom migration logic in the SemanticVersionManager