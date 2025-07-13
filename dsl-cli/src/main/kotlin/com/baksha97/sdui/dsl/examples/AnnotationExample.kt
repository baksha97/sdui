package com.baksha97.sdui.dsl.examples

import com.baksha97.sdui.dsl.*
import com.baksha97.sdui.dsl.reflection.ReflectionSchemaGenerator
import com.baksha97.sdui.dsl.reflection.ReflectionVersionManager
import com.baksha97.sdui.dsl.reflection.TokenRegistry
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

/**
 * Example demonstrating how to use the annotation-based approach for schema generation and version management.
 * This example shows how to:
 * 1. Register annotated token types
 * 2. Generate a schema using reflection
 * 3. Migrate tokens using reflection
 */
object AnnotationExample {

    /**
     * Main function to run the example.
     */
    @JvmStatic
    fun main(args: Array<String>) {
        // Register annotated token types
        registerAnnotatedTokensWithRegistry()
        
        // Generate schema using reflection
        val schemaGenerator = ReflectionSchemaGenerator()
        val schema = schemaGenerator.generateTokenSchema()
        
        println("Generated schema:")
        println(schema.toString())
        
        // Create an example token
        val textToken = AnnotatedTextToken(
            id = "example_text",
            version = 1,
            text = TemplateString("Hello, World!"),
            style = TextStyle.BodySmall
        )
        
        // Migrate the token to version 2
        val versionManager = ReflectionVersionManager()
        val migratedToken = versionManager.migrateToken(textToken, 2)
        
        println("\nOriginal token:")
        println("ID: ${textToken.id}")
        println("Version: ${textToken.version}")
        println("Text: ${textToken.text.raw}")
        println("Style: ${textToken.style}")
        
        println("\nMigrated token:")
        if (migratedToken != null) {
            println("ID: ${migratedToken.id}")
            println("Version: ${migratedToken.version}")
            if (migratedToken is AnnotatedTextToken) {
                println("Text: ${migratedToken.text.raw}")
                println("Style: ${migratedToken.style}")
            }
        } else {
            println("Migration failed")
        }
        
        // Create a JSON example
        val jsonExample = buildJsonObject {
            put("id", JsonPrimitive("example_json"))
            put("version", JsonPrimitive(1))
            put("text", buildJsonObject {
                put("raw", JsonPrimitive("Hello from JSON!"))
            })
            put("style", JsonPrimitive("BodySmall"))
        }
        
        // Migrate the JSON to version 2
        val migratedJson = versionManager.migrateTokenJson(jsonExample, 2)
        
        println("\nOriginal JSON:")
        println(jsonExample.toString())
        
        println("\nMigrated JSON:")
        println(migratedJson.toString())
    }
    
    /**
     * Demonstrates how to use the annotated tokens in a DSL-like manner.
     */
    fun createExampleUI(): AnnotatedColumnToken {
        return AnnotatedColumnToken(
            id = "example_column",
            version = 1,
            children = listOf(
                AnnotatedTextToken(
                    id = "header",
                    version = 1,
                    text = TemplateString("Welcome to the Annotated UI"),
                    style = TextStyle.HeadlineMedium
                ),
                AnnotatedTextToken(
                    id = "description",
                    version = 1,
                    text = TemplateString("This UI is built using annotated token models"),
                    style = TextStyle.BodyMedium
                ),
                AnnotatedButtonToken(
                    id = "action_button",
                    version = 1,
                    text = TemplateString("Click Me"),
                    onClick = Action(ActionType.CUSTOM, mapOf("action" to "button_clicked"))
                )
            )
        )
    }
}

/**
 * This example demonstrates how annotations can be used to:
 * 
 * 1. Reduce boilerplate in token model definitions
 *    - Common properties like id, version, and a11y are annotated with @TokenProperty
 *    - Schema information is provided through annotations like @SchemaRef, @SchemaEnum, etc.
 * 
 * 2. Automate schema generation
 *    - The ReflectionSchemaGenerator uses annotations to generate JSON Schema
 *    - No need to manually define schema for each token type
 * 
 * 3. Simplify version management
 *    - The ReflectionVersionManager uses annotations to handle token migration
 *    - Migration strategies can be specified with @Migrateable
 *    - Custom migration logic can be implemented in companion objects
 * 
 * Benefits:
 * - Less code duplication
 * - More maintainable codebase
 * - Easier to add new token types
 * - Better documentation through annotations
 */