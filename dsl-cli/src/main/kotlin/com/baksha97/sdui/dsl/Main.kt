package com.baksha97.sdui.dsl

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.encodeToString
import java.io.File
import com.baksha97.sdui.shared.models.*

/**
 * Main entry point for the DSL CLI tool.
 * This tool allows generating JSON from DSL code outside of the app.
 */
fun main(args: Array<String>) {
    println("DSL CLI Tool")
    println("============")

    if (args.isEmpty()) {
        printUsage()
        return
    }

    when (args[0]) {
        "generate" -> handleGenerate(args.drop(1).toTypedArray())
        "schema" -> handleSchema(args.drop(1).toTypedArray())
        "render" -> handleRender(args.drop(1).toTypedArray())
        "migrate" -> handleMigrate(args.drop(1).toTypedArray())
        "help" -> printUsage()
        else -> {
            println("Unknown command: ${args[0]}")
            printUsage()
        }
    }
}

/**
 * Handles the 'generate' command.
 */
private fun handleGenerate(args: Array<String>) {
    if (args.size < 2) {
        println("Error: The 'generate' command requires a component type and an output file.")
        println("Usage: generate <component-type> <output-file>")
        return
    }

    val componentType = args[0]
    val outputFile = args[1]

    println("Generating $componentType to $outputFile...")

    try {
        val json = generateJson(componentType)
        File(outputFile).writeText(json)
        println("Successfully generated JSON to $outputFile")
    } catch (e: Exception) {
        println("Error generating JSON: ${e.message}")
        e.printStackTrace()
    }
}

/**
 * Generates JSON for the specified component type.
 */
private fun generateJson(componentType: String): String {
    val json = Json { 
        prettyPrint = true
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    val token = when (componentType) {
        "profile-card" -> generateProfileCard()
        "article-card" -> generateArticleCard()
        "composed-cards" -> generateComposedCards()
        "dashboard" -> generateDashboard()
        "enhanced-card" -> generateEnhancedCard()
        "form" -> generateForm()
        "slider" -> generateSlider()
        "lazy-list" -> generateLazyList()
        else -> throw IllegalArgumentException("Unknown component type: $componentType")
    }

    return json.encodeToString(TokenSerializer, token)
}

/**
 * Generates a profile card component.
 */
private fun generateProfileCard(): Token {
    return profileCard(
        avatarUrl = "https://picsum.photos/56",
        username = "John Doe",
        followers = 1234
    )
}

/**
 * Generates an article card component.
 */
private fun generateArticleCard(): Token {
    return articleCard(
        thumbUrl = "https://picsum.photos/400/250",
        headline = "Server-driven UI cuts release time"
    )
}

/**
 * Generates a composed component with multiple cards in a row.
 * This demonstrates the new add() function for composing tokens.
 */
private fun generateComposedCards(): Token {
    return row("composed_cards") {
        padding {
            all = 16
        }

        // Add existing tokens directly to the row
        add(
            generateProfileCard(),
            generateArticleCard()
        )
    }
}

/**
 * Generates a dashboard component that demonstrates more complex composition.
 * This shows how to compose multiple components into a single UI.
 */
private fun generateDashboard(): Token {
    return column("dashboard") {
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
            padding {
                horizontal = 16
            }

            add(
                generateEnhancedCard(),
                generateSlider()
            )
        }

        // Form section - add an existing form
        add(generateForm())

        // List section
        add(generateLazyList())
    }
}

/**
 * Generates an enhanced card component.
 */
private fun generateEnhancedCard(): Token {
    return enhancedCard(
        title = "Enhanced Card Example",
        description = "This card demonstrates the new CardToken with styling and a button.",
        buttonText = "Learn More"
    )
}

/**
 * Generates a form component.
 */
private fun generateForm(): Token {
    return formExample(
        formTitle = "Contact Form",
        formDescription = "Fill out this form to get in touch with us."
    )
}

/**
 * Generates a slider component.
 */
private fun generateSlider(): Token {
    return sliderExample(
        sliderTitle = "Local State Management",
        sliderDescription = "This slider demonstrates local state management."
    )
}

/**
 * Generates a lazy list component.
 */
private fun generateLazyList(): Token {
    return lazyListExample(
        itemCount = 5
    )
}

/**
 * Handles the 'schema' command.
 */
private fun handleSchema(args: Array<String>) {
    if (args.isEmpty()) {
        println("Error: The 'schema' command requires an output file.")
        println("Usage: schema <output-file>")
        return
    }

    val outputFile = args[0]
    println("Generating schema to $outputFile...")

    try {
        val schemaGenerator = SchemaGenerator()
        val schema = schemaGenerator.generateTokenSchema()
        val json = Json { 
            prettyPrint = true
            encodeDefaults = true
            ignoreUnknownKeys = true
        }
        val schemaJson = json.encodeToString(schema)
        File(outputFile).writeText(schemaJson)
        println("Successfully generated schema to $outputFile")
    } catch (e: Exception) {
        println("Error generating schema: ${e.message}")
        e.printStackTrace()
    }
}

/**
 * Handles the 'render' command.
 */
private fun handleRender(args: Array<String>) {
    if (args.size < 2) {
        println("Error: The 'render' command requires an input file and an output file.")
        println("Usage: render <input-file> <output-file>")
        return
    }

    val inputFile = args[0]
    val outputFile = args[1]
    println("Rendering $inputFile to $outputFile...")

    try {
        // Read the input file
        val jsonString = File(inputFile).readText()

        // Parse the JSON
        val json = Json { 
            prettyPrint = true
            encodeDefaults = true
            ignoreUnknownKeys = true
        }
        val jsonElement = json.parseToJsonElement(jsonString)
        val inputJsonObject = jsonElement.jsonObject

        // Generate the schema
        val schemaGenerator = SchemaGenerator()
        val schema = schemaGenerator.generateTokenSchema()

        // Render the token
        val renderer = SchemaRenderer()
        val token = renderer.renderToken(inputJsonObject, schema)

        if (token != null) {
            // Serialize the token back to JSON
            val outputJsonString = json.encodeToString(TokenSerializer, token)
            File(outputFile).writeText(outputJsonString)
            println("Successfully rendered token to $outputFile")
        } else {
            println("Error: Failed to render token from $inputFile")
        }
    } catch (e: Exception) {
        println("Error rendering token: ${e.message}")
        e.printStackTrace()
    }
}

/**
 * Handles the 'migrate' command.
 */
private fun handleMigrate(args: Array<String>) {
    if (args.size < 3) {
        println("Error: The 'migrate' command requires an input file, an output file, and a target version.")
        println("Usage: migrate <input-file> <output-file> <target-version>")
        return
    }

    val inputFile = args[0]
    val outputFile = args[1]
    val targetVersion = args[2].toIntOrNull()

    if (targetVersion == null) {
        println("Error: Target version must be a valid integer.")
        return
    }

    println("Migrating $inputFile to version $targetVersion and saving to $outputFile...")

    try {
        // Read the input file
        val jsonString = File(inputFile).readText()
        val json = Json { 
            prettyPrint = true
            encodeDefaults = true
            ignoreUnknownKeys = true
        }
        val jsonObject = json.parseToJsonElement(jsonString).jsonObject

        // Migrate the token
        val versionManager = VersionManager()
        val migratedJson = versionManager.migrateTokenJson(jsonObject, targetVersion)

        if (migratedJson != null) {
            // Write the migrated token to the output file
            val migratedJsonString = json.encodeToString(migratedJson)
            File(outputFile).writeText(migratedJsonString)
            println("Successfully migrated token to version $targetVersion and saved to $outputFile")
        } else {
            println("Error: Failed to migrate token from $inputFile to version $targetVersion")
        }
    } catch (e: Exception) {
        println("Error migrating token: ${e.message}")
        e.printStackTrace()
    }
}

/**
 * Prints usage information.
 */
private fun printUsage() {
    println("Usage: dsl-cli <command> [arguments]")
    println()
    println("Commands:")
    println("  generate <component-type> <output-file>  Generate JSON for a component")
    println("  schema <output-file>                     Generate JSON Schema for all token types")
    println("  render <input-file> <output-file>        Render a token from JSON")
    println("  migrate <input-file> <output-file> <target-version>  Migrate a token to a specific version")
    println("  help                                     Show this help message")
    println()
    println("Component Types:")
    println("  profile-card    Generate a profile card component")
    println("  article-card    Generate an article card component")
    println("  composed-cards  Generate a row with composed cards (demonstrates token composition)")
    println("  dashboard       Generate a dashboard with multiple composed components")
    println("  enhanced-card   Generate an enhanced card component")
    println("  form            Generate a form component")
    println("  slider          Generate a slider component")
    println("  lazy-list       Generate a lazy list component")
}
