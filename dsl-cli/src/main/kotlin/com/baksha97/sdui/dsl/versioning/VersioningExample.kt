package com.baksha97.sdui.dsl.versioning

import com.baksha97.sdui.shared.models.*
import com.baksha97.sdui.dsl.*
import kotlinx.serialization.json.*
import java.io.File

/**
 * Example class demonstrating how to use the semantic versioning system.
 * 
 * This class provides practical examples of how to use the various components
 * of the semantic versioning system in real-world scenarios.
 */
object VersioningExample {

    /**
     * Demonstrates basic usage of the semantic versioning system.
     */
    fun basicExample() {
        println("=== Basic Semantic Versioning Example ===")

        // Create semantic versions
        val v1 = SemanticVersion(1, 0, 0)
        val v2 = SemanticVersion(1, 1, 0)
        val v3 = SemanticVersion(2, 0, 0)

        println("v1: $v1")
        println("v2: $v2")
        println("v3: $v3")

        // Compare versions
        println("v1 < v2: ${v1 < v2}")
        println("v2 < v3: ${v2 < v3}")

        // Check compatibility
        println("v1 compatible with v2: ${v1.isCompatibleWith(v2)}")
        println("v2 compatible with v1: ${v2.isCompatibleWith(v1)}")
        println("v2 compatible with v3: ${v2.isCompatibleWith(v3)}")

        // Increment versions
        println("v1 increment patch: ${v1.incrementPatch()}")
        println("v1 increment minor: ${v1.incrementMinor()}")
        println("v1 increment major: ${v1.incrementMajor()}")
    }

    /**
     * Demonstrates using the version registry.
     */
    fun registryExample() {
        println("\n=== Version Registry Example ===")

        // Create a registry
        val registry = VersionRegistry()

        // Register component versions
        registry.registerVersion("TextToken", "1.0.0")
        registry.registerVersion("ButtonToken", SemanticVersion(1, 1, 0))
        registry.registerVersion("ColumnToken", SemanticVersion(2, 0, 0))

        // Get component versions
        println("TextToken version: ${registry.getVersion("TextToken")}")
        println("ButtonToken version: ${registry.getVersion("ButtonToken")}")
        println("ColumnToken version: ${registry.getVersion("ColumnToken")}")

        // Update a version
        val textVersion = registry.getVersion("TextToken")
        if (textVersion != null) {
            registry.updateVersion("TextToken", textVersion.incrementMinor())
            println("Updated TextToken version: ${registry.getVersion("TextToken")}")
        }

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

        // Get a migration path
        val migrationPath = registry.getMigrationPath(
            "TextToken",
            SemanticVersion(1, 0, 0),
            SemanticVersion(1, 1, 0)
        )

        println("Migration path: ${migrationPath?.description}")
        println("Migration steps: ${migrationPath?.migrationSteps}")
    }

    /**
     * Demonstrates using the token adapter with existing tokens.
     */
    fun adapterExample() {
        println("\n=== Token Adapter Example ===")

        // Create a simple token
        val textToken = TextToken(
            id = "text1",
            version = 1,
            text = TemplateString("Hello World"),
            style = TextStyle.BodyMedium
        )

        // Create a container token
        val columnToken = ColumnToken(
            id = "column1",
            version = 1,
            children = listOf(textToken)
        )

        // Convert to versioned tokens
        val versionedTextToken = TokenAdapter.toVersionedToken(textToken)
        val versionedColumnToken = TokenAdapter.toVersionedToken(columnToken)

        println("Original text token version: ${textToken.version}")
        println("Versioned text token version: ${versionedTextToken.version}")

        println("Original column token version: ${columnToken.version}")
        println("Versioned column token version: ${versionedColumnToken.version}")

        // Convert back to regular tokens
        val convertedTextToken = TokenAdapter.fromVersionedToken(versionedTextToken)
        val convertedColumnToken = TokenAdapter.fromVersionedToken(versionedColumnToken)

        println("Converted text token version: ${convertedTextToken.version}")
        println("Converted column token version: ${convertedColumnToken.version}")
    }

    /**
     * Demonstrates using the version manager to migrate tokens.
     */
    fun versionManagerExample() {
        println("\n=== Version Manager Example ===")

        // Create a registry and version manager
        val registry = VersionRegistry()
        val versionManager = SemanticVersionManager(registry)

        // Register component versions and migration paths
        registry.registerVersion("TextToken", "1.0.0")
        registry.registerMigrationPath(
            "TextToken",
            SemanticVersion(1, 0, 0),
            SemanticVersion(1, 1, 0),
            MigrationPath(
                description = "Added text alignment property",
                isBreaking = false
            )
        )

        // Create a token
        val textToken = TextToken(
            id = "text1",
            version = 1,
            text = TemplateString("Hello World"),
            style = TextStyle.BodyMedium
        )

        // Convert to a versioned token
        val versionedToken = TokenAdapter.toVersionedToken(textToken)

        // Check compatibility
        val isCompatible = versionManager.isCompatible(
            versionedToken,
            SemanticVersion(1, 0, 0)
        )
        println("Token compatible with v1.0.0: $isCompatible")

        // Migrate the token
        val migratedToken = versionManager.migrateToken(
            versionedToken,
            SemanticVersion(1, 1, 0)
        )

        if (migratedToken != null) {
            println("Migrated token version: ${migratedToken.version}")

            // Convert back to a regular token
            val convertedToken = TokenAdapter.fromVersionedToken(migratedToken)
            println("Converted token version: ${convertedToken.version}")
        } else {
            println("Migration failed")
        }
    }

    /**
     * Demonstrates a complete workflow using the semantic versioning system.
     */
    fun completeExample() {
        println("\n=== Complete Workflow Example ===")

        // 1. Set up the version registry
        val registry = VersionRegistry()
        val versionManager = SemanticVersionManager(registry)

        // 2. Register initial component versions
        registry.registerVersion("TextToken", "1.0.0")
        registry.registerVersion("ButtonToken", "1.0.0")
        registry.registerVersion("ColumnToken", "1.0.0")

        println("Initial versions:")
        println("TextToken: ${registry.getVersion("TextToken")}")
        println("ButtonToken: ${registry.getVersion("ButtonToken")}")
        println("ColumnToken: ${registry.getVersion("ColumnToken")}")

        // 3. Create a UI with tokens
        val buttonToken = ButtonToken(
            id = "button1",
            version = 1,
            text = TemplateString("Click Me"),
            onClick = Action(ActionType.Custom)
        )

        val textToken = TextToken(
            id = "text1",
            version = 1,
            text = TemplateString("Hello World"),
            style = TextStyle.BodyMedium
        )

        val columnToken = ColumnToken(
            id = "column1",
            version = 1,
            children = listOf(textToken, buttonToken)
        )

        // 4. Convert to versioned tokens
        val versionedColumnToken = TokenAdapter.toVersionedToken(columnToken) as VersionedContainerToken

        // 5. Update a component version (simulate a new release)
        registry.updateVersion("TextToken", SemanticVersion(1, 1, 0))
        registry.registerMigrationPath(
            "TextToken",
            SemanticVersion(1, 0, 0),
            SemanticVersion(1, 1, 0),
            MigrationPath(
                description = "Added text alignment property",
                isBreaking = false
            )
        )

        println("\nUpdated versions:")
        println("TextToken: ${registry.getVersion("TextToken")}")

        // 6. Migrate the UI to the latest versions
        val targetVersion = SemanticVersion(1, 1, 0)
        val migratedToken = versionManager.migrateToken(versionedColumnToken, targetVersion)

        if (migratedToken != null) {
            println("\nMigration successful")
            println("Migrated token version: ${migratedToken.version}")

            // 7. Convert back to regular tokens
            val convertedToken = TokenAdapter.fromVersionedToken(migratedToken)
            println("Converted token version: ${convertedToken.version}")

            // 8. Check the children
            if (convertedToken is ColumnToken) {
                println("Children count: ${convertedToken.children.size}")
                convertedToken.children.forEachIndexed { index, child ->
                    println("Child $index type: ${child.javaClass.simpleName}, version: ${child.version}")
                }
            }
        } else {
            println("Migration failed")
        }
    }

    /**
     * Run all examples.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        basicExample()
        registryExample()
        adapterExample()
        versionManagerExample()
        completeExample()
    }
}
