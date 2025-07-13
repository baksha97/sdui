package com.baksha97.sdui.dsl

import org.junit.Test
import org.junit.Assert.*
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.io.File
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class MainTest {
    
    private val originalOut = System.out
    private val outContent = ByteArrayOutputStream()
    
    private fun setupStreams() {
        System.setOut(PrintStream(outContent))
    }
    
    private fun restoreStreams() {
        System.setOut(originalOut)
        outContent.reset()
    }
    
    @Test
    fun `test main with no arguments prints usage`() {
        setupStreams()
        try {
            main(arrayOf())
            val output = outContent.toString()
            assertTrue(output.contains("Usage: dsl-cli <command> [arguments]"))
            assertTrue(output.contains("Commands:"))
            assertTrue(output.contains("generate <component-type> <output-file>"))
            assertTrue(output.contains("help"))
        } finally {
            restoreStreams()
        }
    }
    
    @Test
    fun `test main with help command prints usage`() {
        setupStreams()
        try {
            main(arrayOf("help"))
            val output = outContent.toString()
            assertTrue(output.contains("Usage: dsl-cli <command> [arguments]"))
            assertTrue(output.contains("Commands:"))
            assertTrue(output.contains("generate <component-type> <output-file>"))
            assertTrue(output.contains("help"))
        } finally {
            restoreStreams()
        }
    }
    
    @Test
    fun `test main with unknown command prints error and usage`() {
        setupStreams()
        try {
            main(arrayOf("unknown"))
            val output = outContent.toString()
            assertTrue(output.contains("Unknown command: unknown"))
            assertTrue(output.contains("Usage: dsl-cli <command> [arguments]"))
        } finally {
            restoreStreams()
        }
    }
    
    @Test
    fun `test generate command with insufficient arguments prints error`() {
        setupStreams()
        try {
            main(arrayOf("generate", "profile-card"))
            val output = outContent.toString()
            assertTrue(output.contains("Error: The 'generate' command requires a component type and an output file."))
            assertTrue(output.contains("Usage: generate <component-type> <output-file>"))
        } finally {
            restoreStreams()
        }
    }
    
    @Test
    fun `test generate command with invalid component type throws exception`() {
        setupStreams()
        try {
            main(arrayOf("generate", "invalid-type", "output.json"))
            val output = outContent.toString()
            assertTrue(output.contains("Error generating JSON: Unknown component type: invalid-type"))
        } finally {
            restoreStreams()
        }
    }
    
    @Test
    fun `test generate profile-card creates valid JSON`() {
        val tempFile = File.createTempFile("profile-card", ".json")
        tempFile.deleteOnExit()
        
        main(arrayOf("generate", "profile-card", tempFile.absolutePath))
        
        val json = tempFile.readText()
        val jsonObject = Json.decodeFromString<JsonObject>(json)
        
        // Verify the JSON structure
        assertTrue(jsonObject.containsKey("id"))
        assertTrue(jsonObject.containsKey("version"))
        assertTrue(jsonObject.containsKey("children"))
        
        // Verify specific profile card properties
        val jsonStr = json.lowercase()
        assertTrue(jsonStr.contains("john doe"))
        assertTrue(jsonStr.contains("1234"))
        assertTrue(jsonStr.contains("https://picsum.photos/56"))
    }
    
    @Test
    fun `test generate article-card creates valid JSON`() {
        val tempFile = File.createTempFile("article-card", ".json")
        tempFile.deleteOnExit()
        
        main(arrayOf("generate", "article-card", tempFile.absolutePath))
        
        val json = tempFile.readText()
        val jsonObject = Json.decodeFromString<JsonObject>(json)
        
        // Verify the JSON structure
        assertTrue(jsonObject.containsKey("id"))
        assertTrue(jsonObject.containsKey("version"))
        assertTrue(jsonObject.containsKey("children"))
        
        // Verify specific article card properties
        val jsonStr = json.lowercase()
        assertTrue(jsonStr.contains("server-driven ui cuts release time"))
        assertTrue(jsonStr.contains("https://picsum.photos/400/250"))
    }
    
    @Test
    fun `test generate enhanced-card creates valid JSON`() {
        val tempFile = File.createTempFile("enhanced-card", ".json")
        tempFile.deleteOnExit()
        
        main(arrayOf("generate", "enhanced-card", tempFile.absolutePath))
        
        val json = tempFile.readText()
        val jsonObject = Json.decodeFromString<JsonObject>(json)
        
        // Verify the JSON structure
        assertTrue(jsonObject.containsKey("id"))
        assertTrue(jsonObject.containsKey("version"))
        assertTrue(jsonObject.containsKey("children"))
        
        // Verify specific enhanced card properties
        val jsonStr = json.lowercase()
        assertTrue(jsonStr.contains("enhanced card example"))
        assertTrue(jsonStr.contains("this card demonstrates the new cardtoken with styling and a button"))
        assertTrue(jsonStr.contains("learn more"))
    }
    
    @Test
    fun `test generate form creates valid JSON`() {
        val tempFile = File.createTempFile("form", ".json")
        tempFile.deleteOnExit()
        
        main(arrayOf("generate", "form", tempFile.absolutePath))
        
        val json = tempFile.readText()
        val jsonObject = Json.decodeFromString<JsonObject>(json)
        
        // Verify the JSON structure
        assertTrue(jsonObject.containsKey("id"))
        assertTrue(jsonObject.containsKey("version"))
        assertTrue(jsonObject.containsKey("children"))
        
        // Verify specific form properties
        val jsonStr = json.lowercase()
        assertTrue(jsonStr.contains("contact form"))
        assertTrue(jsonStr.contains("fill out this form to get in touch with us"))
    }
    
    @Test
    fun `test generate slider creates valid JSON`() {
        val tempFile = File.createTempFile("slider", ".json")
        tempFile.deleteOnExit()
        
        main(arrayOf("generate", "slider", tempFile.absolutePath))
        
        val json = tempFile.readText()
        val jsonObject = Json.decodeFromString<JsonObject>(json)
        
        // Verify the JSON structure
        assertTrue(jsonObject.containsKey("id"))
        assertTrue(jsonObject.containsKey("version"))
        assertTrue(jsonObject.containsKey("children"))
        
        // Verify specific slider properties
        val jsonStr = json.lowercase()
        assertTrue(jsonStr.contains("local state management"))
        assertTrue(jsonStr.contains("this slider demonstrates local state management"))
    }
    
    @Test
    fun `test generate lazy-list creates valid JSON`() {
        val tempFile = File.createTempFile("lazy-list", ".json")
        tempFile.deleteOnExit()
        
        main(arrayOf("generate", "lazy-list", tempFile.absolutePath))
        
        val json = tempFile.readText()
        val jsonObject = Json.decodeFromString<JsonObject>(json)
        
        // Verify the JSON structure
        assertTrue(jsonObject.containsKey("id"))
        assertTrue(jsonObject.containsKey("version"))
        assertTrue(jsonObject.containsKey("children"))
        
        // Verify that the lazy list has the expected number of items
        val jsonStr = json.lowercase()
        assertTrue(jsonStr.contains("item 1"))
        assertTrue(jsonStr.contains("item 5"))
    }
}