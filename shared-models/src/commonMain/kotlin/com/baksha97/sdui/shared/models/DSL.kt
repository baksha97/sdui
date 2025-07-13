package com.baksha97.sdui.shared.models

/**
 * DSL for building UI components as data structures.
 * This file contains extension functions and builders that make it easier to create UI tokens.
 */

// Base builder interface
interface TokenBuilder<T : Token> {
    fun build(): T
}

// Common properties interface for builders that support basic styling
interface BaseBuilderProperties {
    var id: String?
    var version: Int
    var a11y: A11y?
}

// Interface for builders that support padding
interface PaddingSupport {
    var padding: Padding?
}

// Interface for builders that support margin
interface MarginSupport {
    var margin: Margin?
}

// Interface for builders that support background
interface BackgroundSupport {
    var background: Background?
}

// Interface for builders that support onClick actions
interface ClickSupport {
    var onClick: Action?
}

// Interface for container builders that have children
interface ContainerSupport {
    val children: MutableList<Token>
}

// Interface for builders that support text property
interface TextSupport {
    var text: TemplateString
}

// Interface for builders that support url property
interface UrlSupport {
    var url: TemplateString
}

// Interface for builders that support color property
interface ColorSupport {
    var color: ColorValue?
}

// Extension functions for common property builders
fun PaddingSupport.padding(init: PaddingBuilder.() -> Unit) {
    padding = PaddingBuilder().apply(init).build()
}

fun MarginSupport.margin(init: MarginBuilder.() -> Unit) {
    margin = MarginBuilder().apply(init).build()
}

fun BackgroundSupport.background(init: BackgroundBuilder.() -> Unit) {
    background = BackgroundBuilder().apply(init).build()
}

fun BaseBuilderProperties.a11y(init: A11yBuilder.() -> Unit) {
    a11y = A11yBuilder().apply(init).build()
}

fun ClickSupport.onClick(init: ActionBuilder.() -> Unit) {
    onClick = ActionBuilder().apply(init).build()
}

// Legacy functions for backward compatibility (deprecated - use property assignment instead)
@Deprecated("Use textContent = \"value\" instead for clarity", ReplaceWith("textContent = value"))
fun TextSupport.text(value: String) {
    text = TemplateString(value)
}

@Deprecated("Use urlSource = \"value\" instead for clarity", ReplaceWith("urlSource = value"))
fun UrlSupport.url(value: String) {
    url = TemplateString(value)
}

// Extension properties for convenient string assignment
// Allows using textContent = "value" instead of text = TemplateString("value")
var TextSupport.textContent: String
    get() = text.raw
    set(value) { text = TemplateString(value) }

var UrlSupport.urlSource: String
    get() = url.raw
    set(value) { url = TemplateString(value) }



fun ColorSupport.color(init: ColorBuilder.() -> Unit) {
    color = ColorBuilder().apply(init).build()
}

// Non-context versions of container functions for use within builder blocks
fun ContainerSupport.column(id: String, init: ColumnBuilder.() -> Unit) {
    val builder = ColumnBuilder(id)
    builder.apply(init)
    children.add(builder.build())
}

fun ContainerSupport.row(id: String, init: RowBuilder.() -> Unit) {
    val builder = RowBuilder(id)
    builder.apply(init)
    children.add(builder.build())
}

fun ContainerSupport.box(id: String, init: BoxBuilder.() -> Unit) {
    val builder = BoxBuilder(id)
    builder.apply(init)
    children.add(builder.build())
}

fun ContainerSupport.text(id: String, init: TextBuilder.() -> Unit) {
    val builder = TextBuilder(id)
    builder.apply(init)
    children.add(builder.build())
}

fun ContainerSupport.button(id: String, init: ButtonBuilder.() -> Unit) {
    val builder = ButtonBuilder(id)
    builder.apply(init)
    children.add(builder.build())
}

fun ContainerSupport.spacer(id: String, init: SpacerBuilder.() -> Unit) {
    val builder = SpacerBuilder(id)
    builder.apply(init)
    children.add(builder.build())
}

fun ContainerSupport.divider(id: String, init: DividerBuilder.() -> Unit) {
    val builder = DividerBuilder(id)
    builder.apply(init)
    children.add(builder.build())
}

fun ContainerSupport.asyncImage(id: String, init: AsyncImageBuilder.() -> Unit) {
    val builder = AsyncImageBuilder(id)
    builder.apply(init)
    children.add(builder.build())
}

fun ContainerSupport.card(id: String, init: CardBuilder.() -> Unit) {
    val builder = CardBuilder(id)
    builder.apply(init)
    children.add(builder.build())
}

fun ContainerSupport.lazyColumn(id: String, init: LazyColumnBuilder.() -> Unit) {
    val builder = LazyColumnBuilder(id)
    builder.apply(init)
    children.add(builder.build())
}

fun ContainerSupport.lazyRow(id: String, init: LazyRowBuilder.() -> Unit) {
    val builder = LazyRowBuilder(id)
    builder.apply(init)
    children.add(builder.build())
}

fun ContainerSupport.slider(id: String, init: SliderBuilder.() -> Unit) {
    val builder = SliderBuilder(id)
    builder.apply(init)
    children.add(builder.build())
}

// Generic function for adding child components with context management
context(uiScope: UIScope)
inline fun <reified T : TokenBuilder<*>> ContainerSupport.addChild(
    id: String? = null,
    builderFactory: (String) -> T,
    init: T.() -> Unit
) {
    val componentName = T::class.simpleName?.removeSuffix("Builder") ?: "Unknown"
    val actualId = id ?: uiScope.componentContext.generateId(componentName)
    val builder = builderFactory(actualId)
    uiScope.componentContext.pushId(actualId)
    try {
        builder.apply(init)
        children.add(builder.build())
    } finally {
        uiScope.componentContext.popId()
    }
}

// Extension functions for container builders to add child components
context(uiScope: UIScope)
fun ContainerSupport.column(id: String? = null, init: ColumnBuilder.() -> Unit) {
    addChild<ColumnBuilder>(id, ::ColumnBuilder, init)
}

context(uiScope: UIScope)
fun ContainerSupport.row(id: String? = null, init: RowBuilder.() -> Unit) {
    addChild<RowBuilder>(id, ::RowBuilder, init)
}

context(uiScope: UIScope)
fun ContainerSupport.box(id: String? = null, init: BoxBuilder.() -> Unit) {
    addChild<BoxBuilder>(id, ::BoxBuilder, init)
}

context(uiScope: UIScope)
fun ContainerSupport.text(id: String? = null, init: TextBuilder.() -> Unit) {
    addChild<TextBuilder>(id, ::TextBuilder, init)
}

context(uiScope: UIScope)
fun ContainerSupport.button(id: String? = null, init: ButtonBuilder.() -> Unit) {
    addChild<ButtonBuilder>(id, ::ButtonBuilder, init)
}

context(uiScope: UIScope)
fun ContainerSupport.spacer(id: String? = null, init: SpacerBuilder.() -> Unit) {
    addChild<SpacerBuilder>(id, ::SpacerBuilder, init)
}

context(uiScope: UIScope)
fun ContainerSupport.divider(id: String? = null, init: DividerBuilder.() -> Unit) {
    addChild<DividerBuilder>(id, ::DividerBuilder, init)
}

context(uiScope: UIScope)
fun ContainerSupport.asyncImage(id: String? = null, init: AsyncImageBuilder.() -> Unit) {
    addChild<AsyncImageBuilder>(id, ::AsyncImageBuilder, init)
}

context(uiScope: UIScope)
fun ContainerSupport.card(id: String? = null, init: CardBuilder.() -> Unit) {
    addChild<CardBuilder>(id, ::CardBuilder, init)
}

context(uiScope: UIScope)
fun ContainerSupport.lazyColumn(id: String? = null, init: LazyColumnBuilder.() -> Unit) {
    addChild<LazyColumnBuilder>(id, ::LazyColumnBuilder, init)
}

context(uiScope: UIScope)
fun ContainerSupport.lazyRow(id: String? = null, init: LazyRowBuilder.() -> Unit) {
    addChild<LazyRowBuilder>(id, ::LazyRowBuilder, init)
}

context(uiScope: UIScope)
fun ContainerSupport.slider(id: String? = null, init: SliderBuilder.() -> Unit) {
    addChild<SliderBuilder>(id, ::SliderBuilder, init)
}

// Builder classes for each token type
class ColumnBuilder(
    override var id: String? = null,
    override var version: Int = 1,
    override var a11y: A11y? = null,
    override var padding: Padding? = null,
    override var margin: Margin? = null,
    override var background: Background? = null,
    var alignment: HorizontalAlignment = HorizontalAlignment.Start,
    override val children: MutableList<Token> = mutableListOf()
) : TokenBuilder<ColumnToken>, BaseBuilderProperties, PaddingSupport, MarginSupport, BackgroundSupport, ContainerSupport {

    override fun build(): ColumnToken {
        val actualId = id ?: error("ID must be provided for ColumnToken")
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
    override var id: String? = null,
    override var version: Int = 1,
    override var a11y: A11y? = null,
    override var padding: Padding? = null,
    override var margin: Margin? = null,
    override var background: Background? = null,
    var alignment: VerticalAlignment = VerticalAlignment.CenterVertically,
    override val children: MutableList<Token> = mutableListOf()
) : TokenBuilder<RowToken>, BaseBuilderProperties, PaddingSupport, MarginSupport, BackgroundSupport, ContainerSupport {

    override fun build(): RowToken {
        val actualId = id ?: error("ID must be provided for RowToken")
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
    override var id: String? = null,
    override var version: Int = 1,
    override var a11y: A11y? = null,
    override var padding: Padding? = null,
    override var margin: Margin? = null,
    override var background: Background? = null,
    var contentAlignment: BoxAlignment = BoxAlignment.Center,
    override val children: MutableList<Token> = mutableListOf()
) : TokenBuilder<BoxToken>, BaseBuilderProperties, PaddingSupport, MarginSupport, BackgroundSupport, ContainerSupport {

    override fun build(): BoxToken {
        val actualId = id ?: error("ID must be provided for BoxToken")
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
    override var id: String? = null,
    override var version: Int = 1,
    override var a11y: A11y? = null,
    override var padding: Padding? = null,
    override var margin: Margin? = null,
    var elevation: Int = 1,
    var shape: CardShape = CardShape.Rounded8,
    override var background: Background? = null,
    override var onClick: Action? = null,
    override val children: MutableList<Token> = mutableListOf()
) : TokenBuilder<CardToken>, BaseBuilderProperties, PaddingSupport, MarginSupport, BackgroundSupport, ClickSupport, ContainerSupport {

    override fun build(): CardToken {
        val actualId = id ?: error("ID must be provided for CardToken")
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
    override var id: String? = null,
    override var version: Int = 1,
    override var a11y: A11y? = null,
    override var padding: Padding? = null,
    override var margin: Margin? = null,
    override var background: Background? = null,
    var alignment: HorizontalAlignment = HorizontalAlignment.Start,
    override val children: MutableList<Token> = mutableListOf()
) : TokenBuilder<LazyColumnToken>, ContainerSupport, BaseBuilderProperties, PaddingSupport, MarginSupport, BackgroundSupport {

    override fun build(): LazyColumnToken {
        val actualId = id ?: error("ID must be provided for LazyColumnToken")
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
    override var id: String? = null,
    override var version: Int = 1,
    override var a11y: A11y? = null,
    override var padding: Padding? = null,
    override var margin: Margin? = null,
    override var background: Background? = null,
    var alignment: VerticalAlignment = VerticalAlignment.CenterVertically,
    override val children: MutableList<Token> = mutableListOf()
) : TokenBuilder<LazyRowToken>, ContainerSupport, BaseBuilderProperties, PaddingSupport, MarginSupport, BackgroundSupport {

    override fun build(): LazyRowToken {
        val actualId = id ?: error("ID must be provided for LazyRowToken")
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
    override var id: String? = null,
    override var version: Int = 1,
    override var a11y: A11y? = null,
    override var text: TemplateString = TemplateString(""),
    var style: TextStyle = TextStyle.BodyMedium,
    override var color: ColorValue? = null,
    var maxLines: Int? = null,
    var overflow: TextOverflowValue = TextOverflowValue.Clip,
    var textAlign: TextAlignValue? = null,
    override var margin: Margin? = null
) : TokenBuilder<TextToken>, BaseBuilderProperties, MarginSupport, TextSupport, ColorSupport {

    override fun build(): TextToken {
        val actualId = id ?: error("ID must be provided for TextToken")
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
    override var id: String? = null,
    override var version: Int = 1,
    override var a11y: A11y? = null,
    override var text: TemplateString = TemplateString(""),
    var style: ButtonStyle = ButtonStyle.Filled,
    var enabled: Boolean = true,
    override var margin: Margin? = null,
    override var onClick: Action? = Action(ActionType.Custom)
) : TokenBuilder<ButtonToken>, BaseBuilderProperties, MarginSupport, TextSupport, ClickSupport {

    override fun build(): ButtonToken {
        val actualId = id ?: error("ID must be provided for ButtonToken")
        val result = ButtonToken(
            id = actualId,
            version = version,
            a11y = a11y,
            text = text,
            style = style,
            enabled = enabled,
            margin = margin,
            onClick = onClick ?: Action(ActionType.Custom)
        )
        return result
    }
}

class SpacerBuilder(
    override var id: String? = null,
    override var version: Int = 1,
    override var a11y: A11y? = null,
    var width: Int? = null,
    var height: Int? = null
) : TokenBuilder<SpacerToken>, BaseBuilderProperties {

    override fun build(): SpacerToken {
        val actualId = id ?: error("ID must be provided for SpacerToken")
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
    override var id: String? = null,
    override var version: Int = 1,
    override var a11y: A11y? = null,
    var thickness: Int = 1,
    override var color: ColorValue? = null,
    override var margin: Margin? = null
) : TokenBuilder<DividerToken>, BaseBuilderProperties, MarginSupport, ColorSupport {

    override fun build(): DividerToken {
        val actualId = id ?: error("ID must be provided for DividerToken")
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
    override var id: String? = null,
    override var version: Int = 1,
    override var a11y: A11y? = null,
    var initialValue: Float = 0f,
    var valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    var steps: Int? = null,
    var enabled: Boolean = true,
    override var margin: Margin? = null,
    var onChange: Action? = null
) : TokenBuilder<SliderToken>, BaseBuilderProperties, MarginSupport {

    fun onChange(init: ActionBuilder.() -> Unit) {
        onChange = ActionBuilder().apply(init).build()
    }

    override fun build(): SliderToken {
        val actualId = id ?: error("ID must be provided for SliderToken")
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
    override var id: String? = null,
    override var version: Int = 1,
    override var a11y: A11y? = null,
    override var url: TemplateString = TemplateString(""),
    var widthDp: Int? = null,
    var heightDp: Int? = null,
    var layoutWeight: Float? = null,
    var clip: ClipShape? = null,
    var contentScale: ContentScale = ContentScale.FillWidth,
    override var margin: Margin? = null,
    var errorFallback: ErrorFallback? = null,
    var loadingPlaceholder: LoadingPlaceholder? = null,
    override var onClick: Action? = null
) : TokenBuilder<AsyncImageToken>, BaseBuilderProperties, MarginSupport, UrlSupport, ClickSupport {

    fun errorFallback(init: ErrorFallbackBuilder.() -> Unit) {
        errorFallback = ErrorFallbackBuilder().apply(init).build()
    }

    fun loadingPlaceholder(init: LoadingPlaceholderBuilder.() -> Unit) {
        loadingPlaceholder = LoadingPlaceholderBuilder().apply(init).build()
    }

    override fun build(): AsyncImageToken {
        val actualId = id ?: error("ID must be provided for AsyncImageToken")
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
    var role: Role = Role.None
    var label: TemplateString = TemplateString("")
    var liveRegion: LiveRegion = LiveRegion.Off
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
    var type: ActionType = ActionType.Custom
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

// Helper function to derive component name from builder class
inline fun <reified T> getComponentName(): String {
    return T::class.simpleName?.removeSuffix("Builder") ?: "Unknown"
}

// Top-level DSL functions with Kotlin 2.2.0 context parameters
// These functions use context parameters to automatically propagate UIScope
context(uiScope: UIScope)
fun Column(id: String? = null, content: ColumnBuilder.() -> Unit): ColumnToken {
    val actualId = id ?: uiScope.componentContext.generateId(getComponentName<ColumnBuilder>())
    val builder = ColumnBuilder(actualId)
    uiScope.componentContext.pushId(actualId)
    try {
        builder.apply(content)
        return builder.build()
    } finally {
        uiScope.componentContext.popId()
    }
}

context(uiScope: UIScope)
fun Row(id: String? = null, content: RowBuilder.() -> Unit): RowToken {
    val actualId = id ?: uiScope.componentContext.generateId(getComponentName<RowBuilder>())
    val builder = RowBuilder(actualId)
    uiScope.componentContext.pushId(actualId)
    try {
        builder.apply(content)
        return builder.build()
    } finally {
        uiScope.componentContext.popId()
    }
}

context(uiScope: UIScope)
fun Box(id: String? = null, content: BoxBuilder.() -> Unit): BoxToken {
    val actualId = id ?: uiScope.componentContext.generateId(getComponentName<BoxBuilder>())
    val builder = BoxBuilder(actualId)
    uiScope.componentContext.pushId(actualId)
    try {
        builder.apply(content)
        return builder.build()
    } finally {
        uiScope.componentContext.popId()
    }
}

context(uiScope: UIScope)
fun Card(id: String? = null, content: CardBuilder.() -> Unit): CardToken {
    val actualId = id ?: uiScope.componentContext.generateId(getComponentName<CardBuilder>())
    val builder = CardBuilder(actualId)
    uiScope.componentContext.pushId(actualId)
    try {
        builder.apply(content)
        return builder.build()
    } finally {
        uiScope.componentContext.popId()
    }
}

context(uiScope: UIScope)
fun Text(id: String? = null, content: TextBuilder.() -> Unit): TextToken {
    val actualId = id ?: uiScope.componentContext.generateId(getComponentName<TextBuilder>())
    val builder = TextBuilder(actualId)
    uiScope.componentContext.pushId(actualId)
    try {
        builder.apply(content)
        return builder.build()
    } finally {
        uiScope.componentContext.popId()
    }
}

context(uiScope: UIScope)
fun Button(id: String? = null, content: ButtonBuilder.() -> Unit): ButtonToken {
    val actualId = id ?: uiScope.componentContext.generateId(getComponentName<ButtonBuilder>())
    val builder = ButtonBuilder(actualId)
    uiScope.componentContext.pushId(actualId)
    try {
        builder.apply(content)
        return builder.build()
    } finally {
        uiScope.componentContext.popId()
    }
}

context(uiScope: UIScope)
fun Spacer(id: String? = null, content: SpacerBuilder.() -> Unit): SpacerToken {
    val actualId = id ?: uiScope.componentContext.generateId(getComponentName<SpacerBuilder>())
    val builder = SpacerBuilder(actualId)
    uiScope.componentContext.pushId(actualId)
    try {
        builder.apply(content)
        return builder.build()
    } finally {
        uiScope.componentContext.popId()
    }
}

context(uiScope: UIScope)
fun Divider(id: String? = null, content: DividerBuilder.() -> Unit): DividerToken {
    val actualId = id ?: uiScope.componentContext.generateId(getComponentName<DividerBuilder>())
    val builder = DividerBuilder(actualId)
    uiScope.componentContext.pushId(actualId)
    try {
        builder.apply(content)
        return builder.build()
    } finally {
        uiScope.componentContext.popId()
    }
}

context(uiScope: UIScope)
fun Slider(id: String? = null, content: SliderBuilder.() -> Unit): SliderToken {
    val actualId = id ?: uiScope.componentContext.generateId(getComponentName<SliderBuilder>())
    val builder = SliderBuilder(actualId)
    uiScope.componentContext.pushId(actualId)
    try {
        builder.apply(content)
        return builder.build()
    } finally {
        uiScope.componentContext.popId()
    }
}

context(uiScope: UIScope)
fun AsyncImage(id: String? = null, content: AsyncImageBuilder.() -> Unit): AsyncImageToken {
    val actualId = id ?: uiScope.componentContext.generateId(getComponentName<AsyncImageBuilder>())
    val builder = AsyncImageBuilder(actualId)
    uiScope.componentContext.pushId(actualId)
    try {
        builder.apply(content)
        return builder.build()
    } finally {
        uiScope.componentContext.popId()
    }
}

context(uiScope: UIScope)
fun LazyColumn(id: String? = null, content: LazyColumnBuilder.() -> Unit): LazyColumnToken {
    val actualId = id ?: uiScope.componentContext.generateId(getComponentName<LazyColumnBuilder>())
    val builder = LazyColumnBuilder(actualId)
    uiScope.componentContext.pushId(actualId)
    try {
        builder.apply(content)
        return builder.build()
    } finally {
        uiScope.componentContext.popId()
    }
}

context(uiScope: UIScope)
fun LazyRow(id: String? = null, content: LazyRowBuilder.() -> Unit): LazyRowToken {
    val actualId = id ?: uiScope.componentContext.generateId(getComponentName<LazyRowBuilder>())
    val builder = LazyRowBuilder(actualId)
    uiScope.componentContext.pushId(actualId)
    try {
        builder.apply(content)
        return builder.build()
    } finally {
        uiScope.componentContext.popId()
    }
}


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
    val bind: MutableMap<String, String> = mutableMapOf()
) {
    fun bind(vararg pairs: Pair<String, String>) {
        bind.putAll(pairs)
    }

    fun build(): TokenRef = TokenRef(
        id = id,
        bind = bind
    )
}

fun screenPayload(id: String, init: ScreenPayloadBuilder.() -> Unit): ScreenPayload =
    ScreenPayloadBuilder(id).apply(init).build()
