package com.baksha97.sdui.dsl

import kotlinx.serialization.json.*

/**
 * Generates JSON Schema from token models.
 * This class provides functionality to generate JSON Schema definitions for token types,
 * which can be used for validation, documentation, and UI rendering.
 */
class SchemaGenerator {

    /**
     * Generates a JSON Schema for all token types.
     * @return A JsonObject containing the schema definitions for all token types.
     */
    fun generateTokenSchema(): JsonObject {
        val definitions = buildJsonObject {
            // Add schema for base token types
            put("Token", generateBaseTokenSchema())
            put("ContainerToken", generateContainerTokenSchema())
            put("InteractiveToken", generateInteractiveTokenSchema())

            // Add schema for concrete token types
            put("ColumnToken", generateColumnTokenSchema())
            put("RowToken", generateRowTokenSchema())
            put("BoxToken", generateBoxTokenSchema())
            put("CardToken", generateCardTokenSchema())
            put("TextToken", generateTextTokenSchema())
            put("ButtonToken", generateButtonTokenSchema())
            put("SpacerToken", generateSpacerTokenSchema())
            put("DividerToken", generateDividerTokenSchema())
            put("SliderToken", generateSliderTokenSchema())
            put("AsyncImageToken", generateAsyncImageTokenSchema())
            put("LazyColumnToken", generateLazyColumnTokenSchema())
            put("LazyRowToken", generateLazyRowTokenSchema())

            // Add schema for value objects
            put("A11y", generateA11ySchema())
            put("Padding", generatePaddingSchema())
            put("Margin", generateMarginSchema())
            put("Background", generateBackgroundSchema())
            put("Action", generateActionSchema())
            put("ColorValue", generateColorValueSchema())
            put("ErrorFallback", generateErrorFallbackSchema())
            put("LoadingPlaceholder", generateLoadingPlaceholderSchema())
            put("TemplateString", generateTemplateStringSchema())
        }

        return buildJsonObject {
            put("\$schema", JsonPrimitive("http://json-schema.org/draft-07/schema#"))
            put("title", JsonPrimitive("Server-Driven UI Token Schema"))
            put("description", JsonPrimitive("Schema for Server-Driven UI tokens"))
            put("type", JsonPrimitive("object"))
            put("definitions", definitions)

            // The root schema allows any token type
            put("oneOf", buildJsonArray {
                add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/ColumnToken")) })
                add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/RowToken")) })
                add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/BoxToken")) })
                add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/CardToken")) })
                add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/TextToken")) })
                add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/ButtonToken")) })
                add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/SpacerToken")) })
                add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/DividerToken")) })
                add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/SliderToken")) })
                add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/AsyncImageToken")) })
                add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/LazyColumnToken")) })
                add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/LazyRowToken")) })
            })
        }
    }

    // Schema definitions for base token types

    private fun generateBaseTokenSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("Token"))
        put("description", JsonPrimitive("Base interface for all UI tokens"))
        put("properties", buildJsonObject {
            put("id", buildJsonObject {
                put("type", JsonPrimitive("string"))
                put("description", JsonPrimitive("Unique identifier for the token"))
            })
            put("version", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Version of the token"))
            })
            put("a11y", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/A11y"))
                put("description", JsonPrimitive("Accessibility properties"))
            })
        })
        put("required", buildJsonArray {
            add(JsonPrimitive("id"))
            add(JsonPrimitive("version"))
        })
    }

    private fun generateContainerTokenSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("ContainerToken"))
        put("description", JsonPrimitive("Base interface for container tokens that hold other tokens"))
        put("allOf", buildJsonArray {
            add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/Token")) })
        })
        put("properties", buildJsonObject {
            put("children", buildJsonObject {
                put("type", JsonPrimitive("array"))
                put("description", JsonPrimitive("Child tokens"))
                put("items", buildJsonObject {
                    put("oneOf", buildJsonArray {
                        add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/ColumnToken")) })
                        add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/RowToken")) })
                        add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/BoxToken")) })
                        add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/CardToken")) })
                        add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/TextToken")) })
                        add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/ButtonToken")) })
                        add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/SpacerToken")) })
                        add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/DividerToken")) })
                        add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/SliderToken")) })
                        add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/AsyncImageToken")) })
                        add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/LazyColumnToken")) })
                        add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/LazyRowToken")) })
                    })
                })
            })
        })
        put("required", buildJsonArray {
            add(JsonPrimitive("children"))
        })
    }

    private fun generateInteractiveTokenSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("InteractiveToken"))
        put("description", JsonPrimitive("Base interface for interactive tokens that can be clicked"))
        put("allOf", buildJsonArray {
            add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/Token")) })
        })
        put("properties", buildJsonObject {
            put("onClick", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Action"))
                put("description", JsonPrimitive("Action to perform when clicked"))
            })
        })
    }

    // Schema definitions for concrete token types

    private fun generateColumnTokenSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("ColumnToken"))
        put("description", JsonPrimitive("A vertical container that arranges its children in a column"))
        put("allOf", buildJsonArray {
            add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/ContainerToken")) })
        })
        put("properties", buildJsonObject {
            put("padding", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Padding"))
            })
            put("margin", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Margin"))
            })
            put("background", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Background"))
            })
            put("alignment", buildJsonObject {
                put("type", JsonPrimitive("string"))
                put("enum", buildJsonArray {
                    add(JsonPrimitive("Start"))
                    add(JsonPrimitive("Center"))
                    add(JsonPrimitive("End"))
                })
                put("description", JsonPrimitive("Horizontal alignment of children"))
            })
        })
    }

    private fun generateRowTokenSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("RowToken"))
        put("description", JsonPrimitive("A horizontal container that arranges its children in a row"))
        put("allOf", buildJsonArray {
            add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/ContainerToken")) })
        })
        put("properties", buildJsonObject {
            put("padding", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Padding"))
            })
            put("margin", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Margin"))
            })
            put("background", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Background"))
            })
            put("alignment", buildJsonObject {
                put("type", JsonPrimitive("string"))
                put("enum", buildJsonArray {
                    add(JsonPrimitive("Top"))
                    add(JsonPrimitive("CenterVertically"))
                    add(JsonPrimitive("Bottom"))
                })
                put("description", JsonPrimitive("Vertical alignment of children"))
            })
        })
    }

    private fun generateBoxTokenSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("BoxToken"))
        put("description", JsonPrimitive("A container that positions its children according to the specified alignment"))
        put("allOf", buildJsonArray {
            add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/ContainerToken")) })
        })
        put("properties", buildJsonObject {
            put("padding", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Padding"))
            })
            put("margin", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Margin"))
            })
            put("background", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Background"))
            })
            put("contentAlignment", buildJsonObject {
                put("type", JsonPrimitive("string"))
                put("enum", buildJsonArray {
                    add(JsonPrimitive("TopStart"))
                    add(JsonPrimitive("TopCenter"))
                    add(JsonPrimitive("TopEnd"))
                    add(JsonPrimitive("CenterStart"))
                    add(JsonPrimitive("Center"))
                    add(JsonPrimitive("CenterEnd"))
                    add(JsonPrimitive("BottomStart"))
                    add(JsonPrimitive("BottomCenter"))
                    add(JsonPrimitive("BottomEnd"))
                })
                put("description", JsonPrimitive("Alignment of content within the box"))
            })
        })
    }

    private fun generateCardTokenSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("CardToken"))
        put("description", JsonPrimitive("A card container with elevation and shape"))
        put("allOf", buildJsonArray {
            add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/ContainerToken")) })
            add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/InteractiveToken")) })
        })
        put("properties", buildJsonObject {
            put("padding", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Padding"))
            })
            put("margin", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Margin"))
            })
            put("elevation", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Elevation of the card (shadow depth)"))
            })
            put("shape", buildJsonObject {
                put("type", JsonPrimitive("string"))
                put("enum", buildJsonArray {
                    add(JsonPrimitive("ROUNDED4"))
                    add(JsonPrimitive("ROUNDED8"))
                    add(JsonPrimitive("ROUNDED12"))
                    add(JsonPrimitive("ROUNDED16"))
                })
                put("description", JsonPrimitive("Shape of the card"))
            })
            put("background", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Background"))
            })
        })
    }

    private fun generateTextTokenSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("TextToken"))
        put("description", JsonPrimitive("A text component"))
        put("allOf", buildJsonArray {
            add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/Token")) })
        })
        put("properties", buildJsonObject {
            put("text", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/TemplateString"))
                put("description", JsonPrimitive("Text content"))
            })
            put("style", buildJsonObject {
                put("type", JsonPrimitive("string"))
                put("enum", buildJsonArray {
                    add(JsonPrimitive("DisplayLarge"))
                    add(JsonPrimitive("DisplayMedium"))
                    add(JsonPrimitive("DisplaySmall"))
                    add(JsonPrimitive("HeadlineLarge"))
                    add(JsonPrimitive("HeadlineMedium"))
                    add(JsonPrimitive("HeadlineSmall"))
                    add(JsonPrimitive("TitleLarge"))
                    add(JsonPrimitive("TitleMedium"))
                    add(JsonPrimitive("TitleSmall"))
                    add(JsonPrimitive("BodyLarge"))
                    add(JsonPrimitive("BodyMedium"))
                    add(JsonPrimitive("BodySmall"))
                    add(JsonPrimitive("LabelLarge"))
                    add(JsonPrimitive("LabelMedium"))
                    add(JsonPrimitive("LabelSmall"))
                })
                put("description", JsonPrimitive("Text style"))
            })
            put("color", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/ColorValue"))
            })
            put("maxLines", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Maximum number of lines"))
            })
            put("overflow", buildJsonObject {
                put("type", JsonPrimitive("string"))
                put("enum", buildJsonArray {
                    add(JsonPrimitive("Clip"))
                    add(JsonPrimitive("Ellipsis"))
                    add(JsonPrimitive("Visible"))
                })
                put("description", JsonPrimitive("Text overflow behavior"))
            })
            put("textAlign", buildJsonObject {
                put("type", JsonPrimitive("string"))
                put("enum", buildJsonArray {
                    add(JsonPrimitive("Start"))
                    add(JsonPrimitive("Center"))
                    add(JsonPrimitive("End"))
                    add(JsonPrimitive("Justify"))
                    add(JsonPrimitive("Left"))
                    add(JsonPrimitive("Right"))
                })
                put("description", JsonPrimitive("Text alignment"))
            })
            put("margin", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Margin"))
            })
        })
        put("required", buildJsonArray {
            add(JsonPrimitive("text"))
        })
    }

    private fun generateButtonTokenSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("ButtonToken"))
        put("description", JsonPrimitive("A button component"))
        put("allOf", buildJsonArray {
            add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/InteractiveToken")) })
        })
        put("properties", buildJsonObject {
            put("text", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/TemplateString"))
                put("description", JsonPrimitive("Button text"))
            })
            put("style", buildJsonObject {
                put("type", JsonPrimitive("string"))
                put("enum", buildJsonArray {
                    add(JsonPrimitive("Filled"))
                    add(JsonPrimitive("Outlined"))
                    add(JsonPrimitive("Text"))
                    add(JsonPrimitive("Elevated"))
                    add(JsonPrimitive("FilledTonal"))
                })
                put("description", JsonPrimitive("Button style"))
            })
            put("enabled", buildJsonObject {
                put("type", JsonPrimitive("boolean"))
                put("description", JsonPrimitive("Whether the button is enabled"))
            })
            put("margin", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Margin"))
            })
        })
        put("required", buildJsonArray {
            add(JsonPrimitive("text"))
            add(JsonPrimitive("onClick"))
        })
    }

    private fun generateSpacerTokenSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("SpacerToken"))
        put("description", JsonPrimitive("A spacer component that adds empty space"))
        put("allOf", buildJsonArray {
            add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/Token")) })
        })
        put("properties", buildJsonObject {
            put("width", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Width of the spacer in dp"))
            })
            put("height", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Height of the spacer in dp"))
            })
        })
    }

    private fun generateDividerTokenSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("DividerToken"))
        put("description", JsonPrimitive("A divider component"))
        put("allOf", buildJsonArray {
            add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/Token")) })
        })
        put("properties", buildJsonObject {
            put("thickness", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Thickness of the divider in dp"))
            })
            put("color", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/ColorValue"))
            })
            put("margin", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Margin"))
            })
        })
    }

    private fun generateSliderTokenSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("SliderToken"))
        put("description", JsonPrimitive("A slider component"))
        put("allOf", buildJsonArray {
            add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/Token")) })
            add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/InteractiveToken")) })
        })
        put("properties", buildJsonObject {
            put("initialValue", buildJsonObject {
                put("type", JsonPrimitive("number"))
                put("description", JsonPrimitive("Initial value of the slider"))
            })
            put("rangeStart", buildJsonObject {
                put("type", JsonPrimitive("number"))
                put("description", JsonPrimitive("Start of the slider range"))
            })
            put("rangeEnd", buildJsonObject {
                put("type", JsonPrimitive("number"))
                put("description", JsonPrimitive("End of the slider range"))
            })
            put("steps", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Number of discrete steps"))
            })
            put("enabled", buildJsonObject {
                put("type", JsonPrimitive("boolean"))
                put("description", JsonPrimitive("Whether the slider is enabled"))
            })
            put("margin", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Margin"))
            })
            put("onChange", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Action"))
                put("description", JsonPrimitive("Action to perform when the slider value changes"))
            })
        })
    }

    private fun generateAsyncImageTokenSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("AsyncImageToken"))
        put("description", JsonPrimitive("An asynchronously loaded image component"))
        put("allOf", buildJsonArray {
            add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/InteractiveToken")) })
        })
        put("properties", buildJsonObject {
            put("url", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/TemplateString"))
                put("description", JsonPrimitive("URL of the image"))
            })
            put("widthDp", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Width of the image in dp"))
            })
            put("heightDp", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Height of the image in dp"))
            })
            put("layoutWeight", buildJsonObject {
                put("type", JsonPrimitive("number"))
                put("description", JsonPrimitive("Layout weight for flexible sizing"))
            })
            put("clip", buildJsonObject {
                put("type", JsonPrimitive("string"))
                put("enum", buildJsonArray {
                    add(JsonPrimitive("CIRCLE"))
                    add(JsonPrimitive("ROUNDED4"))
                    add(JsonPrimitive("ROUNDED8"))
                    add(JsonPrimitive("ROUNDED12"))
                    add(JsonPrimitive("ROUNDED16"))
                })
                put("description", JsonPrimitive("Clipping shape for the image"))
            })
            put("contentScale", buildJsonObject {
                put("type", JsonPrimitive("string"))
                put("enum", buildJsonArray {
                    add(JsonPrimitive("FillWidth"))
                    add(JsonPrimitive("FillHeight"))
                    add(JsonPrimitive("Crop"))
                    add(JsonPrimitive("Inside"))
                    add(JsonPrimitive("Fit"))
                    add(JsonPrimitive("FillBounds"))
                })
                put("description", JsonPrimitive("Content scaling mode"))
            })
            put("margin", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Margin"))
            })
            put("errorFallback", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/ErrorFallback"))
            })
            put("loadingPlaceholder", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/LoadingPlaceholder"))
            })
        })
        put("required", buildJsonArray {
            add(JsonPrimitive("url"))
        })
    }

    private fun generateLazyColumnTokenSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("LazyColumnToken"))
        put("description", JsonPrimitive("A vertically scrolling container that only renders visible items"))
        put("allOf", buildJsonArray {
            add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/ContainerToken")) })
        })
        put("properties", buildJsonObject {
            put("padding", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Padding"))
            })
            put("margin", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Margin"))
            })
            put("background", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Background"))
            })
            put("alignment", buildJsonObject {
                put("type", JsonPrimitive("string"))
                put("enum", buildJsonArray {
                    add(JsonPrimitive("Start"))
                    add(JsonPrimitive("Center"))
                    add(JsonPrimitive("End"))
                })
                put("description", JsonPrimitive("Horizontal alignment of children"))
            })
        })
    }

    private fun generateLazyRowTokenSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("LazyRowToken"))
        put("description", JsonPrimitive("A horizontally scrolling container that only renders visible items"))
        put("allOf", buildJsonArray {
            add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/ContainerToken")) })
        })
        put("properties", buildJsonObject {
            put("padding", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Padding"))
            })
            put("margin", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Margin"))
            })
            put("background", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/Background"))
            })
            put("alignment", buildJsonObject {
                put("type", JsonPrimitive("string"))
                put("enum", buildJsonArray {
                    add(JsonPrimitive("Top"))
                    add(JsonPrimitive("CenterVertically"))
                    add(JsonPrimitive("Bottom"))
                })
                put("description", JsonPrimitive("Vertical alignment of children"))
            })
        })
    }

    // Schema definitions for value objects

    private fun generateA11ySchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("A11y"))
        put("description", JsonPrimitive("Accessibility properties"))
        put("properties", buildJsonObject {
            put("role", buildJsonObject {
                put("type", JsonPrimitive("string"))
                put("enum", buildJsonArray {
                    add(JsonPrimitive("BANNER"))
                    add(JsonPrimitive("IMAGE"))
                    add(JsonPrimitive("BUTTON"))
                    add(JsonPrimitive("CHECKBOX"))
                    add(JsonPrimitive("HEADER"))
                    add(JsonPrimitive("LINK"))
                    add(JsonPrimitive("SWITCH"))
                    add(JsonPrimitive("TEXT_FIELD"))
                    add(JsonPrimitive("SLIDER"))
                    add(JsonPrimitive("PROGRESS_BAR"))
                    add(JsonPrimitive("RADIO_BUTTON"))
                    add(JsonPrimitive("NONE"))
                })
                put("description", JsonPrimitive("Accessibility role"))
            })
            put("label", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/TemplateString"))
                put("description", JsonPrimitive("Accessibility label"))
            })
            put("liveRegion", buildJsonObject {
                put("type", JsonPrimitive("string"))
                put("enum", buildJsonArray {
                    add(JsonPrimitive("OFF"))
                    add(JsonPrimitive("POLITE"))
                    add(JsonPrimitive("ASSERTIVE"))
                })
                put("description", JsonPrimitive("Live region mode"))
            })
            put("isEnabled", buildJsonObject {
                put("type", JsonPrimitive("boolean"))
                put("description", JsonPrimitive("Whether the element is enabled for accessibility"))
            })
            put("isFocusable", buildJsonObject {
                put("type", JsonPrimitive("boolean"))
                put("description", JsonPrimitive("Whether the element is focusable for accessibility"))
            })
        })
        put("required", buildJsonArray {
            add(JsonPrimitive("role"))
            add(JsonPrimitive("label"))
        })
    }

    private fun generatePaddingSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("Padding"))
        put("description", JsonPrimitive("Padding properties"))
        put("properties", buildJsonObject {
            put("all", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Padding on all sides"))
            })
            put("horizontal", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Horizontal padding (left and right)"))
            })
            put("vertical", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Vertical padding (top and bottom)"))
            })
            put("start", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Padding at the start (left in LTR layouts)"))
            })
            put("top", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Padding at the top"))
            })
            put("end", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Padding at the end (right in LTR layouts)"))
            })
            put("bottom", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Padding at the bottom"))
            })
        })
    }

    private fun generateMarginSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("Margin"))
        put("description", JsonPrimitive("Margin properties"))
        put("properties", buildJsonObject {
            put("all", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Margin on all sides"))
            })
            put("horizontal", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Horizontal margin (left and right)"))
            })
            put("vertical", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Vertical margin (top and bottom)"))
            })
            put("start", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Margin at the start (left in LTR layouts)"))
            })
            put("top", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Margin at the top"))
            })
            put("end", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Margin at the end (right in LTR layouts)"))
            })
            put("bottom", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Margin at the bottom"))
            })
        })
    }

    private fun generateBackgroundSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("Background"))
        put("description", JsonPrimitive("Background properties"))
        put("properties", buildJsonObject {
            put("color", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/ColorValue"))
                put("description", JsonPrimitive("Background color"))
            })
            put("borderColor", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/ColorValue"))
                put("description", JsonPrimitive("Border color"))
            })
            put("borderWidth", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Border width in dp"))
            })
            put("cornerRadius", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("description", JsonPrimitive("Corner radius in dp"))
            })
        })
    }

    private fun generateActionSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("Action"))
        put("description", JsonPrimitive("Action to perform when an interactive element is activated"))
        put("properties", buildJsonObject {
            put("type", buildJsonObject {
                put("type", JsonPrimitive("string"))
                put("enum", buildJsonArray {
                    add(JsonPrimitive("NAVIGATE"))
                    add(JsonPrimitive("DEEP_LINK"))
                    add(JsonPrimitive("OPEN_URL"))
                    add(JsonPrimitive("CUSTOM"))
                })
                put("description", JsonPrimitive("Type of action"))
            })
            put("data", buildJsonObject {
                put("type", JsonPrimitive("object"))
                put("additionalProperties", buildJsonObject {
                    put("type", JsonPrimitive("string"))
                })
                put("description", JsonPrimitive("Data associated with the action"))
            })
        })
        put("required", buildJsonArray {
            add(JsonPrimitive("type"))
        })
    }

    private fun generateColorValueSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("ColorValue"))
        put("description", JsonPrimitive("Color value in RGBA format"))
        put("properties", buildJsonObject {
            put("red", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("minimum", JsonPrimitive(0))
                put("maximum", JsonPrimitive(255))
                put("description", JsonPrimitive("Red component (0-255)"))
            })
            put("green", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("minimum", JsonPrimitive(0))
                put("maximum", JsonPrimitive(255))
                put("description", JsonPrimitive("Green component (0-255)"))
            })
            put("blue", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("minimum", JsonPrimitive(0))
                put("maximum", JsonPrimitive(255))
                put("description", JsonPrimitive("Blue component (0-255)"))
            })
            put("alpha", buildJsonObject {
                put("type", JsonPrimitive("integer"))
                put("minimum", JsonPrimitive(0))
                put("maximum", JsonPrimitive(255))
                put("description", JsonPrimitive("Alpha component (0-255)"))
            })
        })
        put("required", buildJsonArray {
            add(JsonPrimitive("red"))
            add(JsonPrimitive("green"))
            add(JsonPrimitive("blue"))
        })
    }

    private fun generateErrorFallbackSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("ErrorFallback"))
        put("description", JsonPrimitive("Fallback content to display when an error occurs"))
        put("properties", buildJsonObject {
            put("text", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/TemplateString"))
                put("description", JsonPrimitive("Error text to display"))
            })
            put("iconUrl", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/TemplateString"))
                put("description", JsonPrimitive("URL of error icon to display"))
            })
        })
    }

    private fun generateLoadingPlaceholderSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("LoadingPlaceholder"))
        put("description", JsonPrimitive("Placeholder content to display while loading"))
        put("properties", buildJsonObject {
            put("showProgressIndicator", buildJsonObject {
                put("type", JsonPrimitive("boolean"))
                put("description", JsonPrimitive("Whether to show a progress indicator"))
            })
            put("backgroundColor", buildJsonObject {
                put("\$ref", JsonPrimitive("#/definitions/ColorValue"))
                put("description", JsonPrimitive("Background color of the placeholder"))
            })
        })
    }

    private fun generateTemplateStringSchema(): JsonObject = buildJsonObject {
        put("type", JsonPrimitive("object"))
        put("title", JsonPrimitive("TemplateString"))
        put("description", JsonPrimitive("String that can contain placeholders for dynamic content"))
        put("properties", buildJsonObject {
            put("raw", buildJsonObject {
                put("type", JsonPrimitive("string"))
                put("description", JsonPrimitive("Raw string with placeholders in the format {{placeholder}}"))
            })
        })
        put("required", buildJsonArray {
            add(JsonPrimitive("raw"))
        })
    }
}
