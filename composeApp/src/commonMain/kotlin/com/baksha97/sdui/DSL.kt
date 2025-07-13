package com.baksha97.sdui

import androidx.compose.ui.Alignment

/**
 * DSL for building UI components as data structures.
 * This file contains extension functions and builders that make it easier to create UI tokens.
 */

// Context for tracking component hierarchy and generating IDs
object ComponentContext {
    private val idStack = mutableListOf<String>()
    private var counter = mutableMapOf<String, Int>()

    fun pushId(id: String) {
        idStack.add(id)
    }

    fun popId() {
        if (idStack.isNotEmpty()) {
            idStack.removeAt(idStack.size - 1)
        }
    }

    fun getCurrentPath(): String {
        return idStack.joinToString(".")
    }

    fun generateId(type: String): String {
        val prefix = if (idStack.isEmpty()) "" else "${getCurrentPath()}."
        val key = "$prefix$type"
        val count = counter[key] ?: 0
        val newCount = count + 1
        counter[key] = newCount
        return if (newCount == 1) "$prefix$type" else "$prefix${type}_$newCount"
    }

    fun reset() {
        idStack.clear()
        counter.clear()
    }
}

// Base builder interface
interface TokenBuilder<T : Token> {
    fun build(): T

    /**
     * Adds an existing token as a child to this builder.
     * This allows for direct composition of tokens within the DSL.
     * Default implementation throws an exception as not all builders support children.
     */
    fun token(token: Token) {
        throw UnsupportedOperationException("This builder does not support adding child tokens")
    }
}

// Context classes for each token type
class ColumnBuilder(
    var id: String? = null,
    var version: Int = 1,
    var a11y: A11y? = null,
    var padding: Padding? = null,
    var margin: Margin? = null,
    var background: Background? = null,
    var alignment: Alignment.Horizontal = Alignment.Start,
    val children: MutableList<Token> = mutableListOf()
) : TokenBuilder<ColumnToken> {

    /**
     * Adds an existing token as a child to this builder.
     * This allows for direct composition of tokens within the DSL.
     */
    override fun token(token: Token) {
        children.add(token)
    }

    fun padding(init: PaddingBuilder.() -> Unit) {
        padding = PaddingBuilder().apply(init).build()
    }

    fun margin(init: MarginBuilder.() -> Unit) {
        margin = MarginBuilder().apply(init).build()
    }

    fun background(init: BackgroundBuilder.() -> Unit) {
        background = BackgroundBuilder().apply(init).build()
    }

    fun a11y(init: A11yBuilder.() -> Unit) {
        a11y = A11yBuilder().apply(init).build()
    }

    // Child builders
    fun column(id: String? = null, init: ColumnBuilder.() -> Unit) {
        val builder = ColumnBuilder(id).apply(init)
        children.add(builder.build())
    }

    fun row(id: String? = null, init: RowBuilder.() -> Unit) {
        val builder = RowBuilder(id).apply(init)
        children.add(builder.build())
    }

    fun box(id: String? = null, init: BoxBuilder.() -> Unit) {
        val builder = BoxBuilder(id).apply(init)
        children.add(builder.build())
    }

    fun text(id: String? = null, init: TextBuilder.() -> Unit) {
        val builder = TextBuilder(id).apply(init)
        children.add(builder.build())
    }

    fun button(id: String? = null, init: ButtonBuilder.() -> Unit) {
        val builder = ButtonBuilder(id).apply(init)
        children.add(builder.build())
    }

    fun spacer(id: String? = null, init: SpacerBuilder.() -> Unit) {
        val builder = SpacerBuilder(id).apply(init)
        children.add(builder.build())
    }

    fun divider(id: String? = null, init: DividerBuilder.() -> Unit) {
        val builder = DividerBuilder(id).apply(init)
        children.add(builder.build())
    }

    fun asyncImage(id: String? = null, init: AsyncImageBuilder.() -> Unit) {
        val builder = AsyncImageBuilder(id).apply(init)
        children.add(builder.build())
    }

    fun card(id: String? = null, init: CardBuilder.() -> Unit) {
        val builder = CardBuilder(id).apply(init)
        children.add(builder.build())
    }

    fun lazyColumn(id: String? = null, init: LazyColumnBuilder.() -> Unit) {
        val builder = LazyColumnBuilder(id).apply(init)
        children.add(builder.build())
    }

    fun lazyRow(id: String? = null, init: LazyRowBuilder.() -> Unit) {
        val builder = LazyRowBuilder(id).apply(init)
        children.add(builder.build())
    }

    fun slider(id: String? = null, init: SliderBuilder.() -> Unit) {
        val builder = SliderBuilder(id).apply(init)
        children.add(builder.build())
    }

    override fun build(): ColumnToken {
        val actualId = id ?: ComponentContext.generateId("column")
        val result = ColumnToken(
            id = actualId,
            version = version,
            a11y = a11y,
            padding = padding,
            margin = margin,
            background = background,
            alignment = alignment,
            children = children
        )
        return result
    }
}

class RowBuilder(
    var id: String? = null,
    var version: Int = 1,
    var a11y: A11y? = null,
    var padding: Padding? = null,
    var margin: Margin? = null,
    var background: Background? = null,
    var alignment: Alignment.Vertical = Alignment.CenterVertically,
    val children: MutableList<Token> = mutableListOf()
) : TokenBuilder<RowToken> {

    /**
     * Adds an existing token as a child to this builder.
     * This allows for direct composition of tokens within the DSL.
     */
    override fun token(token: Token) {
        children.add(token)
    }

    fun padding(init: PaddingBuilder.() -> Unit) {
        padding = PaddingBuilder().apply(init).build()
    }

    fun margin(init: MarginBuilder.() -> Unit) {
        margin = MarginBuilder().apply(init).build()
    }

    fun background(init: BackgroundBuilder.() -> Unit) {
        background = BackgroundBuilder().apply(init).build()
    }

    fun a11y(init: A11yBuilder.() -> Unit) {
        a11y = A11yBuilder().apply(init).build()
    }

    // Child builders (same as ColumnBuilder)
    fun column(id: String, init: ColumnBuilder.() -> Unit) {
        children.add(ColumnBuilder(id).apply(init).build())
    }

    fun row(id: String, init: RowBuilder.() -> Unit) {
        children.add(RowBuilder(id).apply(init).build())
    }

    fun box(id: String, init: BoxBuilder.() -> Unit) {
        children.add(BoxBuilder(id).apply(init).build())
    }

    fun text(id: String, init: TextBuilder.() -> Unit) {
        children.add(TextBuilder(id).apply(init).build())
    }

    fun button(id: String, init: ButtonBuilder.() -> Unit) {
        children.add(ButtonBuilder(id).apply(init).build())
    }

    fun spacer(id: String, init: SpacerBuilder.() -> Unit) {
        children.add(SpacerBuilder(id).apply(init).build())
    }

    fun divider(id: String, init: DividerBuilder.() -> Unit) {
        children.add(DividerBuilder(id).apply(init).build())
    }

    fun asyncImage(id: String, init: AsyncImageBuilder.() -> Unit) {
        children.add(AsyncImageBuilder(id).apply(init).build())
    }

    fun card(id: String, init: CardBuilder.() -> Unit) {
        children.add(CardBuilder(id).apply(init).build())
    }

    override fun build(): RowToken {
        val actualId = id ?: ComponentContext.generateId("row")
        val result = RowToken(
            id = actualId,
            version = version,
            a11y = a11y,
            padding = padding,
            margin = margin,
            background = background,
            alignment = alignment,
            children = children
        )
        return result
    }
}

class BoxBuilder(
    var id: String? = null,
    var version: Int = 1,
    var a11y: A11y? = null,
    var padding: Padding? = null,
    var margin: Margin? = null,
    var background: Background? = null,
    var contentAlignment: BoxAlignment = BoxAlignment.Center,
    val children: MutableList<Token> = mutableListOf()
) : TokenBuilder<BoxToken> {

    /**
     * Adds an existing token as a child to this builder.
     * This allows for direct composition of tokens within the DSL.
     */
    override fun token(token: Token) {
        children.add(token)
    }

    fun padding(init: PaddingBuilder.() -> Unit) {
        padding = PaddingBuilder().apply(init).build()
    }

    fun margin(init: MarginBuilder.() -> Unit) {
        margin = MarginBuilder().apply(init).build()
    }

    fun background(init: BackgroundBuilder.() -> Unit) {
        background = BackgroundBuilder().apply(init).build()
    }

    fun a11y(init: A11yBuilder.() -> Unit) {
        a11y = A11yBuilder().apply(init).build()
    }

    // Child builders (same as ColumnBuilder)
    fun column(id: String, init: ColumnBuilder.() -> Unit) {
        children.add(ColumnBuilder(id).apply(init).build())
    }

    fun row(id: String, init: RowBuilder.() -> Unit) {
        children.add(RowBuilder(id).apply(init).build())
    }

    fun box(id: String, init: BoxBuilder.() -> Unit) {
        children.add(BoxBuilder(id).apply(init).build())
    }

    fun text(id: String, init: TextBuilder.() -> Unit) {
        children.add(TextBuilder(id).apply(init).build())
    }

    fun button(id: String, init: ButtonBuilder.() -> Unit) {
        children.add(ButtonBuilder(id).apply(init).build())
    }

    fun spacer(id: String, init: SpacerBuilder.() -> Unit) {
        children.add(SpacerBuilder(id).apply(init).build())
    }

    fun divider(id: String, init: DividerBuilder.() -> Unit) {
        children.add(DividerBuilder(id).apply(init).build())
    }

    fun asyncImage(id: String, init: AsyncImageBuilder.() -> Unit) {
        children.add(AsyncImageBuilder(id).apply(init).build())
    }

    fun card(id: String, init: CardBuilder.() -> Unit) {
        children.add(CardBuilder(id).apply(init).build())
    }

    override fun build(): BoxToken {
        val actualId = id ?: ComponentContext.generateId("box")
        val result = BoxToken(
            id = actualId,
            version = version,
            a11y = a11y,
            padding = padding,
            margin = margin,
            background = background,
            contentAlignment = contentAlignment,
            children = children
        )
        return result
    }
}

class CardBuilder(
    var id: String? = null,
    var version: Int = 1,
    var a11y: A11y? = null,
    var padding: Padding? = null,
    var margin: Margin? = null,
    var elevation: Int = 1,
    var shape: CardShape = CardShape.ROUNDED8,
    var background: Background? = null,
    var onClick: Action? = null,
    val children: MutableList<Token> = mutableListOf()
) : TokenBuilder<CardToken> {

    /**
     * Adds an existing token as a child to this builder.
     * This allows for direct composition of tokens within the DSL.
     */
    override fun token(token: Token) {
        children.add(token)
    }

    fun padding(init: PaddingBuilder.() -> Unit) {
        padding = PaddingBuilder().apply(init).build()
    }

    fun margin(init: MarginBuilder.() -> Unit) {
        margin = MarginBuilder().apply(init).build()
    }

    fun background(init: BackgroundBuilder.() -> Unit) {
        background = BackgroundBuilder().apply(init).build()
    }

    fun a11y(init: A11yBuilder.() -> Unit) {
        a11y = A11yBuilder().apply(init).build()
    }

    fun onClick(init: ActionBuilder.() -> Unit) {
        onClick = ActionBuilder().apply(init).build()
    }

    // Child builders (same as ColumnBuilder)
    fun column(id: String, init: ColumnBuilder.() -> Unit) {
        children.add(ColumnBuilder(id).apply(init).build())
    }

    fun row(id: String, init: RowBuilder.() -> Unit) {
        children.add(RowBuilder(id).apply(init).build())
    }

    fun box(id: String, init: BoxBuilder.() -> Unit) {
        children.add(BoxBuilder(id).apply(init).build())
    }

    fun text(id: String, init: TextBuilder.() -> Unit) {
        children.add(TextBuilder(id).apply(init).build())
    }

    fun button(id: String, init: ButtonBuilder.() -> Unit) {
        children.add(ButtonBuilder(id).apply(init).build())
    }

    fun spacer(id: String, init: SpacerBuilder.() -> Unit) {
        children.add(SpacerBuilder(id).apply(init).build())
    }

    fun divider(id: String, init: DividerBuilder.() -> Unit) {
        children.add(DividerBuilder(id).apply(init).build())
    }

    fun asyncImage(id: String, init: AsyncImageBuilder.() -> Unit) {
        children.add(AsyncImageBuilder(id).apply(init).build())
    }

    override fun build(): CardToken {
        val actualId = id ?: ComponentContext.generateId("card")
        val result = CardToken(
            id = actualId,
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
        return result
    }
}

class LazyColumnBuilder(
    var id: String? = null,
    var version: Int = 1,
    var a11y: A11y? = null,
    var padding: Padding? = null,
    var margin: Margin? = null,
    var background: Background? = null,
    var alignment: Alignment.Horizontal = Alignment.Start,
    val children: MutableList<Token> = mutableListOf()
) : TokenBuilder<LazyColumnToken> {

    /**
     * Adds an existing token as a child to this builder.
     * This allows for direct composition of tokens within the DSL.
     */
    override fun token(token: Token) {
        children.add(token)
    }

    fun padding(init: PaddingBuilder.() -> Unit) {
        padding = PaddingBuilder().apply(init).build()
    }

    fun margin(init: MarginBuilder.() -> Unit) {
        margin = MarginBuilder().apply(init).build()
    }

    fun background(init: BackgroundBuilder.() -> Unit) {
        background = BackgroundBuilder().apply(init).build()
    }

    fun a11y(init: A11yBuilder.() -> Unit) {
        a11y = A11yBuilder().apply(init).build()
    }

    // Child builders (same as ColumnBuilder)
    fun column(id: String, init: ColumnBuilder.() -> Unit) {
        children.add(ColumnBuilder(id).apply(init).build())
    }

    fun row(id: String, init: RowBuilder.() -> Unit) {
        children.add(RowBuilder(id).apply(init).build())
    }

    fun box(id: String, init: BoxBuilder.() -> Unit) {
        children.add(BoxBuilder(id).apply(init).build())
    }

    fun text(id: String, init: TextBuilder.() -> Unit) {
        children.add(TextBuilder(id).apply(init).build())
    }

    fun button(id: String, init: ButtonBuilder.() -> Unit) {
        children.add(ButtonBuilder(id).apply(init).build())
    }

    fun spacer(id: String, init: SpacerBuilder.() -> Unit) {
        children.add(SpacerBuilder(id).apply(init).build())
    }

    fun divider(id: String, init: DividerBuilder.() -> Unit) {
        children.add(DividerBuilder(id).apply(init).build())
    }

    fun asyncImage(id: String, init: AsyncImageBuilder.() -> Unit) {
        children.add(AsyncImageBuilder(id).apply(init).build())
    }

    fun card(id: String, init: CardBuilder.() -> Unit) {
        children.add(CardBuilder(id).apply(init).build())
    }

    override fun build(): LazyColumnToken {
        val actualId = id ?: ComponentContext.generateId("lazyColumn")
        val result = LazyColumnToken(
            id = actualId,
            version = version,
            a11y = a11y,
            padding = padding,
            margin = margin,
            background = background,
            alignment = alignment,
            children = children
        )
        return result
    }
}

class LazyRowBuilder(
    var id: String? = null,
    var version: Int = 1,
    var a11y: A11y? = null,
    var padding: Padding? = null,
    var margin: Margin? = null,
    var background: Background? = null,
    var alignment: Alignment.Vertical = Alignment.CenterVertically,
    val children: MutableList<Token> = mutableListOf()
) : TokenBuilder<LazyRowToken> {

    /**
     * Adds an existing token as a child to this builder.
     * This allows for direct composition of tokens within the DSL.
     */
    override fun token(token: Token) {
        children.add(token)
    }

    fun padding(init: PaddingBuilder.() -> Unit) {
        padding = PaddingBuilder().apply(init).build()
    }

    fun margin(init: MarginBuilder.() -> Unit) {
        margin = MarginBuilder().apply(init).build()
    }

    fun background(init: BackgroundBuilder.() -> Unit) {
        background = BackgroundBuilder().apply(init).build()
    }

    fun a11y(init: A11yBuilder.() -> Unit) {
        a11y = A11yBuilder().apply(init).build()
    }

    // Child builders (same as ColumnBuilder)
    fun column(id: String, init: ColumnBuilder.() -> Unit) {
        children.add(ColumnBuilder(id).apply(init).build())
    }

    fun row(id: String, init: RowBuilder.() -> Unit) {
        children.add(RowBuilder(id).apply(init).build())
    }

    fun box(id: String, init: BoxBuilder.() -> Unit) {
        children.add(BoxBuilder(id).apply(init).build())
    }

    fun text(id: String, init: TextBuilder.() -> Unit) {
        children.add(TextBuilder(id).apply(init).build())
    }

    fun button(id: String, init: ButtonBuilder.() -> Unit) {
        children.add(ButtonBuilder(id).apply(init).build())
    }

    fun spacer(id: String, init: SpacerBuilder.() -> Unit) {
        children.add(SpacerBuilder(id).apply(init).build())
    }

    fun divider(id: String, init: DividerBuilder.() -> Unit) {
        children.add(DividerBuilder(id).apply(init).build())
    }

    fun asyncImage(id: String, init: AsyncImageBuilder.() -> Unit) {
        children.add(AsyncImageBuilder(id).apply(init).build())
    }

    fun card(id: String, init: CardBuilder.() -> Unit) {
        children.add(CardBuilder(id).apply(init).build())
    }

    override fun build(): LazyRowToken {
        val actualId = id ?: ComponentContext.generateId("lazyRow")
        val result = LazyRowToken(
            id = actualId,
            version = version,
            a11y = a11y,
            padding = padding,
            margin = margin,
            background = background,
            alignment = alignment,
            children = children
        )
        return result
    }
}

class TextBuilder(
    var id: String? = null,
    var version: Int = 1,
    var a11y: A11y? = null,
    var text: TemplateString = TemplateString(""),
    var style: TextStyle = TextStyle.BodyMedium,
    var color: ColorValue? = null,
    var maxLines: Int? = null,
    var overflow: TextOverflowValue = TextOverflowValue.Clip,
    var textAlign: TextAlignValue? = null,
    var margin: Margin? = null
) : TokenBuilder<TextToken> {

    fun text(value: String) {
        text = TemplateString(value)
    }

    fun margin(init: MarginBuilder.() -> Unit) {
        margin = MarginBuilder().apply(init).build()
    }

    fun a11y(init: A11yBuilder.() -> Unit) {
        a11y = A11yBuilder().apply(init).build()
    }

    fun color(init: ColorBuilder.() -> Unit) {
        color = ColorBuilder().apply(init).build()
    }

    override fun build(): TextToken {
        val actualId = id ?: ComponentContext.generateId("text")
        val result = TextToken(
            id = actualId,
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
        return result
    }
}

class ButtonBuilder(
    var id: String? = null,
    var version: Int = 1,
    var a11y: A11y? = null,
    var text: TemplateString = TemplateString(""),
    var style: ButtonStyle = ButtonStyle.Filled,
    var enabled: Boolean = true,
    var margin: Margin? = null,
    var onClick: Action = Action(ActionType.CUSTOM)
) : TokenBuilder<ButtonToken> {

    fun text(value: String) {
        text = TemplateString(value)
    }

    fun margin(init: MarginBuilder.() -> Unit) {
        margin = MarginBuilder().apply(init).build()
    }

    fun a11y(init: A11yBuilder.() -> Unit) {
        a11y = A11yBuilder().apply(init).build()
    }

    fun onClick(init: ActionBuilder.() -> Unit) {
        onClick = ActionBuilder().apply(init).build()
    }

    override fun build(): ButtonToken {
        val actualId = id ?: ComponentContext.generateId("button")
        val result = ButtonToken(
            id = actualId,
            version = version,
            a11y = a11y,
            text = text,
            style = style,
            enabled = enabled,
            margin = margin,
            onClick = onClick
        )
        return result
    }
}

class SpacerBuilder(
    var id: String? = null,
    var version: Int = 1,
    var a11y: A11y? = null,
    var width: Int? = null,
    var height: Int? = null
) : TokenBuilder<SpacerToken> {

    fun a11y(init: A11yBuilder.() -> Unit) {
        a11y = A11yBuilder().apply(init).build()
    }

    override fun build(): SpacerToken {
        val actualId = id ?: ComponentContext.generateId("spacer")
        val result = SpacerToken(
            id = actualId,
            version = version,
            a11y = a11y,
            width = width,
            height = height
        )
        return result
    }
}

class DividerBuilder(
    var id: String? = null,
    var version: Int = 1,
    var a11y: A11y? = null,
    var thickness: Int = 1,
    var color: ColorValue? = null,
    var margin: Margin? = null
) : TokenBuilder<DividerToken> {

    fun margin(init: MarginBuilder.() -> Unit) {
        margin = MarginBuilder().apply(init).build()
    }

    fun a11y(init: A11yBuilder.() -> Unit) {
        a11y = A11yBuilder().apply(init).build()
    }

    fun color(init: ColorBuilder.() -> Unit) {
        color = ColorBuilder().apply(init).build()
    }

    override fun build(): DividerToken {
        val actualId = id ?: ComponentContext.generateId("divider")
        val result = DividerToken(
            id = actualId,
            version = version,
            a11y = a11y,
            thickness = thickness,
            color = color,
            margin = margin
        )
        return result
    }
}

class SliderBuilder(
    var id: String? = null,
    var version: Int = 1,
    var a11y: A11y? = null,
    var initialValue: Float = 0f,
    var valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    var steps: Int? = null,
    var enabled: Boolean = true,
    var margin: Margin? = null,
    var onChange: Action? = null
) : TokenBuilder<SliderToken> {

    fun margin(init: MarginBuilder.() -> Unit) {
        margin = MarginBuilder().apply(init).build()
    }

    fun a11y(init: A11yBuilder.() -> Unit) {
        a11y = A11yBuilder().apply(init).build()
    }

    fun onChange(init: ActionBuilder.() -> Unit) {
        onChange = ActionBuilder().apply(init).build()
    }

    override fun build(): SliderToken {
        val actualId = id ?: ComponentContext.generateId("slider")
        return SliderToken(
            id = actualId,
            version = version,
            a11y = a11y,
            initialValue = initialValue,
            valueRange = valueRange,
            steps = steps,
            enabled = enabled,
            margin = margin,
            onChange = onChange
        )
    }
}

class AsyncImageBuilder(
    var id: String? = null,
    var version: Int = 1,
    var a11y: A11y? = null,
    var url: TemplateString = TemplateString(""),
    var widthDp: Int? = null,
    var heightDp: Int? = null,
    var layoutWeight: Float? = null,
    var clip: ClipShape? = null,
    var contentScale: ContentScale = ContentScale.FillWidth,
    var margin: Margin? = null,
    var errorFallback: ErrorFallback? = null,
    var loadingPlaceholder: LoadingPlaceholder? = null,
    var onClick: Action? = null
) : TokenBuilder<AsyncImageToken> {

    fun url(value: String) {
        url = TemplateString(value)
    }

    fun margin(init: MarginBuilder.() -> Unit) {
        margin = MarginBuilder().apply(init).build()
    }

    fun a11y(init: A11yBuilder.() -> Unit) {
        a11y = A11yBuilder().apply(init).build()
    }

    fun errorFallback(init: ErrorFallbackBuilder.() -> Unit) {
        errorFallback = ErrorFallbackBuilder().apply(init).build()
    }

    fun loadingPlaceholder(init: LoadingPlaceholderBuilder.() -> Unit) {
        loadingPlaceholder = LoadingPlaceholderBuilder().apply(init).build()
    }

    fun onClick(init: ActionBuilder.() -> Unit) {
        onClick = ActionBuilder().apply(init).build()
    }

    override fun build(): AsyncImageToken {
        val actualId = id ?: ComponentContext.generateId("asyncImage")
        val result = AsyncImageToken(
            id = actualId,
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
        return result
    }
}

// Helper builders for common properties
class PaddingBuilder {
    var all: Int? = null
    var horizontal: Int? = null
    var vertical: Int? = null
    var start: Int? = null
    var top: Int? = null
    var end: Int? = null
    var bottom: Int? = null

    fun build(): Padding = Padding(
        all = all,
        horizontal = horizontal,
        vertical = vertical,
        start = start,
        top = top,
        end = end,
        bottom = bottom
    )
}

class MarginBuilder {
    var all: Int? = null
    var horizontal: Int? = null
    var vertical: Int? = null
    var start: Int? = null
    var top: Int? = null
    var end: Int? = null
    var bottom: Int? = null

    fun build(): Margin = Margin(
        all = all,
        horizontal = horizontal,
        vertical = vertical,
        start = start,
        top = top,
        end = end,
        bottom = bottom
    )
}

class BackgroundBuilder {
    var color: ColorValue? = null
    var borderColor: ColorValue? = null
    var borderWidth: Int? = null
    var cornerRadius: Int? = null

    fun color(init: ColorBuilder.() -> Unit) {
        color = ColorBuilder().apply(init).build()
    }

    fun borderColor(init: ColorBuilder.() -> Unit) {
        borderColor = ColorBuilder().apply(init).build()
    }

    fun build(): Background = Background(
        color = color,
        borderColor = borderColor,
        borderWidth = borderWidth,
        cornerRadius = cornerRadius
    )
}

class ColorBuilder {
    var red: Int = 0
    var green: Int = 0
    var blue: Int = 0
    var alpha: Int = 255

    fun build(): ColorValue = ColorValue(
        red = red,
        green = green,
        blue = blue,
        alpha = alpha
    )
}

class A11yBuilder {
    var role: Role = Role.NONE
    var label: TemplateString = TemplateString("")
    var liveRegion: LiveRegion = LiveRegion.OFF
    var isEnabled: Boolean = true
    var isFocusable: Boolean = true

    fun label(value: String) {
        label = TemplateString(value)
    }

    fun build(): A11y = A11y(
        role = role,
        label = label,
        liveRegion = liveRegion,
        isEnabled = isEnabled,
        isFocusable = isFocusable
    )
}

class ActionBuilder {
    var type: ActionType = ActionType.CUSTOM
    var data: Map<String, String> = emptyMap()

    fun data(vararg pairs: Pair<String, String>) {
        data = mapOf(*pairs)
    }

    fun build(): Action = Action(
        type = type,
        data = data
    )
}

class ErrorFallbackBuilder {
    var text: TemplateString? = null
    var iconUrl: TemplateString? = null

    fun text(value: String) {
        text = TemplateString(value)
    }

    fun iconUrl(value: String) {
        iconUrl = TemplateString(value)
    }

    fun build(): ErrorFallback = ErrorFallback(
        text = text,
        iconUrl = iconUrl
    )
}

class LoadingPlaceholderBuilder {
    var showProgressIndicator: Boolean = true
    var backgroundColor: ColorValue? = null

    fun backgroundColor(init: ColorBuilder.() -> Unit) {
        backgroundColor = ColorBuilder().apply(init).build()
    }

    fun build(): LoadingPlaceholder = LoadingPlaceholder(
        showProgressIndicator = showProgressIndicator,
        backgroundColor = backgroundColor
    )
}

// Top-level DSL functions
fun column(id: String? = null, init: ColumnBuilder.() -> Unit): ColumnToken =
    ColumnBuilder(id).apply(init).build()

fun row(id: String? = null, init: RowBuilder.() -> Unit): RowToken =
    RowBuilder(id).apply(init).build()

fun box(id: String? = null, init: BoxBuilder.() -> Unit): BoxToken =
    BoxBuilder(id).apply(init).build()

fun card(id: String? = null, init: CardBuilder.() -> Unit): CardToken =
    CardBuilder(id).apply(init).build()

fun text(id: String? = null, init: TextBuilder.() -> Unit): TextToken =
    TextBuilder(id).apply(init).build()

fun button(id: String? = null, init: ButtonBuilder.() -> Unit): ButtonToken =
    ButtonBuilder(id).apply(init).build()

fun spacer(id: String? = null, init: SpacerBuilder.() -> Unit): SpacerToken =
    SpacerBuilder(id).apply(init).build()

fun divider(id: String? = null, init: DividerBuilder.() -> Unit): DividerToken =
    DividerBuilder(id).apply(init).build()

fun slider(id: String? = null, init: SliderBuilder.() -> Unit): SliderToken =
    SliderBuilder(id).apply(init).build()

fun asyncImage(id: String? = null, init: AsyncImageBuilder.() -> Unit): AsyncImageToken =
    AsyncImageBuilder(id).apply(init).build()

fun lazyColumn(id: String? = null, init: LazyColumnBuilder.() -> Unit): LazyColumnToken =
    LazyColumnBuilder(id).apply(init).build()

fun lazyRow(id: String? = null, init: LazyRowBuilder.() -> Unit): LazyRowToken =
    LazyRowBuilder(id).apply(init).build()

// Screen payload builder
class ScreenPayloadBuilder(
    var id: String,
    val tokens: MutableList<TokenRef> = mutableListOf()
) {
    fun tokenRef(id: String, init: TokenRefBuilder.() -> Unit = {}) {
        tokens.add(TokenRefBuilder(id).apply(init).build())
    }

    fun build(): ScreenPayload = ScreenPayload(
        id = id,
        tokens = tokens
    )
}

class TokenRefBuilder(
    var id: String,
    val bind: MutableMap<String, Any> = mutableMapOf()
) {
    fun bind(vararg pairs: Pair<String, Any>) {
        bind.putAll(pairs)
    }

    fun build(): TokenRef = TokenRef(
        id = id,
        bind = bind
    )
}

fun screenPayload(id: String, init: ScreenPayloadBuilder.() -> Unit): ScreenPayload =
    ScreenPayloadBuilder(id).apply(init).build()

// Extension functions for adding existing tokens to builders
fun ColumnBuilder.add(token: Token) = token(token)
fun RowBuilder.add(token: Token) = token(token)
fun BoxBuilder.add(token: Token) = token(token)
fun CardBuilder.add(token: Token) = token(token)
fun LazyColumnBuilder.add(token: Token) = token(token)
fun LazyRowBuilder.add(token: Token) = token(token)

// Extension functions for adding multiple tokens to builders
fun ColumnBuilder.add(vararg tokens: Token) = tokens.forEach { token(it) }
fun RowBuilder.add(vararg tokens: Token) = tokens.forEach { token(it) }
fun BoxBuilder.add(vararg tokens: Token) = tokens.forEach { token(it) }
fun CardBuilder.add(vararg tokens: Token) = tokens.forEach { token(it) }
fun LazyColumnBuilder.add(vararg tokens: Token) = tokens.forEach { token(it) }
fun LazyRowBuilder.add(vararg tokens: Token) = tokens.forEach { token(it) }
