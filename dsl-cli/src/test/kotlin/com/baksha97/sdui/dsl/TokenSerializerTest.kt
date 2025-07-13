package com.baksha97.sdui.dsl

import org.junit.Test
import org.junit.Assert.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonArray

class TokenSerializerTest {

    // Use the tokenJson instance from TokenSerializer.kt which has the necessary serializers module
    private val json = tokenJson

    @Test
    fun `test serialization of TextToken`() {
        val token = text {
            text("Hello World")
            style = TextStyle.HeadlineMedium
        }

        val jsonString = json.encodeToString(token)
        val jsonObject = Json.decodeFromString<JsonObject>(jsonString)

        // Verify the JSON structure
        assertTrue(jsonObject.containsKey("id"))
        assertTrue(jsonObject.containsKey("version"))
        assertTrue(jsonObject.containsKey("text"))
        assertTrue(jsonObject.containsKey("style"))

        // Verify values
        assertEquals("Hello World", jsonObject["text"]?.jsonObject?.get("raw")?.toString()?.trim('"'))
        assertEquals("HeadlineMedium", jsonObject["style"]?.toString()?.trim('"'))
    }

    @Test
    fun `test serialization of ButtonToken`() {
        val token = button {
            text("Click Me")
            style = ButtonStyle.Filled
            onClick {
                type = ActionType.CUSTOM
                data("key" to "value")
            }
        }

        val jsonString = json.encodeToString(token)
        val jsonObject = Json.decodeFromString<JsonObject>(jsonString)

        // Verify the JSON structure
        assertTrue(jsonObject.containsKey("id"))
        assertTrue(jsonObject.containsKey("version"))
        assertTrue(jsonObject.containsKey("text"))
        assertTrue(jsonObject.containsKey("style"))
        assertTrue(jsonObject.containsKey("onClick"))

        // Verify values
        assertEquals("Click Me", jsonObject["text"]?.jsonObject?.get("raw")?.toString()?.trim('"'))
        assertEquals("Filled", jsonObject["style"]?.toString()?.trim('"'))
        assertEquals("CUSTOM", jsonObject["onClick"]?.jsonObject?.get("type")?.toString()?.trim('"'))
        val data = jsonObject["onClick"]?.jsonObject?.get("data")?.jsonObject
        assertNotNull(data)
        assertEquals("value", data?.get("key")?.toString()?.trim('"'))
    }

    @Test
    fun `test serialization of ColumnToken with children`() {
        val token = column {
            text {
                text("Title")
                style = TextStyle.HeadlineLarge
            }
            text {
                text("Description")
                style = TextStyle.BodyMedium
            }
        }

        val jsonString = json.encodeToString(token)
        val jsonObject = Json.decodeFromString<JsonObject>(jsonString)

        // Verify the JSON structure
        assertTrue(jsonObject.containsKey("id"))
        assertTrue(jsonObject.containsKey("version"))
        assertTrue(jsonObject.containsKey("alignment"))
        assertTrue(jsonObject.containsKey("children"))

        // Verify children
        val children = jsonObject["children"]?.jsonArray
        assertNotNull(children)
        assertEquals(2, children?.size)
    }

    @Test
    fun `test serialization of CardToken with onClick and children`() {
        val token = card {
            elevation = 4
            shape = CardShape.ROUNDED16
            onClick {
                type = ActionType.NAVIGATE
                data("screen" to "details")
            }
            column {
                text {
                    text("Card Title")
                }
            }
        }

        val jsonString = json.encodeToString(token)
        val jsonObject = Json.decodeFromString<JsonObject>(jsonString)

        // Verify the JSON structure
        assertTrue(jsonObject.containsKey("id"))
        assertTrue(jsonObject.containsKey("version"))
        assertTrue(jsonObject.containsKey("elevation"))
        assertTrue(jsonObject.containsKey("shape"))
        assertTrue(jsonObject.containsKey("onClick"))
        assertTrue(jsonObject.containsKey("children"))

        // Verify values
        assertEquals(4, jsonObject["elevation"]?.toString()?.toInt())
        assertEquals("ROUNDED16", jsonObject["shape"]?.toString()?.trim('"'))
        assertEquals("NAVIGATE", jsonObject["onClick"]?.jsonObject?.get("type")?.toString()?.trim('"'))

        // Verify children
        val children = jsonObject["children"]?.jsonArray
        assertNotNull(children)
        assertEquals(1, children?.size)
    }

    @Test
    fun `test serialization of complex nested structure`() {
        val token = column {
            card {
                column {
                    text {
                        text("Card Title")
                    }
                    row {
                        text {
                            text("Left")
                        }
                        spacer {
                            width = 16
                        }
                        text {
                            text("Right")
                        }
                    }
                    button {
                        text("Action")
                    }
                }
            }
        }

        val jsonString = json.encodeToString(token)
        val jsonObject = Json.decodeFromString<JsonObject>(jsonString)

        // Verify the JSON structure has the expected depth
        assertTrue(jsonObject.containsKey("children"))
        val cardToken = jsonObject["children"]?.jsonArray?.get(0)?.jsonObject
        assertNotNull(cardToken)

        assertTrue(cardToken?.containsKey("children") ?: false)
        val columnToken = cardToken?.get("children")?.jsonArray?.get(0)?.jsonObject
        assertNotNull(columnToken)

        assertTrue(columnToken?.containsKey("children") ?: false)
        val children = columnToken?.get("children")?.jsonArray
        assertNotNull(children)
        assertEquals(3, children?.size)
    }

    @Test
    fun `test serialization of token with all properties set`() {
        val token = text {
            text("Complete Text")
            style = TextStyle.BodyLarge
            color {
                red = 255
                green = 0
                blue = 0
                alpha = 128
            }
            maxLines = 2
            overflow = TextOverflowValue.Ellipsis
            textAlign = TextAlignValue.Center
            margin {
                all = 16
            }
            a11y {
                role = Role.TEXT_FIELD
                label("Accessible Text")
                liveRegion = LiveRegion.POLITE
                isEnabled = true
                isFocusable = true
            }
        }

        val jsonString = json.encodeToString(token)
        val jsonObject = Json.decodeFromString<JsonObject>(jsonString)

        // Verify all properties are serialized
        assertTrue(jsonObject.containsKey("id"))
        assertTrue(jsonObject.containsKey("version"))
        assertTrue(jsonObject.containsKey("text"))
        assertTrue(jsonObject.containsKey("style"))
        assertTrue(jsonObject.containsKey("color"))
        assertTrue(jsonObject.containsKey("maxLines"))
        assertTrue(jsonObject.containsKey("overflow"))
        assertTrue(jsonObject.containsKey("textAlign"))
        assertTrue(jsonObject.containsKey("margin"))
        assertTrue(jsonObject.containsKey("a11y"))

        // Verify nested properties
        val color = jsonObject["color"]?.jsonObject
        assertNotNull(color)
        assertEquals(255, color?.get("red")?.toString()?.toInt())
        assertEquals(0, color?.get("green")?.toString()?.toInt())
        assertEquals(0, color?.get("blue")?.toString()?.toInt())
        assertEquals(128, color?.get("alpha")?.toString()?.toInt())

        val margin = jsonObject["margin"]?.jsonObject
        assertNotNull(margin)
        assertEquals(16, margin?.get("all")?.toString()?.toInt())

        val a11y = jsonObject["a11y"]?.jsonObject
        assertNotNull(a11y)
        assertEquals("TEXT_FIELD", a11y?.get("role")?.toString()?.trim('"'))
        assertEquals("Accessible Text", a11y?.get("label")?.jsonObject?.get("raw")?.toString()?.trim('"'))
        assertEquals("POLITE", a11y?.get("liveRegion")?.toString()?.trim('"'))
        assertEquals("true", a11y?.get("isEnabled")?.toString())
        assertEquals("true", a11y?.get("isFocusable")?.toString())
    }
}
