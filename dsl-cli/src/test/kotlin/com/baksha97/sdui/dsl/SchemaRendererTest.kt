package com.baksha97.sdui.dsl

import org.junit.Test
import org.junit.Assert.*
import kotlinx.serialization.json.*

class SchemaRendererTest {

    private val schemaRenderer = SchemaRenderer()
    private val schemaGenerator = SchemaGenerator()
    private val schema = schemaGenerator.generateTokenSchema()

    @Test
    fun `test renderToken with TextToken`() {
        // Create a JSON representation of a TextToken
        val textTokenJson = buildJsonObject {
            put("id", JsonPrimitive("test_text"))
            put("version", JsonPrimitive(1))
            put("text", buildJsonObject {
                put("raw", JsonPrimitive("Hello World"))
            })
            put("style", JsonPrimitive("BodyMedium"))
        }

        // Render the token
        val token = schemaRenderer.renderToken(textTokenJson, schema)

        // Verify the token is not null and is a TextToken
        assertNotNull(token)
        assertTrue(token is TextToken)

        // Verify the token properties
        val textToken = token as TextToken
        assertEquals("test_text", textToken.id)
        assertEquals(1, textToken.version)
        assertEquals("Hello World", textToken.text.raw)
        assertEquals(TextStyle.BodyMedium, textToken.style)
    }

    @Test
    fun `test renderToken with ButtonToken`() {
        // Create a JSON representation of a ButtonToken
        val buttonTokenJson = buildJsonObject {
            put("id", JsonPrimitive("test_button"))
            put("version", JsonPrimitive(1))
            put("text", buildJsonObject {
                put("raw", JsonPrimitive("Click Me"))
            })
            put("style", JsonPrimitive("Filled"))
            put("enabled", JsonPrimitive(true))
            put("onClick", buildJsonObject {
                put("type", JsonPrimitive("CUSTOM"))
                put("data", buildJsonObject {
                    put("action", JsonPrimitive("button_click"))
                })
            })
            // Add type property to help the renderer identify the token type
            put("type", JsonPrimitive("ButtonToken"))
        }

        // Render the token
        val token = schemaRenderer.renderToken(buttonTokenJson, schema)

        // Verify the token is not null and is a ButtonToken
        assertNotNull(token)
        assertTrue(token is ButtonToken)

        // Verify the token properties
        val buttonToken = token as ButtonToken
        assertEquals("test_button", buttonToken.id)
        assertEquals(1, buttonToken.version)
        assertEquals("Click Me", buttonToken.text.raw)
        assertEquals(ButtonStyle.Filled, buttonToken.style)
        assertTrue(buttonToken.enabled)
        assertNotNull(buttonToken.onClick)
        assertEquals(ActionType.CUSTOM, buttonToken.onClick.type)
        assertEquals("button_click", buttonToken.onClick.data["action"])
    }

    @Test
    fun `test renderToken with ColumnToken containing children`() {
        // Create a JSON representation of a ColumnToken with children
        val columnTokenJson = buildJsonObject {
            put("id", JsonPrimitive("test_column"))
            put("version", JsonPrimitive(1))
            put("alignment", JsonPrimitive("Start"))
            // Add type property to help the renderer identify the token type
            put("type", JsonPrimitive("ColumnToken"))
            put("children", buildJsonArray {
                add(buildJsonObject {
                    put("id", JsonPrimitive("child_text"))
                    put("version", JsonPrimitive(1))
                    put("text", buildJsonObject {
                        put("raw", JsonPrimitive("Child Text"))
                    })
                    put("style", JsonPrimitive("BodyMedium"))
                    // Add type property to help the renderer identify the token type
                    put("type", JsonPrimitive("TextToken"))
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
                    // Add type property to help the renderer identify the token type
                    put("type", JsonPrimitive("ButtonToken"))
                })
            })
        }

        // Render the token
        val token = schemaRenderer.renderToken(columnTokenJson, schema)

        // Verify the token is not null and is a ColumnToken
        assertNotNull(token)
        assertTrue(token is ColumnToken)

        // Verify the token properties
        val columnToken = token as ColumnToken
        assertEquals("test_column", columnToken.id)
        assertEquals(1, columnToken.version)
        assertEquals(HorizontalAlignment.Start, columnToken.alignment)

        // Verify the children
        assertEquals(2, columnToken.children.size)

        // Verify the first child
        val firstChild = columnToken.children[0]
        assertTrue(firstChild is TextToken)
        assertEquals("child_text", firstChild.id)
        assertEquals("Child Text", (firstChild as TextToken).text.raw)

        // Verify the second child
        val secondChild = columnToken.children[1]
        assertTrue(secondChild is ButtonToken)
        assertEquals("child_button", secondChild.id)
        assertEquals("Child Button", (secondChild as ButtonToken).text.raw)
        assertEquals("child_button_click", secondChild.onClick.data["action"])
    }

    @Test
    fun `test renderToken with complex nested structure`() {
        // Create a JSON representation of a complex nested structure
        val complexTokenJson = buildJsonObject {
            put("id", JsonPrimitive("test_card"))
            put("version", JsonPrimitive(1))
            put("elevation", JsonPrimitive(2))
            put("shape", JsonPrimitive("ROUNDED8"))
            // Add type property to help the renderer identify the token type
            put("type", JsonPrimitive("CardToken"))
            put("children", buildJsonArray {
                add(buildJsonObject {
                    put("id", JsonPrimitive("card_column"))
                    put("version", JsonPrimitive(1))
                    put("alignment", JsonPrimitive("Start"))
                    // Add type property to help the renderer identify the token type
                    put("type", JsonPrimitive("ColumnToken"))
                    put("children", buildJsonArray {
                        add(buildJsonObject {
                            put("id", JsonPrimitive("card_title"))
                            put("version", JsonPrimitive(1))
                            put("text", buildJsonObject {
                                put("raw", JsonPrimitive("Card Title"))
                            })
                            put("style", JsonPrimitive("HeadlineMedium"))
                            // Add type property to help the renderer identify the token type
                            put("type", JsonPrimitive("TextToken"))
                        })
                        add(buildJsonObject {
                            put("id", JsonPrimitive("card_description"))
                            put("version", JsonPrimitive(1))
                            put("text", buildJsonObject {
                                put("raw", JsonPrimitive("Card Description"))
                            })
                            put("style", JsonPrimitive("BodyMedium"))
                            // Add type property to help the renderer identify the token type
                            put("type", JsonPrimitive("TextToken"))
                        })
                    })
                })
            })
        }

        // Render the token
        val token = schemaRenderer.renderToken(complexTokenJson, schema)

        // Verify the token is not null and is a CardToken
        assertNotNull(token)
        assertTrue(token is CardToken)

        // Verify the token properties
        val cardToken = token as CardToken
        assertEquals("test_card", cardToken.id)
        assertEquals(1, cardToken.version)
        assertEquals(2, cardToken.elevation)
        assertEquals(CardShape.ROUNDED8, cardToken.shape)

        // Verify the children
        assertEquals(1, cardToken.children.size)

        // Verify the column child
        val columnChild = cardToken.children[0]
        assertTrue(columnChild is ColumnToken)
        assertEquals("card_column", columnChild.id)

        // Verify the column's children
        val columnToken = columnChild as ColumnToken
        assertEquals(2, columnToken.children.size)

        // Verify the title
        val titleToken = columnToken.children[0]
        assertTrue(titleToken is TextToken)
        assertEquals("card_title", titleToken.id)
        assertEquals("Card Title", (titleToken as TextToken).text.raw)
        assertEquals(TextStyle.HeadlineMedium, titleToken.style)

        // Verify the description
        val descriptionToken = columnToken.children[1]
        assertTrue(descriptionToken is TextToken)
        assertEquals("card_description", descriptionToken.id)
        assertEquals("Card Description", (descriptionToken as TextToken).text.raw)
        assertEquals(TextStyle.BodyMedium, descriptionToken.style)
    }
}
