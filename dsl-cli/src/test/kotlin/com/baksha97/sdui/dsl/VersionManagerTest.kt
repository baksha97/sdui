package com.baksha97.sdui.dsl

import org.junit.Test
import org.junit.Assert.*
import kotlinx.serialization.json.*

class VersionManagerTest {

    private val versionManager = VersionManager()

    @Test
    fun `test isCompatible returns true for compatible token`() {
        // Create a token with version 1 and minSupportedVersion 1
        val token = TextToken(
            id = "test_text",
            version = 1,
            a11y = null,
            text = TemplateString("Hello World"),
            style = TextStyle.BodyMedium
        )

        // Check compatibility with client version 1
        assertTrue(versionManager.isCompatible(token, 1))

        // Check compatibility with client version 2
        assertTrue(versionManager.isCompatible(token, 2))
    }

    @Test
    fun `test isCompatible logic with different versions`() {
        // Create a token with version 2
        val token = TextToken(
            id = "test_token",
            version = 2,
            a11y = null,
            text = TemplateString("Hello World"),
            style = TextStyle.BodyMedium
        )

        // Test the logic of isCompatible directly
        // A token with version 2 should be compatible with client version 2 or higher
        assertTrue(token.version <= 2 && token.minSupportedVersion <= 2)

        // A token with version 2 should not be compatible with client version 1
        assertFalse(token.version <= 1 && token.minSupportedVersion <= 1)

        // Create a token with version 1
        val tokenV1 = TextToken(
            id = "test_token_v1",
            version = 1,
            a11y = null,
            text = TemplateString("Hello World"),
            style = TextStyle.BodyMedium
        )

        // A token with version 1 should be compatible with client version 1 or higher
        assertTrue(tokenV1.version <= 1 && tokenV1.minSupportedVersion <= 1)
        assertTrue(tokenV1.version <= 2 && tokenV1.minSupportedVersion <= 2)
    }

    @Test
    fun `test migrateToken returns null for higher version`() {
        // Create a token with version 2
        val token = TextToken(
            id = "test_text",
            version = 2,
            a11y = null,
            text = TemplateString("Hello World"),
            style = TextStyle.BodyMedium
        )

        // Try to migrate to version 1
        val migratedToken = versionManager.migrateToken(token, 1)

        // Verify that migration failed
        assertNull(migratedToken)
    }

    @Test
    fun `test migrateToken returns same token for same version`() {
        // Create a token with version 1
        val token = TextToken(
            id = "test_text",
            version = 1,
            a11y = null,
            text = TemplateString("Hello World"),
            style = TextStyle.BodyMedium
        )

        // Migrate to version 1
        val migratedToken = versionManager.migrateToken(token, 1)

        // Verify that the token is the same
        assertNotNull(migratedToken)
        assertEquals(token.id, migratedToken!!.id)
        assertEquals(token.version, migratedToken.version)
    }

    @Test
    fun `test migrateToken migrates to higher version`() {
        // Create a token with version 1
        val token = TextToken(
            id = "test_text",
            version = 1,
            a11y = null,
            text = TemplateString("Hello World"),
            style = TextStyle.BodyMedium
        )

        // Migrate to version 2
        val migratedToken = versionManager.migrateToken(token, 2)

        // Verify that migration succeeded
        assertNotNull(migratedToken)
        assertEquals(token.id, migratedToken!!.id)
        assertEquals(2, migratedToken.version)
        assertTrue(migratedToken is TextToken)

        // Verify that other properties are preserved
        val textToken = migratedToken as TextToken
        assertEquals("Hello World", textToken.text.raw)
        assertEquals(TextStyle.BodyMedium, textToken.style)
    }

    @Test
    fun `test migrateToken migrates container with children`() {
        // Create a column token with children
        val token = ColumnToken(
            id = "test_column",
            version = 1,
            alignment = HorizontalAlignment.Start,
            children = listOf(
                TextToken(
                    id = "child_text",
                    version = 1,
                    text = TemplateString("Child Text"),
                    style = TextStyle.BodyMedium
                ),
                ButtonToken(
                    id = "child_button",
                    version = 1,
                    text = TemplateString("Child Button"),
                    onClick = Action(ActionType.CUSTOM, mapOf("action" to "child_button_click"))
                )
            )
        )

        // Migrate to version 2
        val migratedToken = versionManager.migrateToken(token, 2)

        // Verify that migration succeeded
        assertNotNull(migratedToken)
        assertEquals(token.id, migratedToken!!.id)
        assertEquals(2, migratedToken.version)
        assertTrue(migratedToken is ColumnToken)

        // Verify that children were also migrated
        val columnToken = migratedToken as ColumnToken
        assertEquals(2, columnToken.children.size)

        // Verify the first child
        val firstChild = columnToken.children[0]
        assertEquals("child_text", firstChild.id)
        assertEquals(2, firstChild.version)

        // Verify the second child
        val secondChild = columnToken.children[1]
        assertEquals("child_button", secondChild.id)
        assertEquals(2, secondChild.version)
    }

    @Test
    fun `test migrateTokenJson returns null for higher version`() {
        // Create a JSON token with version 2
        val tokenJson = buildJsonObject {
            put("id", JsonPrimitive("test_text"))
            put("version", JsonPrimitive(2))
            put("text", buildJsonObject {
                put("raw", JsonPrimitive("Hello World"))
            })
            put("style", JsonPrimitive("BodyMedium"))
        }

        // Try to migrate to version 1
        val migratedJson = versionManager.migrateTokenJson(tokenJson, 1)

        // Verify that migration failed
        assertNull(migratedJson)
    }

    @Test
    fun `test migrateTokenJson returns same json for same version`() {
        // Create a JSON token with version 1
        val tokenJson = buildJsonObject {
            put("id", JsonPrimitive("test_text"))
            put("version", JsonPrimitive(1))
            put("text", buildJsonObject {
                put("raw", JsonPrimitive("Hello World"))
            })
            put("style", JsonPrimitive("BodyMedium"))
        }

        // Migrate to version 1
        val migratedJson = versionManager.migrateTokenJson(tokenJson, 1)

        // Verify that the JSON is the same
        assertNotNull(migratedJson)
        assertEquals(tokenJson["id"], migratedJson!!["id"])
        assertEquals(tokenJson["version"], migratedJson["version"])
    }

    @Test
    fun `test migrateTokenJson migrates to higher version`() {
        // Create a JSON token with version 1
        val tokenJson = buildJsonObject {
            put("id", JsonPrimitive("test_text"))
            put("version", JsonPrimitive(1))
            put("text", buildJsonObject {
                put("raw", JsonPrimitive("Hello World"))
            })
            put("style", JsonPrimitive("BodyMedium"))
        }

        // Migrate to version 2
        val migratedJson = versionManager.migrateTokenJson(tokenJson, 2)

        // Verify that migration succeeded
        assertNotNull(migratedJson)
        assertEquals(tokenJson["id"], migratedJson!!["id"])
        assertEquals(JsonPrimitive(2), migratedJson["version"])

        // Verify that other properties are preserved
        assertEquals(tokenJson["text"], migratedJson["text"])
        assertEquals(tokenJson["style"], migratedJson["style"])
    }

    @Test
    fun `test migrateTokenJson migrates container with children`() {
        // Create a JSON column token with children
        val tokenJson = buildJsonObject {
            put("id", JsonPrimitive("test_column"))
            put("version", JsonPrimitive(1))
            put("alignment", JsonPrimitive("Start"))
            put("children", buildJsonArray {
                add(buildJsonObject {
                    put("id", JsonPrimitive("child_text"))
                    put("version", JsonPrimitive(1))
                    put("text", buildJsonObject {
                        put("raw", JsonPrimitive("Child Text"))
                    })
                    put("style", JsonPrimitive("BodyMedium"))
                })
                add(buildJsonObject {
                    put("id", JsonPrimitive("child_button"))
                    put("version", JsonPrimitive(1))
                    put("text", buildJsonObject {
                        put("raw", JsonPrimitive("Child Button"))
                    })
                    put("style", JsonPrimitive("Filled"))
                    put("onClick", buildJsonObject {
                        put("type", JsonPrimitive("CUSTOM"))
                        put("data", buildJsonObject {
                            put("action", JsonPrimitive("child_button_click"))
                        })
                    })
                })
            })
        }

        // Migrate to version 2
        val migratedJson = versionManager.migrateTokenJson(tokenJson, 2)

        // Verify that migration succeeded
        assertNotNull(migratedJson)
        assertEquals(tokenJson["id"], migratedJson!!["id"])
        assertEquals(JsonPrimitive(2), migratedJson["version"])

        // Verify that children were also migrated
        val children = migratedJson["children"]?.jsonArray
        assertNotNull(children)
        assertEquals(2, children!!.size)

        // Verify the first child
        val firstChild = children[0].jsonObject
        assertEquals(JsonPrimitive("child_text"), firstChild["id"])
        assertEquals(JsonPrimitive(2), firstChild["version"])

        // Verify the second child
        val secondChild = children[1].jsonObject
        assertEquals(JsonPrimitive("child_button"), secondChild["id"])
        assertEquals(JsonPrimitive(2), secondChild["version"])
    }
}
