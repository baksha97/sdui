package com.baksha97.sdui

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MissingTokensTest {

    @Test
    fun testMissingTokensAreNowAvailableInRegistry() {
        // Test the LocalSpecProviderImpl which now provides shared-models types directly
        val specProvider = LocalSpecProviderImpl()

        // Since we can't use coroutines in tests easily, we'll access the registry directly
        // The LocalSpecProviderImpl creates the registry in its constructor
        val sharedRegistry = specProvider.sharedRegistry

        // Test that the previously missing tokens are now available
        val enhancedCard = sharedRegistry.getToken("enhanced_card")
        val sliderExample = sharedRegistry.getToken("slider_example") 
        val formExample = sharedRegistry.getToken("form_example")

        // Assert that all tokens are found
        assertNotNull(enhancedCard, "enhanced_card token should be available")
        assertNotNull(sliderExample, "slider_example token should be available")
        assertNotNull(formExample, "form_example token should be available")

        // Verify token IDs match
        assertTrue(enhancedCard.id == "enhanced_card", "enhanced_card token should have correct ID")
        assertTrue(sliderExample.id == "slider_example", "slider_example token should have correct ID")
        assertTrue(formExample.id == "form_example", "form_example token should have correct ID")
    }

    @Test
    fun testRegistryContainsExpectedTokens() {
        // Test the LocalSpecProviderImpl which now provides shared-models types directly
        val specProvider = LocalSpecProviderImpl()
        val sharedRegistry = specProvider.sharedRegistry

        // Verify that the registry has tokens
        val allTokens = sharedRegistry.getAllTokens()
        assertTrue(allTokens.isNotEmpty(), "Registry should contain tokens")

        // Verify specific tokens exist
        assertTrue(sharedRegistry.hasToken("enhanced_card"), "Registry should contain enhanced_card")
        assertTrue(sharedRegistry.hasToken("slider_example"), "Registry should contain slider_example")
        assertTrue(sharedRegistry.hasToken("form_example"), "Registry should contain form_example")
    }

    @Test
    fun testScreenPayloadDirectAccess() {
        // Test screen payload direct access from LocalSpecProviderImpl
        val specProvider = LocalSpecProviderImpl()

        // Access the screens map directly since we can't use suspend functions in tests easily
        val sharedScreen = specProvider.screens["enhanced_home"]

        // Assert screen exists and has correct ID
        assertNotNull(sharedScreen, "Screen should exist")
        assertTrue(sharedScreen!!.id == "enhanced_home", "Screen should have correct ID")

        // Verify that the screen has tokens
        assertTrue(sharedScreen.tokens.isNotEmpty(), "Screen should have tokens")

        // Get the token IDs from the screen
        val tokenIds = sharedScreen.tokens.map { it.id }

        // Verify that the screen references expected tokens
        assertTrue(tokenIds.isNotEmpty(), "Screen should reference tokens")
        assertTrue(tokenIds.contains("enhanced_card"), "Screen should reference enhanced_card token")
        assertTrue(tokenIds.contains("slider_example"), "Screen should reference slider_example token")
        assertTrue(tokenIds.contains("form_example"), "Screen should reference form_example token")
    }
}
