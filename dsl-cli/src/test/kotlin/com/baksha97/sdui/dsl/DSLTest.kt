package com.baksha97.sdui.dsl

import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

class DSLTest {

    @Before
    fun setup() {
        // Reset the ID generator before each test
        ComponentContext.reset()
    }

    @Test
    fun `test column builder creates valid column token`() {
        val token = column {
            text {
                text("Hello World")
                style = TextStyle.HeadlineMedium
            }
            button {
                text("Click Me")
                style = ButtonStyle.Filled
                onClick {
                    type = ActionType.CUSTOM
                    data("key" to "value")
                }
            }
        }

        // Verify token properties
        assertEquals("column", token.id.split("-")[0])
        assertEquals(1, token.version)
        assertEquals(HorizontalAlignment.Start, token.alignment)
        assertEquals(2, token.children.size)

        // Verify children
        val textToken = token.children[0] as TextToken
        assertEquals("text", textToken.id.split("-")[0])
        assertEquals("Hello World", textToken.text.raw)
        assertEquals(TextStyle.HeadlineMedium, textToken.style)

        val buttonToken = token.children[1] as ButtonToken
        assertEquals("button", buttonToken.id.split("-")[0])
        assertEquals("Click Me", buttonToken.text.raw)
        assertEquals(ButtonStyle.Filled, buttonToken.style)
        assertEquals(ActionType.CUSTOM, buttonToken.onClick.type)
        assertEquals("value", buttonToken.onClick.data["key"])
    }

    @Test
    fun `test row builder creates valid row token`() {
        val token = row {
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

        // Verify token properties
        assertEquals("row", token.id.split("-")[0])
        assertEquals(1, token.version)
        assertEquals(VerticalAlignment.CenterVertically, token.alignment)
        assertEquals(3, token.children.size)

        // Verify children
        val leftText = token.children[0] as TextToken
        assertEquals("Left", leftText.text.raw)

        val spacer = token.children[1] as SpacerToken
        assertEquals(16, spacer.width)

        val rightText = token.children[2] as TextToken
        assertEquals("Right", rightText.text.raw)
    }

    @Test
    fun `test box builder creates valid box token`() {
        val token = box {
            contentAlignment = BoxAlignment.Center
            text {
                text("Centered Text")
            }
        }

        // Verify token properties
        assertEquals("box", token.id.split("-")[0])
        assertEquals(1, token.version)
        assertEquals(BoxAlignment.Center, token.contentAlignment)
        assertEquals(1, token.children.size)

        // Verify children
        val textToken = token.children[0] as TextToken
        assertEquals("Centered Text", textToken.text.raw)
    }

    @Test
    fun `test card builder creates valid card token`() {
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
                    style = TextStyle.TitleLarge
                }
                text {
                    text("Card Description")
                    style = TextStyle.BodyMedium
                }
            }
        }

        // Verify token properties
        assertEquals("card", token.id.split("-")[0])
        assertEquals(1, token.version)
        assertEquals(4, token.elevation)
        assertEquals(CardShape.ROUNDED16, token.shape)
        assertEquals(ActionType.NAVIGATE, token.onClick?.type)
        assertEquals("details", token.onClick?.data?.get("screen"))
        assertEquals(1, token.children.size)

        // Verify children
        val columnToken = token.children[0] as ColumnToken
        assertEquals(2, columnToken.children.size)

        val titleToken = columnToken.children[0] as TextToken
        assertEquals("Card Title", titleToken.text.raw)
        assertEquals(TextStyle.TitleLarge, titleToken.style)

        val descriptionToken = columnToken.children[1] as TextToken
        assertEquals("Card Description", descriptionToken.text.raw)
        assertEquals(TextStyle.BodyMedium, descriptionToken.style)
    }

    @Test
    fun `test text builder creates valid text token`() {
        val token = text {
            text("Sample Text")
            style = TextStyle.BodyLarge
            color {
                red = 255
                green = 0
                blue = 0
            }
            maxLines = 2
            overflow = TextOverflowValue.Ellipsis
            textAlign = TextAlignValue.Center
            margin {
                all = 16
            }
        }

        // Verify token properties
        assertEquals("text", token.id.split("-")[0])
        assertEquals(1, token.version)
        assertEquals("Sample Text", token.text.raw)
        assertEquals(TextStyle.BodyLarge, token.style)
        assertEquals(255, token.color?.red)
        assertEquals(0, token.color?.green)
        assertEquals(0, token.color?.blue)
        assertEquals(2, token.maxLines)
        assertEquals(TextOverflowValue.Ellipsis, token.overflow)
        assertEquals(TextAlignValue.Center, token.textAlign)
        assertEquals(16, token.margin?.all)
    }

    @Test
    fun `test button builder creates valid button token`() {
        val token = button {
            text("Submit")
            style = ButtonStyle.Elevated
            enabled = false
            margin {
                vertical = 8
            }
            onClick {
                type = ActionType.CUSTOM
                data("action" to "submit_form")
            }
        }

        // Verify token properties
        assertEquals("button", token.id.split("-")[0])
        assertEquals(1, token.version)
        assertEquals("Submit", token.text.raw)
        assertEquals(ButtonStyle.Elevated, token.style)
        assertFalse(token.enabled)
        assertEquals(8, token.margin?.vertical)
        assertEquals(ActionType.CUSTOM, token.onClick.type)
        assertEquals("submit_form", token.onClick.data["action"])
    }

    @Test
    fun `test spacer builder creates valid spacer token`() {
        val token = spacer {
            width = 24
            height = 16
        }

        // Verify token properties
        assertEquals("spacer", token.id.split("-")[0])
        assertEquals(1, token.version)
        assertEquals(24, token.width)
        assertEquals(16, token.height)
    }

    @Test
    fun `test divider builder creates valid divider token`() {
        val token = divider {
            thickness = 2
            color {
                red = 200
                green = 200
                blue = 200
                alpha = 128
            }
            margin {
                vertical = 8
            }
        }

        // Verify token properties
        assertEquals("divider", token.id.split("-")[0])
        assertEquals(1, token.version)
        assertEquals(2, token.thickness)
        assertEquals(200, token.color?.red)
        assertEquals(200, token.color?.green)
        assertEquals(200, token.color?.blue)
        assertEquals(128, token.color?.alpha)
        assertEquals(8, token.margin?.vertical)
    }

    @Test
    fun `test slider builder creates valid slider token`() {
        val token = slider {
            initialValue = 0.5f
            rangeStart = 0f
            rangeEnd = 10f
            steps = 10
            enabled = true
            margin {
                horizontal = 16
            }
            onChange {
                type = ActionType.CUSTOM
                data("value" to "{{value}}")
            }
        }

        // Verify token properties
        assertEquals("slider", token.id.split("-")[0])
        assertEquals(1, token.version)
        assertEquals(0.5f, token.initialValue)
        assertEquals(0f, token.rangeStart)
        assertEquals(10f, token.rangeEnd)
        assertEquals(10, token.steps)
        assertTrue(token.enabled)
        assertEquals(16, token.margin?.horizontal)
        assertEquals(ActionType.CUSTOM, token.onChange?.type)
        assertEquals("{{value}}", token.onChange?.data?.get("value"))
    }

    @Test
    fun `test async image builder creates valid async image token`() {
        val token = asyncImage {
            url("https://example.com/image.jpg")
            widthDp = 200
            heightDp = 150
            clip = ClipShape.ROUNDED8
            contentScale = ContentScale.Crop
            margin {
                all = 8
            }
            errorFallback {
                text("Failed to load image")
            }
            loadingPlaceholder {
                showProgressIndicator = true
                backgroundColor {
                    red = 240
                    green = 240
                    blue = 240
                }
            }
            onClick {
                type = ActionType.OPEN_URL
                data("url" to "https://example.com")
            }
        }

        // Verify token properties
        assertEquals("asyncImage", token.id.split("-")[0])
        assertEquals(1, token.version)
        assertEquals("https://example.com/image.jpg", token.url.raw)
        assertEquals(200, token.widthDp)
        assertEquals(150, token.heightDp)
        assertEquals(ClipShape.ROUNDED8, token.clip)
        assertEquals(ContentScale.Crop, token.contentScale)
        assertEquals(8, token.margin?.all)
        assertEquals("Failed to load image", token.errorFallback?.text?.raw)
        assertTrue(token.loadingPlaceholder?.showProgressIndicator ?: false)
        assertEquals(240, token.loadingPlaceholder?.backgroundColor?.red)
        assertEquals(ActionType.OPEN_URL, token.onClick?.type)
        assertEquals("https://example.com", token.onClick?.data?.get("url"))
    }

    @Test
    fun `test lazy column builder creates valid lazy column token`() {
        val token = lazyColumn {
            alignment = HorizontalAlignment.Center
            for (i in 1..3) {
                text {
                    text("Item $i")
                }
            }
        }

        // Verify token properties
        assertEquals("lazyColumn", token.id.split("-")[0])
        assertEquals(1, token.version)
        assertEquals(HorizontalAlignment.Center, token.alignment)
        assertEquals(3, token.children.size)

        // Verify children
        for (i in 1..3) {
            val textToken = token.children[i-1] as TextToken
            assertEquals("Item $i", textToken.text.raw)
        }
    }

    @Test
    fun `test lazy row builder creates valid lazy row token`() {
        val token = lazyRow {
            alignment = VerticalAlignment.Bottom
            for (i in 1..3) {
                text {
                    text("Item $i")
                }
            }
        }

        // Verify token properties
        assertEquals("lazyRow", token.id.split("-")[0])
        assertEquals(1, token.version)
        assertEquals(VerticalAlignment.Bottom, token.alignment)
        assertEquals(3, token.children.size)

        // Verify children
        for (i in 1..3) {
            val textToken = token.children[i-1] as TextToken
            assertEquals("Item $i", textToken.text.raw)
        }
    }

    @Test
    fun `test id generation`() {
        // Reset the ID generator
        ComponentContext.reset()

        // Generate IDs for different types
        val id1 = ComponentContext.generateId("test")
        val id2 = ComponentContext.generateId("test")

        // Verify that IDs are unique and follow the expected format
        assertTrue(id1.startsWith("test"))
        assertTrue(id2.startsWith("test"))
        assertNotEquals(id1, id2)
    }

    @Test
    fun `test id path management`() {
        // Reset the ID generator
        ComponentContext.reset()

        // Push and pop IDs
        ComponentContext.pushId("parent")
        assertEquals("parent", ComponentContext.getCurrentPath())

        ComponentContext.pushId("child")
        assertEquals("parent.child", ComponentContext.getCurrentPath())

        ComponentContext.popId()
        assertEquals("parent", ComponentContext.getCurrentPath())

        ComponentContext.popId()
        assertEquals("", ComponentContext.getCurrentPath())
    }
}
