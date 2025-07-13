package com.baksha97.sdui

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.assertFailsWith
import kotlin.test.assertEquals
import com.baksha97.sdui.shared.models.TokenRegistry as SharedTokenRegistry
import com.baksha97.sdui.shared.models.ScreenPayload as SharedScreenPayload
import com.baksha97.sdui.shared.models.TokenRef as SharedTokenRef
import com.baksha97.sdui.shared.models.CardToken as SharedCardToken
import com.baksha97.sdui.shared.models.TextToken as SharedTextToken
import com.baksha97.sdui.shared.models.TemplateString

class ComponentRegistrationGuaranteeTest {

    @Test
    fun testAutomaticValidationOnInitialization() {
        // This should succeed because all tokens are properly registered
        val specProvider = LocalSpecProviderImpl()

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
    fun testRegistrationStatsAreAvailable() {
        val specProvider = LocalSpecProviderImpl()
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
        assertTrue(tokenIds.contains("form_example"), "Should contain form_example token")
    }

    @Test
    fun testValidationWithMissingToken() {
        // Create a registry with a missing token reference
        val registry = SharedTokenRegistry()

        // Register a card that references a missing child token
        registry.register(SharedCardToken(
            id = "test_card",
            version = 1,
            children = listOf(
                SharedTextToken(
                    id = "missing_text",
                    version = 1,
                    text = TemplateString("Test")
                )
            )
        ))

        // The missing_text token is not registered separately, so validation should fail
        val errors = registry.validateRegistry()
        assertTrue(errors.isNotEmpty(), "Should detect missing child token")
        assertTrue(errors.any { it.contains("missing_text") }, "Should mention the missing token")
    }

    @Test
    fun testScreenPayloadValidation() {
        val registry = SharedTokenRegistry()

        // Register some tokens
        registry.register(SharedTextToken(
            id = "valid_text",
            version = 1,
            text = TemplateString("Valid")
        ))

        // Create a screen that references both valid and invalid tokens
        val screenPayload = SharedScreenPayload(
            id = "test_screen",
            tokens = listOf(
                SharedTokenRef(id = "valid_text"),
                SharedTokenRef(id = "invalid_token")
            )
        )

        val missingTokens = registry.validateScreenPayload(screenPayload)
        assertEquals(1, missingTokens.size, "Should find one missing token")
        assertTrue(missingTokens.contains("invalid_token"), "Should identify the missing token")
    }

    @Test
    fun testRegisterWithValidation() {
        val registry = SharedTokenRegistry()

        // Valid token should register successfully
        val validToken = SharedTextToken(
            id = "valid_token",
            version = 1,
            text = TemplateString("Valid")
        )
        registry.registerWithValidation(validToken)
        assertTrue(registry.hasToken("valid_token"), "Valid token should be registered")

        // Invalid token (empty ID) should throw exception
        val invalidToken = SharedTextToken(
            id = "",
            version = 1,
            text = TemplateString("Invalid")
        )
        assertFailsWith<IllegalArgumentException> {
            registry.registerWithValidation(invalidToken)
        }
    }

    @Test
    fun testAllScreensHaveValidTokenReferences() {
        val specProvider = LocalSpecProviderImpl()

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
    fun testRegistryContainsAllExpectedTokens() {
        val specProvider = LocalSpecProviderImpl()
        val registry = specProvider.sharedRegistry

        // Verify all expected tokens are registered
        val expectedTokens = listOf("enhanced_card", "slider_example", "form_example")
        expectedTokens.forEach { tokenId ->
            assertTrue(registry.hasToken(tokenId), "Registry should contain token: $tokenId")
            assertNotNull(registry.getToken(tokenId), "Token should be retrievable: $tokenId")
        }

        // Verify child tokens are also registered
        val enhancedCard = registry.getToken("enhanced_card") as? SharedCardToken
        assertNotNull(enhancedCard, "enhanced_card should be a CardToken")

        enhancedCard.children.forEach { child ->
            assertTrue(registry.hasToken(child.id), "Child token should be registered: ${child.id}")
        }
    }

    @Test
    fun testValidationReportStructure() {
        val specProvider = LocalSpecProviderImpl()
        val report = specProvider.getValidationReport()

        // Verify report structure
        assertTrue(report.containsKey("registryErrors"), "Report should contain registry errors")
        assertTrue(report.containsKey("screenValidation"), "Report should contain screen validation")
        assertTrue(report.containsKey("registrationStats"), "Report should contain registration stats")
        assertTrue(report.containsKey("isValid"), "Report should contain validity flag")

        // For a properly configured provider, everything should be valid
        assertTrue(report["isValid"] as Boolean, "Provider should be valid")
        assertTrue((report["registryErrors"] as List<*>).isEmpty(), "Should have no registry errors")

        val screenValidation = report["screenValidation"] as Map<*, *>
        screenValidation.values.forEach { errors ->
            assertTrue((errors as List<*>).isEmpty(), "All screens should be valid")
        }
    }
}
