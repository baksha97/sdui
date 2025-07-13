package com.baksha97.sdui.dsl

// Import extension functions
import com.baksha97.sdui.shared.models.ActionType
import com.baksha97.sdui.shared.models.ButtonStyle
import com.baksha97.sdui.shared.models.Card
import com.baksha97.sdui.shared.models.Column
import com.baksha97.sdui.shared.models.LazyColumn
import com.baksha97.sdui.shared.models.Row
import com.baksha97.sdui.shared.models.TextStyle
import com.baksha97.sdui.shared.models.Token
import com.baksha97.sdui.shared.models.asyncImage
import com.baksha97.sdui.shared.models.button
import com.baksha97.sdui.shared.models.card
import com.baksha97.sdui.shared.models.column
import com.baksha97.sdui.shared.models.margin
import com.baksha97.sdui.shared.models.onClick
import com.baksha97.sdui.shared.models.padding
import com.baksha97.sdui.shared.models.row
import com.baksha97.sdui.shared.models.slider
import com.baksha97.sdui.shared.models.text
import com.baksha97.sdui.shared.models.textContent
import com.baksha97.sdui.shared.models.ui
import com.baksha97.sdui.shared.models.urlSource
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import java.io.File

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
private fun generateProfileCard(): Token = ui {
    Card("profile_card") {
        padding {
            all = 16
        }

        column {
            asyncImage("avatar_image") {
                urlSource = "https://picsum.photos/56"
                widthDp = 56
                heightDp = 56
            }

            text("name_text") {
                textContent = "John Doe"
                style = TextStyle.HeadlineMedium
            }

            text("followers_text") {
                textContent = "1234 followers"
                style = TextStyle.BodyMedium
            }
        }
    }
}

/**
 * Generates an article card component.
 */
private fun generateArticleCard(): Token = ui {
    Card("article_card") {
        padding {
            all = 16
        }

        column("article_column") {
            asyncImage("article_image") {
                urlSource = "https://picsum.photos/400/250"
                widthDp = 400
                heightDp = 250
            }

            text("article_title") {
                textContent = "Server-driven UI cuts release time"
                style = TextStyle.HeadlineMedium
                margin {
                    top = 8
                }
            }
        }
    }
}

/**
 * Generates a composed component with multiple cards in a row.
 * This demonstrates composing tokens.
 */
private fun generateComposedCards(): Token = ui {
        Row("composed_cards") {
            padding {
                all = 16
            }

            // Add existing tokens directly to the row
            val profileCard = generateProfileCard()
            val articleCard = generateArticleCard()
            children.add(profileCard)
            children.add(articleCard)
        }
    }

/**
 * Generates a dashboard component that demonstrates more complex composition.
 * This shows how to compose multiple components into a single UI.
 */
private fun generateDashboard(): Token = ui {
    Column("dashboard") {
        // Header section
        text("dashboard_header") {
            textContent = "Dashboard"
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
            val enhancedCard = generateEnhancedCard()
            val sliderCard = generateSlider()
            children.add(enhancedCard)
            children.add(sliderCard)
        }

        val formCard = generateForm()
        val lazyList = generateLazyList()
        children.add(formCard)
        children.add(lazyList)
    }
}

/**
 * Generates an enhanced card component.
 */
private fun generateEnhancedCard(): Token = ui {
    Card("enhanced_card") {
        padding {
            all = 16
        }

        column("enhanced_column") {
            text("enhanced_title") {
                textContent = "Enhanced Card Example"
                style = TextStyle.HeadlineMedium
            }

            text("enhanced_description") {
                textContent = "This card demonstrates the new CardToken with styling and a button."
                style = TextStyle.BodyMedium
                margin {
                    top = 8
                }
            }

            button("enhanced_button") {
                textContent = "Learn More"
                style = ButtonStyle.Filled
                margin {
                    top = 16
                }
                onClick {
                    type = ActionType.Navigate
                    data("url" to "https://example.com")
                }
            }
        }
    }
}

/**
 * Generates a form component.
 */
private fun generateForm(): Token = ui {
    Card("form_card") {
        padding {
            all = 16
        }

        column("form_column") {
            text("form_title") {
                textContent = "Contact Form"
                style = TextStyle.HeadlineMedium
            }

            text("form_description") {
                textContent = "Fill out this form to get in touch with us."
                style = TextStyle.BodyMedium
                margin {
                    top = 8
                }
            }

            button("form_submit") {
                textContent = "Submit"
                style = ButtonStyle.Filled
                margin {
                    top = 16
                }
                onClick {
                    type = ActionType.Custom
                    data("action" to "submit_form")
                }
            }
        }
    }
}

/**
 * Generates a slider component.
 */
private fun generateSlider(): Token = ui {
    Card("slider_card") {
        padding {
            all = 16
        }

        column("slider_column") {
            text("slider_title") {
                textContent = "Local State Management"
                style = TextStyle.HeadlineMedium
            }

            text("slider_description") {
                textContent = "This slider demonstrates local state management."
                style = TextStyle.BodyMedium
                margin {
                    top = 8
                }
            }

            slider("demo_slider") {
                initialValue = 0.5f
                margin {
                    top = 16
                }
            }
        }
    }
}

/**
 * Generates a lazy list component.
 */
private fun generateLazyList(): Token = ui {
    LazyColumn("lazy_list") {
        padding {
            all = 16
        }

        // Add some sample items
        for (i in 1..5) {
            card("list_item_$i") {
                margin {
                    bottom = 8
                }
                padding {
                    all = 12
                }

                text("item_text_$i") {
                    textContent = "List Item $i"
                    style = TextStyle.BodyMedium
                }
            }
        }
    }
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

    println("Error: The 'render' command is temporarily disabled.")
    println("The DSL is still being iterated on. When finalized, the render functionality will be reimplemented.")
    println("Input file: $inputFile")
    println("Output file: $outputFile")
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
