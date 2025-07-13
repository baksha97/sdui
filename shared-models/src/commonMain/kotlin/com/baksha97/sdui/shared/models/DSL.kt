package com.baksha97.sdui.shared.models

/**
 * DSL for building UI components as data structures.
 * This file contains extension functions and builders that make it easier to create UI tokens.
 */

// Base builder interface
interface TokenBuilder<T : Token> {
    fun build(): T
}

// Builder classes for each token type
class ColumnBuilder(
    var id: String? = null,
    var version: Int = 1,
    var a11y: A11y? = null,
    var padding: Padding? = null,
    var margin: Margin? = null,
    var background: Background? = null,
    var alignment: HorizontalAlignment = HorizontalAlignment.Start,
    val children: MutableList<Token> = mutableListOf()
) : TokenBuilder<ColumnToken> {

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

    // Child builders with context parameter support
    context(uiScope: UIScope)
    fun column(id: String? = null, init: ColumnBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Column")
        val builder = ColumnBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun row(id: String? = null, init: RowBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Row")
        val builder = RowBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun box(id: String? = null, init: BoxBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Box")
        val builder = BoxBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun text(id: String? = null, init: TextBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Text")
        val builder = TextBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun button(id: String? = null, init: ButtonBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Button")
        val builder = ButtonBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun spacer(id: String? = null, init: SpacerBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Spacer")
        val builder = SpacerBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun divider(id: String? = null, init: DividerBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Divider")
        val builder = DividerBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun asyncImage(id: String? = null, init: AsyncImageBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("AsyncImage")
        val builder = AsyncImageBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun card(id: String? = null, init: CardBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Card")
        val builder = CardBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun lazyColumn(id: String? = null, init: LazyColumnBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("LazyColumn")
        val builder = LazyColumnBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun lazyRow(id: String? = null, init: LazyRowBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("LazyRow")
        val builder = LazyRowBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun slider(id: String? = null, init: SliderBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Slider")
        val builder = SliderBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

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
    var id: String? = null,
    var version: Int = 1,
    var a11y: A11y? = null,
    var padding: Padding? = null,
    var margin: Margin? = null,
    var background: Background? = null,
    var alignment: VerticalAlignment = VerticalAlignment.CenterVertically,
    val children: MutableList<Token> = mutableListOf()
) : TokenBuilder<RowToken> {

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

    // Child builders with context parameter support
    context(uiScope: UIScope)
    fun column(id: String? = null, init: ColumnBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Column")
        val builder = ColumnBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun row(id: String? = null, init: RowBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Row")
        val builder = RowBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun box(id: String? = null, init: BoxBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Box")
        val builder = BoxBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun text(id: String? = null, init: TextBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Text")
        val builder = TextBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun button(id: String? = null, init: ButtonBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Button")
        val builder = ButtonBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun spacer(id: String? = null, init: SpacerBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Spacer")
        val builder = SpacerBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun divider(id: String? = null, init: DividerBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Divider")
        val builder = DividerBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun asyncImage(id: String? = null, init: AsyncImageBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("AsyncImage")
        val builder = AsyncImageBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun card(id: String? = null, init: CardBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Card")
        val builder = CardBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

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
    var id: String? = null,
    var version: Int = 1,
    var a11y: A11y? = null,
    var padding: Padding? = null,
    var margin: Margin? = null,
    var background: Background? = null,
    var contentAlignment: BoxAlignment = BoxAlignment.Center,
    val children: MutableList<Token> = mutableListOf()
) : TokenBuilder<BoxToken> {

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

    // Child builders with context parameter support
    context(uiScope: UIScope)
    fun column(id: String? = null, init: ColumnBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Column")
        val builder = ColumnBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun row(id: String? = null, init: RowBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Row")
        val builder = RowBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun box(id: String? = null, init: BoxBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Box")
        val builder = BoxBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun text(id: String? = null, init: TextBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Text")
        val builder = TextBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun button(id: String? = null, init: ButtonBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Button")
        val builder = ButtonBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun spacer(id: String? = null, init: SpacerBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Spacer")
        val builder = SpacerBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun divider(id: String? = null, init: DividerBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Divider")
        val builder = DividerBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun asyncImage(id: String? = null, init: AsyncImageBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("AsyncImage")
        val builder = AsyncImageBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun card(id: String? = null, init: CardBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Card")
        val builder = CardBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

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
    var id: String? = null,
    var version: Int = 1,
    var a11y: A11y? = null,
    var padding: Padding? = null,
    var margin: Margin? = null,
    var elevation: Int = 1,
    var shape: CardShape = CardShape.Rounded8,
    var background: Background? = null,
    var onClick: Action? = null,
    val children: MutableList<Token> = mutableListOf()
) : TokenBuilder<CardToken> {

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

    // Child builders with context parameter support
    context(uiScope: UIScope)
    fun column(id: String? = null, init: ColumnBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Column")
        val builder = ColumnBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun row(id: String? = null, init: RowBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Row")
        val builder = RowBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun box(id: String? = null, init: BoxBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Box")
        val builder = BoxBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun text(id: String? = null, init: TextBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Text")
        val builder = TextBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun button(id: String? = null, init: ButtonBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Button")
        val builder = ButtonBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun spacer(id: String? = null, init: SpacerBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Spacer")
        val builder = SpacerBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun divider(id: String? = null, init: DividerBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Divider")
        val builder = DividerBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun asyncImage(id: String? = null, init: AsyncImageBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("AsyncImage")
        val builder = AsyncImageBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

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
    var id: String? = null,
    var version: Int = 1,
    var a11y: A11y? = null,
    var padding: Padding? = null,
    var margin: Margin? = null,
    var background: Background? = null,
    var alignment: HorizontalAlignment = HorizontalAlignment.Start,
    val children: MutableList<Token> = mutableListOf()
) : TokenBuilder<LazyColumnToken> {

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

    // Child builders with context parameter support
    context(uiScope: UIScope)
    fun column(id: String? = null, init: ColumnBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Column")
        val builder = ColumnBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun row(id: String? = null, init: RowBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Row")
        val builder = RowBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun box(id: String? = null, init: BoxBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Box")
        val builder = BoxBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun text(id: String? = null, init: TextBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Text")
        val builder = TextBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun button(id: String? = null, init: ButtonBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Button")
        val builder = ButtonBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun spacer(id: String? = null, init: SpacerBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Spacer")
        val builder = SpacerBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun divider(id: String? = null, init: DividerBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Divider")
        val builder = DividerBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun asyncImage(id: String? = null, init: AsyncImageBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("AsyncImage")
        val builder = AsyncImageBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun card(id: String? = null, init: CardBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Card")
        val builder = CardBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

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
    var id: String? = null,
    var version: Int = 1,
    var a11y: A11y? = null,
    var padding: Padding? = null,
    var margin: Margin? = null,
    var background: Background? = null,
    var alignment: VerticalAlignment = VerticalAlignment.CenterVertically,
    val children: MutableList<Token> = mutableListOf()
) : TokenBuilder<LazyRowToken> {

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

    // Child builders with context parameter support
    context(uiScope: UIScope)
    fun column(id: String? = null, init: ColumnBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Column")
        val builder = ColumnBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun row(id: String? = null, init: RowBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Row")
        val builder = RowBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun box(id: String? = null, init: BoxBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Box")
        val builder = BoxBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun text(id: String? = null, init: TextBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Text")
        val builder = TextBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun button(id: String? = null, init: ButtonBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Button")
        val builder = ButtonBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun spacer(id: String? = null, init: SpacerBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Spacer")
        val builder = SpacerBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun divider(id: String? = null, init: DividerBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Divider")
        val builder = DividerBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun asyncImage(id: String? = null, init: AsyncImageBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("AsyncImage")
        val builder = AsyncImageBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

    context(uiScope: UIScope)
    fun card(id: String? = null, init: CardBuilder.() -> Unit) {
        val actualId = id ?: uiScope.componentContext.generateId("Card")
        val builder = CardBuilder(actualId)
        uiScope.componentContext.pushId(actualId)
        try {
            builder.apply(init)
            children.add(builder.build())
        } finally {
            uiScope.componentContext.popId()
        }
    }

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
    var id: String? = null,
    var version: Int = 1,
    var a11y: A11y? = null,
    var text: TemplateString = TemplateString(""),
    var style: ButtonStyle = ButtonStyle.Filled,
    var enabled: Boolean = true,
    var margin: Margin? = null,
    var onClick: Action = Action(ActionType.Custom)
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
        val actualId = id ?: error("ID must be provided for ButtonToken")
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

// Top-level DSL functions with Kotlin 2.2.0 context parameters
// These functions use context parameters to automatically propagate UIScope
context(uiScope: UIScope)
fun Column(id: String? = null, content: ColumnBuilder.() -> Unit): ColumnToken {
    val actualId = id ?: uiScope.componentContext.generateId("Column")
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
    val actualId = id ?: uiScope.componentContext.generateId("Row")
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
    val actualId = id ?: uiScope.componentContext.generateId("Box")
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
    val actualId = id ?: uiScope.componentContext.generateId("Card")
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
    val actualId = id ?: uiScope.componentContext.generateId("Text")
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
    val actualId = id ?: uiScope.componentContext.generateId("Button")
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
    val actualId = id ?: uiScope.componentContext.generateId("Spacer")
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
    val actualId = id ?: uiScope.componentContext.generateId("Divider")
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
    val actualId = id ?: uiScope.componentContext.generateId("Slider")
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
    val actualId = id ?: uiScope.componentContext.generateId("AsyncImage")
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
    val actualId = id ?: uiScope.componentContext.generateId("LazyColumn")
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
    val actualId = id ?: uiScope.componentContext.generateId("LazyRow")
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
