package com.baksha97.sdui.dsl

import kotlinx.serialization.json.*

/**
 * Renders UI components based on JSON Schema.
 * This class provides functionality to render UI components based on a JSON Schema definition.
 */
class SchemaRenderer {
    
    /**
     * Renders a token based on its JSON representation and schema.
     * @param tokenJson The JSON representation of the token.
     * @param schema The JSON Schema for the token.
     * @return A Token object representing the rendered UI component.
     */
    fun renderToken(tokenJson: JsonObject, schema: JsonObject): Token? {
        // Determine the token type
        val tokenType = determineTokenType(tokenJson, schema)
        
        // Render the token based on its type
        return when (tokenType) {
            "ColumnToken" -> renderColumnToken(tokenJson)
            "RowToken" -> renderRowToken(tokenJson)
            "BoxToken" -> renderBoxToken(tokenJson)
            "CardToken" -> renderCardToken(tokenJson)
            "TextToken" -> renderTextToken(tokenJson)
            "ButtonToken" -> renderButtonToken(tokenJson)
            "SpacerToken" -> renderSpacerToken(tokenJson)
            "DividerToken" -> renderDividerToken(tokenJson)
            "SliderToken" -> renderSliderToken(tokenJson)
            "AsyncImageToken" -> renderAsyncImageToken(tokenJson)
            "LazyColumnToken" -> renderLazyColumnToken(tokenJson)
            "LazyRowToken" -> renderLazyRowToken(tokenJson)
            else -> null
        }
    }
    
    /**
     * Determines the type of a token based on its JSON representation and schema.
     * @param tokenJson The JSON representation of the token.
     * @param schema The JSON Schema for the token.
     * @return The type of the token as a string.
     */
    private fun determineTokenType(tokenJson: JsonObject, schema: JsonObject): String {
        // Check if the token has a type property
        val typeProperty = tokenJson["type"]?.jsonPrimitive?.content
        if (typeProperty != null) {
            return typeProperty
        }
        
        // If no type property, try to infer the type from the properties
        if (tokenJson.containsKey("children")) {
            // It's a container token
            if (tokenJson.containsKey("alignment")) {
                val alignment = tokenJson["alignment"]?.jsonPrimitive?.content
                return if (alignment in listOf("Start", "Center", "End")) {
                    "ColumnToken"
                } else if (alignment in listOf("Top", "CenterVertically", "Bottom")) {
                    "RowToken"
                } else {
                    "BoxToken"
                }
            } else if (tokenJson.containsKey("contentAlignment")) {
                return "BoxToken"
            } else if (tokenJson.containsKey("elevation") || tokenJson.containsKey("shape")) {
                return "CardToken"
            }
        }
        
        // Check for specific properties of each token type
        return when {
            tokenJson.containsKey("text") && tokenJson.containsKey("style") -> "TextToken"
            tokenJson.containsKey("text") && tokenJson.containsKey("onClick") -> "ButtonToken"
            tokenJson.containsKey("width") || tokenJson.containsKey("height") -> "SpacerToken"
            tokenJson.containsKey("thickness") -> "DividerToken"
            tokenJson.containsKey("initialValue") && tokenJson.containsKey("rangeStart") -> "SliderToken"
            tokenJson.containsKey("url") && tokenJson.containsKey("contentScale") -> "AsyncImageToken"
            else -> "UnknownToken"
        }
    }
    
    /**
     * Renders a ColumnToken based on its JSON representation.
     * @param tokenJson The JSON representation of the token.
     * @return A ColumnToken object representing the rendered UI component.
     */
    private fun renderColumnToken(tokenJson: JsonObject): ColumnToken {
        val id = tokenJson["id"]?.jsonPrimitive?.content ?: "column"
        val version = tokenJson["version"]?.jsonPrimitive?.int ?: 1
        val a11y = tokenJson["a11y"]?.let { parseA11y(it.jsonObject) }
        val padding = tokenJson["padding"]?.let { parsePadding(it.jsonObject) }
        val margin = tokenJson["margin"]?.let { parseMargin(it.jsonObject) }
        val background = tokenJson["background"]?.let { parseBackground(it.jsonObject) }
        val alignment = tokenJson["alignment"]?.jsonPrimitive?.content?.let {
            when (it) {
                "Start" -> HorizontalAlignment.Start
                "Center" -> HorizontalAlignment.Center
                "End" -> HorizontalAlignment.End
                else -> HorizontalAlignment.Start
            }
        } ?: HorizontalAlignment.Start
        
        val children = tokenJson["children"]?.jsonArray?.mapNotNull { 
            renderToken(it.jsonObject, JsonObject(emptyMap())) 
        } ?: emptyList()
        
        return ColumnToken(
            id = id,
            version = version,
            a11y = a11y,
            padding = padding,
            margin = margin,
            background = background,
            alignment = alignment,
            children = children
        )
    }
    
    /**
     * Renders a RowToken based on its JSON representation.
     * @param tokenJson The JSON representation of the token.
     * @return A RowToken object representing the rendered UI component.
     */
    private fun renderRowToken(tokenJson: JsonObject): RowToken {
        val id = tokenJson["id"]?.jsonPrimitive?.content ?: "row"
        val version = tokenJson["version"]?.jsonPrimitive?.int ?: 1
        val a11y = tokenJson["a11y"]?.let { parseA11y(it.jsonObject) }
        val padding = tokenJson["padding"]?.let { parsePadding(it.jsonObject) }
        val margin = tokenJson["margin"]?.let { parseMargin(it.jsonObject) }
        val background = tokenJson["background"]?.let { parseBackground(it.jsonObject) }
        val alignment = tokenJson["alignment"]?.jsonPrimitive?.content?.let {
            when (it) {
                "Top" -> VerticalAlignment.Top
                "CenterVertically" -> VerticalAlignment.CenterVertically
                "Bottom" -> VerticalAlignment.Bottom
                else -> VerticalAlignment.CenterVertically
            }
        } ?: VerticalAlignment.CenterVertically
        
        val children = tokenJson["children"]?.jsonArray?.mapNotNull { 
            renderToken(it.jsonObject, JsonObject(emptyMap())) 
        } ?: emptyList()
        
        return RowToken(
            id = id,
            version = version,
            a11y = a11y,
            padding = padding,
            margin = margin,
            background = background,
            alignment = alignment,
            children = children
        )
    }
    
    /**
     * Renders a BoxToken based on its JSON representation.
     * @param tokenJson The JSON representation of the token.
     * @return A BoxToken object representing the rendered UI component.
     */
    private fun renderBoxToken(tokenJson: JsonObject): BoxToken {
        val id = tokenJson["id"]?.jsonPrimitive?.content ?: "box"
        val version = tokenJson["version"]?.jsonPrimitive?.int ?: 1
        val a11y = tokenJson["a11y"]?.let { parseA11y(it.jsonObject) }
        val padding = tokenJson["padding"]?.let { parsePadding(it.jsonObject) }
        val margin = tokenJson["margin"]?.let { parseMargin(it.jsonObject) }
        val background = tokenJson["background"]?.let { parseBackground(it.jsonObject) }
        val contentAlignment = tokenJson["contentAlignment"]?.jsonPrimitive?.content?.let {
            when (it) {
                "TopStart" -> BoxAlignment.TopStart
                "TopCenter" -> BoxAlignment.TopCenter
                "TopEnd" -> BoxAlignment.TopEnd
                "CenterStart" -> BoxAlignment.CenterStart
                "Center" -> BoxAlignment.Center
                "CenterEnd" -> BoxAlignment.CenterEnd
                "BottomStart" -> BoxAlignment.BottomStart
                "BottomCenter" -> BoxAlignment.BottomCenter
                "BottomEnd" -> BoxAlignment.BottomEnd
                else -> BoxAlignment.Center
            }
        } ?: BoxAlignment.Center
        
        val children = tokenJson["children"]?.jsonArray?.mapNotNull { 
            renderToken(it.jsonObject, JsonObject(emptyMap())) 
        } ?: emptyList()
        
        return BoxToken(
            id = id,
            version = version,
            a11y = a11y,
            padding = padding,
            margin = margin,
            background = background,
            contentAlignment = contentAlignment,
            children = children
        )
    }
    
    /**
     * Renders a CardToken based on its JSON representation.
     * @param tokenJson The JSON representation of the token.
     * @return A CardToken object representing the rendered UI component.
     */
    private fun renderCardToken(tokenJson: JsonObject): CardToken {
        val id = tokenJson["id"]?.jsonPrimitive?.content ?: "card"
        val version = tokenJson["version"]?.jsonPrimitive?.int ?: 1
        val a11y = tokenJson["a11y"]?.let { parseA11y(it.jsonObject) }
        val padding = tokenJson["padding"]?.let { parsePadding(it.jsonObject) }
        val margin = tokenJson["margin"]?.let { parseMargin(it.jsonObject) }
        val background = tokenJson["background"]?.let { parseBackground(it.jsonObject) }
        val elevation = tokenJson["elevation"]?.jsonPrimitive?.int ?: 1
        val shape = tokenJson["shape"]?.jsonPrimitive?.content?.let {
            when (it) {
                "ROUNDED4" -> CardShape.ROUNDED4
                "ROUNDED8" -> CardShape.ROUNDED8
                "ROUNDED12" -> CardShape.ROUNDED12
                "ROUNDED16" -> CardShape.ROUNDED16
                else -> CardShape.ROUNDED8
            }
        } ?: CardShape.ROUNDED8
        
        val onClick = tokenJson["onClick"]?.let { parseAction(it.jsonObject) }
        
        val children = tokenJson["children"]?.jsonArray?.mapNotNull { 
            renderToken(it.jsonObject, JsonObject(emptyMap())) 
        } ?: emptyList()
        
        return CardToken(
            id = id,
            version = version,
            a11y = a11y,
            padding = padding,
            margin = margin,
            elevation = elevation,
            shape = shape,
            background = background,
            onClick = onClick,
            children = children
        )
    }
    
    /**
     * Renders a TextToken based on its JSON representation.
     * @param tokenJson The JSON representation of the token.
     * @return A TextToken object representing the rendered UI component.
     */
    private fun renderTextToken(tokenJson: JsonObject): TextToken {
        val id = tokenJson["id"]?.jsonPrimitive?.content ?: "text"
        val version = tokenJson["version"]?.jsonPrimitive?.int ?: 1
        val a11y = tokenJson["a11y"]?.let { parseA11y(it.jsonObject) }
        val text = tokenJson["text"]?.let { parseTemplateString(it.jsonObject) } ?: TemplateString("")
        val style = tokenJson["style"]?.jsonPrimitive?.content?.let {
            when (it) {
                "DisplayLarge" -> TextStyle.DisplayLarge
                "DisplayMedium" -> TextStyle.DisplayMedium
                "DisplaySmall" -> TextStyle.DisplaySmall
                "HeadlineLarge" -> TextStyle.HeadlineLarge
                "HeadlineMedium" -> TextStyle.HeadlineMedium
                "HeadlineSmall" -> TextStyle.HeadlineSmall
                "TitleLarge" -> TextStyle.TitleLarge
                "TitleMedium" -> TextStyle.TitleMedium
                "TitleSmall" -> TextStyle.TitleSmall
                "BodyLarge" -> TextStyle.BodyLarge
                "BodyMedium" -> TextStyle.BodyMedium
                "BodySmall" -> TextStyle.BodySmall
                "LabelLarge" -> TextStyle.LabelLarge
                "LabelMedium" -> TextStyle.LabelMedium
                "LabelSmall" -> TextStyle.LabelSmall
                else -> TextStyle.BodyMedium
            }
        } ?: TextStyle.BodyMedium
        
        val color = tokenJson["color"]?.let { parseColorValue(it.jsonObject) }
        val maxLines = tokenJson["maxLines"]?.jsonPrimitive?.int
        val overflow = tokenJson["overflow"]?.jsonPrimitive?.content?.let {
            when (it) {
                "Clip" -> TextOverflowValue.Clip
                "Ellipsis" -> TextOverflowValue.Ellipsis
                "Visible" -> TextOverflowValue.Visible
                else -> TextOverflowValue.Clip
            }
        } ?: TextOverflowValue.Clip
        
        val textAlign = tokenJson["textAlign"]?.jsonPrimitive?.content?.let {
            when (it) {
                "Start" -> TextAlignValue.Start
                "Center" -> TextAlignValue.Center
                "End" -> TextAlignValue.End
                "Justify" -> TextAlignValue.Justify
                "Left" -> TextAlignValue.Left
                "Right" -> TextAlignValue.Right
                else -> null
            }
        }
        
        val margin = tokenJson["margin"]?.let { parseMargin(it.jsonObject) }
        
        return TextToken(
            id = id,
            version = version,
            a11y = a11y,
            text = text,
            style = style,
            color = color,
            maxLines = maxLines,
            overflow = overflow,
            textAlign = textAlign,
            margin = margin
        )
    }
    
    /**
     * Renders a ButtonToken based on its JSON representation.
     * @param tokenJson The JSON representation of the token.
     * @return A ButtonToken object representing the rendered UI component.
     */
    private fun renderButtonToken(tokenJson: JsonObject): ButtonToken {
        val id = tokenJson["id"]?.jsonPrimitive?.content ?: "button"
        val version = tokenJson["version"]?.jsonPrimitive?.int ?: 1
        val a11y = tokenJson["a11y"]?.let { parseA11y(it.jsonObject) }
        val text = tokenJson["text"]?.let { parseTemplateString(it.jsonObject) } ?: TemplateString("")
        val style = tokenJson["style"]?.jsonPrimitive?.content?.let {
            when (it) {
                "Filled" -> ButtonStyle.Filled
                "Outlined" -> ButtonStyle.Outlined
                "Text" -> ButtonStyle.Text
                "Elevated" -> ButtonStyle.Elevated
                "FilledTonal" -> ButtonStyle.FilledTonal
                else -> ButtonStyle.Filled
            }
        } ?: ButtonStyle.Filled
        
        val enabled = tokenJson["enabled"]?.jsonPrimitive?.boolean ?: true
        val margin = tokenJson["margin"]?.let { parseMargin(it.jsonObject) }
        val onClick = tokenJson["onClick"]?.let { parseAction(it.jsonObject) } ?: Action(ActionType.CUSTOM)
        
        return ButtonToken(
            id = id,
            version = version,
            a11y = a11y,
            text = text,
            style = style,
            enabled = enabled,
            margin = margin,
            onClick = onClick
        )
    }
    
    /**
     * Renders a SpacerToken based on its JSON representation.
     * @param tokenJson The JSON representation of the token.
     * @return A SpacerToken object representing the rendered UI component.
     */
    private fun renderSpacerToken(tokenJson: JsonObject): SpacerToken {
        val id = tokenJson["id"]?.jsonPrimitive?.content ?: "spacer"
        val version = tokenJson["version"]?.jsonPrimitive?.int ?: 1
        val a11y = tokenJson["a11y"]?.let { parseA11y(it.jsonObject) }
        val width = tokenJson["width"]?.jsonPrimitive?.int
        val height = tokenJson["height"]?.jsonPrimitive?.int
        
        return SpacerToken(
            id = id,
            version = version,
            a11y = a11y,
            width = width,
            height = height
        )
    }
    
    /**
     * Renders a DividerToken based on its JSON representation.
     * @param tokenJson The JSON representation of the token.
     * @return A DividerToken object representing the rendered UI component.
     */
    private fun renderDividerToken(tokenJson: JsonObject): DividerToken {
        val id = tokenJson["id"]?.jsonPrimitive?.content ?: "divider"
        val version = tokenJson["version"]?.jsonPrimitive?.int ?: 1
        val a11y = tokenJson["a11y"]?.let { parseA11y(it.jsonObject) }
        val thickness = tokenJson["thickness"]?.jsonPrimitive?.int ?: 1
        val color = tokenJson["color"]?.let { parseColorValue(it.jsonObject) }
        val margin = tokenJson["margin"]?.let { parseMargin(it.jsonObject) }
        
        return DividerToken(
            id = id,
            version = version,
            a11y = a11y,
            thickness = thickness,
            color = color,
            margin = margin
        )
    }
    
    /**
     * Renders a SliderToken based on its JSON representation.
     * @param tokenJson The JSON representation of the token.
     * @return A SliderToken object representing the rendered UI component.
     */
    private fun renderSliderToken(tokenJson: JsonObject): SliderToken {
        val id = tokenJson["id"]?.jsonPrimitive?.content ?: "slider"
        val version = tokenJson["version"]?.jsonPrimitive?.int ?: 1
        val a11y = tokenJson["a11y"]?.let { parseA11y(it.jsonObject) }
        val initialValue = tokenJson["initialValue"]?.jsonPrimitive?.float ?: 0f
        val rangeStart = tokenJson["rangeStart"]?.jsonPrimitive?.float ?: 0f
        val rangeEnd = tokenJson["rangeEnd"]?.jsonPrimitive?.float ?: 1f
        val steps = tokenJson["steps"]?.jsonPrimitive?.int
        val enabled = tokenJson["enabled"]?.jsonPrimitive?.boolean ?: true
        val margin = tokenJson["margin"]?.let { parseMargin(it.jsonObject) }
        val onChange = tokenJson["onChange"]?.let { parseAction(it.jsonObject) }
        
        return SliderToken(
            id = id,
            version = version,
            a11y = a11y,
            initialValue = initialValue,
            rangeStart = rangeStart,
            rangeEnd = rangeEnd,
            steps = steps,
            enabled = enabled,
            margin = margin,
            onChange = onChange
        )
    }
    
    /**
     * Renders an AsyncImageToken based on its JSON representation.
     * @param tokenJson The JSON representation of the token.
     * @return An AsyncImageToken object representing the rendered UI component.
     */
    private fun renderAsyncImageToken(tokenJson: JsonObject): AsyncImageToken {
        val id = tokenJson["id"]?.jsonPrimitive?.content ?: "asyncImage"
        val version = tokenJson["version"]?.jsonPrimitive?.int ?: 1
        val a11y = tokenJson["a11y"]?.let { parseA11y(it.jsonObject) }
        val url = tokenJson["url"]?.let { parseTemplateString(it.jsonObject) } ?: TemplateString("")
        val widthDp = tokenJson["widthDp"]?.jsonPrimitive?.int
        val heightDp = tokenJson["heightDp"]?.jsonPrimitive?.int
        val layoutWeight = tokenJson["layoutWeight"]?.jsonPrimitive?.float
        val clip = tokenJson["clip"]?.jsonPrimitive?.content?.let {
            when (it) {
                "CIRCLE" -> ClipShape.CIRCLE
                "ROUNDED4" -> ClipShape.ROUNDED4
                "ROUNDED8" -> ClipShape.ROUNDED8
                "ROUNDED12" -> ClipShape.ROUNDED12
                "ROUNDED16" -> ClipShape.ROUNDED16
                else -> null
            }
        }
        
        val contentScale = tokenJson["contentScale"]?.jsonPrimitive?.content?.let {
            when (it) {
                "FillWidth" -> ContentScale.FillWidth
                "FillHeight" -> ContentScale.FillHeight
                "Crop" -> ContentScale.Crop
                "Inside" -> ContentScale.Inside
                "Fit" -> ContentScale.Fit
                "FillBounds" -> ContentScale.FillBounds
                else -> ContentScale.FillWidth
            }
        } ?: ContentScale.FillWidth
        
        val margin = tokenJson["margin"]?.let { parseMargin(it.jsonObject) }
        val errorFallback = tokenJson["errorFallback"]?.let { parseErrorFallback(it.jsonObject) }
        val loadingPlaceholder = tokenJson["loadingPlaceholder"]?.let { parseLoadingPlaceholder(it.jsonObject) }
        val onClick = tokenJson["onClick"]?.let { parseAction(it.jsonObject) }
        
        return AsyncImageToken(
            id = id,
            version = version,
            a11y = a11y,
            url = url,
            widthDp = widthDp,
            heightDp = heightDp,
            layoutWeight = layoutWeight,
            clip = clip,
            contentScale = contentScale,
            margin = margin,
            errorFallback = errorFallback,
            loadingPlaceholder = loadingPlaceholder,
            onClick = onClick
        )
    }
    
    /**
     * Renders a LazyColumnToken based on its JSON representation.
     * @param tokenJson The JSON representation of the token.
     * @return A LazyColumnToken object representing the rendered UI component.
     */
    private fun renderLazyColumnToken(tokenJson: JsonObject): LazyColumnToken {
        val id = tokenJson["id"]?.jsonPrimitive?.content ?: "lazyColumn"
        val version = tokenJson["version"]?.jsonPrimitive?.int ?: 1
        val a11y = tokenJson["a11y"]?.let { parseA11y(it.jsonObject) }
        val padding = tokenJson["padding"]?.let { parsePadding(it.jsonObject) }
        val margin = tokenJson["margin"]?.let { parseMargin(it.jsonObject) }
        val background = tokenJson["background"]?.let { parseBackground(it.jsonObject) }
        val alignment = tokenJson["alignment"]?.jsonPrimitive?.content?.let {
            when (it) {
                "Start" -> HorizontalAlignment.Start
                "Center" -> HorizontalAlignment.Center
                "End" -> HorizontalAlignment.End
                else -> HorizontalAlignment.Start
            }
        } ?: HorizontalAlignment.Start
        
        val children = tokenJson["children"]?.jsonArray?.mapNotNull { 
            renderToken(it.jsonObject, JsonObject(emptyMap())) 
        } ?: emptyList()
        
        return LazyColumnToken(
            id = id,
            version = version,
            a11y = a11y,
            padding = padding,
            margin = margin,
            background = background,
            alignment = alignment,
            children = children
        )
    }
    
    /**
     * Renders a LazyRowToken based on its JSON representation.
     * @param tokenJson The JSON representation of the token.
     * @return A LazyRowToken object representing the rendered UI component.
     */
    private fun renderLazyRowToken(tokenJson: JsonObject): LazyRowToken {
        val id = tokenJson["id"]?.jsonPrimitive?.content ?: "lazyRow"
        val version = tokenJson["version"]?.jsonPrimitive?.int ?: 1
        val a11y = tokenJson["a11y"]?.let { parseA11y(it.jsonObject) }
        val padding = tokenJson["padding"]?.let { parsePadding(it.jsonObject) }
        val margin = tokenJson["margin"]?.let { parseMargin(it.jsonObject) }
        val background = tokenJson["background"]?.let { parseBackground(it.jsonObject) }
        val alignment = tokenJson["alignment"]?.jsonPrimitive?.content?.let {
            when (it) {
                "Top" -> VerticalAlignment.Top
                "CenterVertically" -> VerticalAlignment.CenterVertically
                "Bottom" -> VerticalAlignment.Bottom
                else -> VerticalAlignment.CenterVertically
            }
        } ?: VerticalAlignment.CenterVertically
        
        val children = tokenJson["children"]?.jsonArray?.mapNotNull { 
            renderToken(it.jsonObject, JsonObject(emptyMap())) 
        } ?: emptyList()
        
        return LazyRowToken(
            id = id,
            version = version,
            a11y = a11y,
            padding = padding,
            margin = margin,
            background = background,
            alignment = alignment,
            children = children
        )
    }
    
    // Helper methods for parsing value objects
    
    private fun parseA11y(json: JsonObject): A11y {
        val role = json["role"]?.jsonPrimitive?.content?.let {
            when (it) {
                "BANNER" -> Role.BANNER
                "IMAGE" -> Role.IMAGE
                "BUTTON" -> Role.BUTTON
                "CHECKBOX" -> Role.CHECKBOX
                "HEADER" -> Role.HEADER
                "LINK" -> Role.LINK
                "SWITCH" -> Role.SWITCH
                "TEXT_FIELD" -> Role.TEXT_FIELD
                "SLIDER" -> Role.SLIDER
                "PROGRESS_BAR" -> Role.PROGRESS_BAR
                "RADIO_BUTTON" -> Role.RADIO_BUTTON
                "NONE" -> Role.NONE
                else -> Role.NONE
            }
        } ?: Role.NONE
        
        val label = json["label"]?.let { parseTemplateString(it.jsonObject) } ?: TemplateString("")
        val liveRegion = json["liveRegion"]?.jsonPrimitive?.content?.let {
            when (it) {
                "OFF" -> LiveRegion.OFF
                "POLITE" -> LiveRegion.POLITE
                "ASSERTIVE" -> LiveRegion.ASSERTIVE
                else -> LiveRegion.OFF
            }
        } ?: LiveRegion.OFF
        
        val isEnabled = json["isEnabled"]?.jsonPrimitive?.boolean ?: true
        val isFocusable = json["isFocusable"]?.jsonPrimitive?.boolean ?: true
        
        return A11y(
            role = role,
            label = label,
            liveRegion = liveRegion,
            isEnabled = isEnabled,
            isFocusable = isFocusable
        )
    }
    
    private fun parsePadding(json: JsonObject): Padding {
        val all = json["all"]?.jsonPrimitive?.int
        val horizontal = json["horizontal"]?.jsonPrimitive?.int
        val vertical = json["vertical"]?.jsonPrimitive?.int
        val start = json["start"]?.jsonPrimitive?.int
        val top = json["top"]?.jsonPrimitive?.int
        val end = json["end"]?.jsonPrimitive?.int
        val bottom = json["bottom"]?.jsonPrimitive?.int
        
        return Padding(
            all = all,
            horizontal = horizontal,
            vertical = vertical,
            start = start,
            top = top,
            end = end,
            bottom = bottom
        )
    }
    
    private fun parseMargin(json: JsonObject): Margin {
        val all = json["all"]?.jsonPrimitive?.int
        val horizontal = json["horizontal"]?.jsonPrimitive?.int
        val vertical = json["vertical"]?.jsonPrimitive?.int
        val start = json["start"]?.jsonPrimitive?.int
        val top = json["top"]?.jsonPrimitive?.int
        val end = json["end"]?.jsonPrimitive?.int
        val bottom = json["bottom"]?.jsonPrimitive?.int
        
        return Margin(
            all = all,
            horizontal = horizontal,
            vertical = vertical,
            start = start,
            top = top,
            end = end,
            bottom = bottom
        )
    }
    
    private fun parseBackground(json: JsonObject): Background {
        val color = json["color"]?.let { parseColorValue(it.jsonObject) }
        val borderColor = json["borderColor"]?.let { parseColorValue(it.jsonObject) }
        val borderWidth = json["borderWidth"]?.jsonPrimitive?.int
        val cornerRadius = json["cornerRadius"]?.jsonPrimitive?.int
        
        return Background(
            color = color,
            borderColor = borderColor,
            borderWidth = borderWidth,
            cornerRadius = cornerRadius
        )
    }
    
    private fun parseAction(json: JsonObject): Action {
        val type = json["type"]?.jsonPrimitive?.content?.let {
            when (it) {
                "NAVIGATE" -> ActionType.NAVIGATE
                "DEEP_LINK" -> ActionType.DEEP_LINK
                "OPEN_URL" -> ActionType.OPEN_URL
                "CUSTOM" -> ActionType.CUSTOM
                else -> ActionType.CUSTOM
            }
        } ?: ActionType.CUSTOM
        
        val data = json["data"]?.jsonObject?.mapValues { it.value.jsonPrimitive.content } ?: emptyMap()
        
        return Action(
            type = type,
            data = data
        )
    }
    
    private fun parseColorValue(json: JsonObject): ColorValue {
        val red = json["red"]?.jsonPrimitive?.int ?: 0
        val green = json["green"]?.jsonPrimitive?.int ?: 0
        val blue = json["blue"]?.jsonPrimitive?.int ?: 0
        val alpha = json["alpha"]?.jsonPrimitive?.int ?: 255
        
        return ColorValue(
            red = red,
            green = green,
            blue = blue,
            alpha = alpha
        )
    }
    
    private fun parseErrorFallback(json: JsonObject): ErrorFallback {
        val text = json["text"]?.let { parseTemplateString(it.jsonObject) }
        val iconUrl = json["iconUrl"]?.let { parseTemplateString(it.jsonObject) }
        
        return ErrorFallback(
            text = text,
            iconUrl = iconUrl
        )
    }
    
    private fun parseLoadingPlaceholder(json: JsonObject): LoadingPlaceholder {
        val showProgressIndicator = json["showProgressIndicator"]?.jsonPrimitive?.boolean ?: true
        val backgroundColor = json["backgroundColor"]?.let { parseColorValue(it.jsonObject) }
        
        return LoadingPlaceholder(
            showProgressIndicator = showProgressIndicator,
            backgroundColor = backgroundColor
        )
    }
    
    private fun parseTemplateString(json: JsonObject): TemplateString {
        val raw = json["raw"]?.jsonPrimitive?.content ?: ""
        
        return TemplateString(raw)
    }
}