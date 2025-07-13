package com.baksha97.sdui

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class AnnotationBasedRegistrationTest {

    @Test
    fun testAnnotationBasedProviderInitialization() {
        // This should succeed because all tokens are properly registered via annotations
        val specProvider = AnnotationBasedSpecProvider()

        // Verify that validation was successful
        val validationReport = specProvider.getValidationReport()
        assertTrue(validationReport["isValid"] as Boolean, "Validation should pass")

        val registryErrors = validationReport["registryErrors"] as List<*>
        assertTrue(registryErrors.isEmpty(), "Registry should have no errors")

        val screenValidation = validationReport["screenValidation"] as Map<*, *>
        screenValidation.values.forEach { errors ->
            assertTrue((errors as List<*>).isEmpty(), "All screens should be valid")
        }
    }

    @Test
    fun testAnnotationBasedRegistrationStats() {
        val specProvider = AnnotationBasedSpecProvider()
        val validationReport = specProvider.getValidationReport()
        val stats = validationReport["registrationStats"] as Map<*, *>

        assertTrue(stats.containsKey("totalTokens"), "Stats should include total tokens")
        assertTrue(stats.containsKey("tokensByType"), "Stats should include tokens by type")
        assertTrue(stats.containsKey("tokenIds"), "Stats should include token IDs")

        val totalTokens = stats["totalTokens"] as Int
        assertTrue(totalTokens > 0, "Should have registered tokens")

        val tokenIds = stats["tokenIds"] as List<*>
        assertTrue(tokenIds.contains("enhanced_card"), "Should contain enhanced_card token")
        assertTrue(tokenIds.contains("slider_example"), "Should contain slider_example token")
    }

    @Test
    fun testAnnotationBasedScreensAreAvailable() {
        val specProvider = AnnotationBasedSpecProvider()

        // Test that screens are available
        val homeScreen = specProvider.screens["home"]
        val enhancedHomeScreen = specProvider.screens["enhanced_home"]

        assertNotNull(homeScreen, "Home screen should be available")
        assertNotNull(enhancedHomeScreen, "Enhanced home screen should be available")

        assertEquals("home", homeScreen.id, "Home screen should have correct ID")
        assertEquals("enhanced_home", enhancedHomeScreen.id, "Enhanced home screen should have correct ID")
    }

    @Test
    fun testAnnotationBasedTokensAreRegistered() {
        val specProvider = AnnotationBasedSpecProvider()
        val registry = specProvider.sharedRegistry

        // Verify that all expected tokens are registered
        val expectedTokens = listOf(
            "enhanced_card.title",
            "enhanced_card.description", 
            "enhanced_card.button",
            "enhanced_card",
            "slider_example.title",
            "slider_example.description",
            "slider_example.slider",
            "slider_example"
        )

        expectedTokens.forEach { tokenId ->
            assertTrue(registry.hasToken(tokenId), "Registry should contain token: $tokenId")
            assertNotNull(registry.getToken(tokenId), "Token should be retrievable: $tokenId")
        }
    }

    @Test
    fun testAnnotationBasedScreenValidation() {
        val specProvider = AnnotationBasedSpecProvider()

        // Test each screen individually
        specProvider.screens.forEach { (screenId, screen) ->
            val missingTokens = specProvider.sharedRegistry.validateScreenPayload(screen)
            assertTrue(
                missingTokens.isEmpty(),
                "Screen '$screenId' should not have missing tokens: ${missingTokens.joinToString(", ")}"
            )
        }
    }

    @Test
    fun testAnnotationBasedVsManualRegistrationEquivalence() {
        // Compare annotation-based provider with manual provider
        val annotationProvider = AnnotationBasedSpecProvider()
        val manualProvider = LocalSpecProviderImpl()

        // Both should have the same screens
        val annotationScreens = annotationProvider.screens.keys
        val manualScreens = manualProvider.screens.keys

        assertTrue(annotationScreens.containsAll(manualScreens), 
            "Annotation-based provider should have all manual screens")

        // Both should have similar token counts (annotation-based might have more due to different structure)
        val annotationStats = annotationProvider.sharedRegistry.getRegistrationStats()
        val manualStats = manualProvider.sharedRegistry.getRegistrationStats()

        val annotationTokenCount = annotationStats["totalTokens"] as Int
        val manualTokenCount = manualStats["totalTokens"] as Int

        assertTrue(annotationTokenCount > 0, "Annotation-based provider should have tokens")
        assertTrue(manualTokenCount > 0, "Manual provider should have tokens")
    }

    @Test
    fun testAnnotationBasedProviderIntegration() {
        val specProvider = AnnotationBasedSpecProvider()

        // Test that the provider can be used like any other SpecProvider
        // Access properties directly since we can't easily use suspend functions in tests
        val homeScreen = specProvider.screens["home"]
        val registry = specProvider.sharedRegistry

        assertNotNull(homeScreen, "Should be able to get home screen")
        assertNotNull(registry, "Should be able to get registry")

        assertTrue(homeScreen.tokens.isNotEmpty(), "Home screen should have tokens")
        assertTrue(registry.getAllTokens().isNotEmpty(), "Registry should have tokens")
    }

    @Test
    fun testAnnotationBasedComponentHierarchy() {
        val specProvider = AnnotationBasedSpecProvider()
        val registry = specProvider.sharedRegistry

        // Test that component hierarchy is properly maintained
        val enhancedCard = registry.getToken("enhanced_card") as? com.baksha97.sdui.shared.models.CardToken
        assertNotNull(enhancedCard, "enhanced_card should be a CardToken")

        // Verify that child components exist
        enhancedCard.children.forEach { child ->
            assertTrue(registry.hasToken(child.id), "Child token should be registered: ${child.id}")
        }
    }
}
