package com.baksha97.sdui.dsl

import kotlinx.serialization.Serializable

/**
 * Core token hierarchy for the DSL.
 * These are the data models that represent UI components.
 */

sealed interface Token {
    val id: String
    val version: Int
    val a11y: A11y?

    /**
     * Minimum supported version for this token type.
     * If server sends a token with version < minSupportedVersion, 
     * it should be rendered as a fallback or ignored.
     */
    val minSupportedVersion: Int
        get() = 1
}

/**
 * Base interface for container tokens that hold other tokens
 */
sealed interface ContainerToken : Token {
    val children: List<Token>
}

/**
 * Base interface for interactive tokens that can be clicked
 */
sealed interface InteractiveToken : Token {
    val onClick: Action?
}

@Serializable
data class ColumnToken(
    override val id: String,
    override val version: Int,
    override val a11y: A11y? = null,
    val padding: Padding? = null,
    val margin: Margin? = null,
    val background: Background? = null,
    val alignment: HorizontalAlignment = HorizontalAlignment.Start,
    override val children: List<Token>
) : ContainerToken {
    override val minSupportedVersion: Int = 1
}

@Serializable
data class RowToken(
    override val id: String,
    override val version: Int,
    override val a11y: A11y? = null,
    val padding: Padding? = null,
    val margin: Margin? = null,
    val background: Background? = null,
    val alignment: VerticalAlignment = VerticalAlignment.CenterVertically,
    override val children: List<Token>
) : ContainerToken {
    override val minSupportedVersion: Int = 1
}

@Serializable
data class BoxToken(
    override val id: String,
    override val version: Int,
    override val a11y: A11y? = null,
    val padding: Padding? = null,
    val margin: Margin? = null,
    val background: Background? = null,
    val contentAlignment: BoxAlignment = BoxAlignment.Center,
    override val children: List<Token>
) : ContainerToken {
    override val minSupportedVersion: Int = 1
}

@Serializable
data class LazyColumnToken(
    override val id: String,
    override val version: Int,
    override val a11y: A11y? = null,
    val padding: Padding? = null,
    val margin: Margin? = null,
    val background: Background? = null,
    val alignment: HorizontalAlignment = HorizontalAlignment.Start,
    override val children: List<Token>
) : ContainerToken {
    override val minSupportedVersion: Int = 1
}

@Serializable
data class LazyRowToken(
    override val id: String,
    override val version: Int,
    override val a11y: A11y? = null,
    val padding: Padding? = null,
    val margin: Margin? = null,
    val background: Background? = null,
    val alignment: VerticalAlignment = VerticalAlignment.CenterVertically,
    override val children: List<Token>
) : ContainerToken {
    override val minSupportedVersion: Int = 1
}

@Serializable
data class CardToken(
    override val id: String,
    override val version: Int,
    override val a11y: A11y? = null,
    val padding: Padding? = null,
    val margin: Margin? = null,
    val elevation: Int = 1,
    val shape: CardShape = CardShape.ROUNDED8,
    val background: Background? = null,
    override val onClick: Action? = null,
    override val children: List<Token>
) : ContainerToken, InteractiveToken {
    override val minSupportedVersion: Int = 1
}

@Serializable
data class TextToken(
    override val id: String,
    override val version: Int,
    override val a11y: A11y? = null,
    val text: TemplateString,
    val style: TextStyle = TextStyle.BodyMedium,
    val color: ColorValue? = null,
    val maxLines: Int? = null,
    val overflow: TextOverflowValue = TextOverflowValue.Clip,
    val textAlign: TextAlignValue? = null,
    val margin: Margin? = null
) : Token {
    override val minSupportedVersion: Int = 1
}

@Serializable
data class ButtonToken(
    override val id: String,
    override val version: Int,
    override val a11y: A11y? = null,
    val text: TemplateString,
    val style: ButtonStyle = ButtonStyle.Filled,
    val enabled: Boolean = true,
    val margin: Margin? = null,
    override val onClick: Action
) : InteractiveToken {
    override val minSupportedVersion: Int = 1
}

@Serializable
data class SpacerToken(
    override val id: String,
    override val version: Int,
    override val a11y: A11y? = null,
    val width: Int? = null,
    val height: Int? = null
) : Token {
    override val minSupportedVersion: Int = 1
}

@Serializable
data class DividerToken(
    override val id: String,
    override val version: Int,
    override val a11y: A11y? = null,
    val thickness: Int = 1,
    val color: ColorValue? = null,
    val margin: Margin? = null
) : Token {
    override val minSupportedVersion: Int = 1
}

/**
 * A token representing a slider component.
 */
@Serializable
data class SliderToken(
    override val id: String,
    override val version: Int,
    override val a11y: A11y? = null,
    val initialValue: Float = 0f,
    val rangeStart: Float = 0f,
    val rangeEnd: Float = 1f,
    val steps: Int? = null,
    val enabled: Boolean = true,
    val margin: Margin? = null,
    val onChange: Action? = null
) : Token, InteractiveToken {
    override val onClick: Action? = null
    override val minSupportedVersion: Int = 1
}

@Serializable
data class AsyncImageToken(
    override val id: String,
    override val version: Int,
    override val a11y: A11y? = null,
    val url: TemplateString,
    val widthDp: Int? = null,
    val heightDp: Int? = null,
    val layoutWeight: Float? = null,
    val clip: ClipShape? = null,
    val contentScale: ContentScale = ContentScale.FillWidth,
    val margin: Margin? = null,
    val errorFallback: ErrorFallback? = null,
    val loadingPlaceholder: LoadingPlaceholder? = null,
    override val onClick: Action? = null
) : InteractiveToken {
    override val minSupportedVersion: Int = 1
}

/* ───────── Value objects ───────── */

@Serializable
data class A11y(
    val role: Role,
    val label: TemplateString,
    val liveRegion: LiveRegion = LiveRegion.OFF,
    val isEnabled: Boolean = true,
    val isFocusable: Boolean = true
)

enum class Role { 
    BANNER, IMAGE, BUTTON, CHECKBOX, HEADER, LINK, SWITCH, 
    TEXT_FIELD, SLIDER, PROGRESS_BAR, RADIO_BUTTON, NONE 
}

enum class LiveRegion { OFF, POLITE, ASSERTIVE }

@Serializable 
data class Padding(
    val all: Int? = null,
    val horizontal: Int? = null,
    val vertical: Int? = null,
    val start: Int? = null,
    val top: Int? = null,
    val end: Int? = null,
    val bottom: Int? = null
)

@Serializable 
data class Margin(
    val all: Int? = null,
    val horizontal: Int? = null,
    val vertical: Int? = null,
    val start: Int? = null,
    val top: Int? = null,
    val end: Int? = null,
    val bottom: Int? = null
)

@Serializable
data class Background(
    val color: ColorValue? = null,
    val borderColor: ColorValue? = null,
    val borderWidth: Int? = null,
    val cornerRadius: Int? = null
)

@Serializable
data class Action(
    val type: ActionType,
    val data: Map<String, String> = emptyMap()
)

enum class ActionType {
    NAVIGATE, DEEP_LINK, OPEN_URL, CUSTOM
}

enum class TextStyle { 
    DisplayLarge, DisplayMedium, DisplaySmall,
    HeadlineLarge, HeadlineMedium, HeadlineSmall,
    TitleLarge, TitleMedium, TitleSmall,
    BodyLarge, BodyMedium, BodySmall,
    LabelLarge, LabelMedium, LabelSmall
}

enum class ContentScale { FillWidth, FillHeight, Crop, Inside, Fit, FillBounds }
enum class ClipShape { CIRCLE, ROUNDED4, ROUNDED8, ROUNDED12, ROUNDED16 }
enum class CardShape { ROUNDED4, ROUNDED8, ROUNDED12, ROUNDED16 }
enum class ButtonStyle { Filled, Outlined, Text, Elevated, FilledTonal }

// Replacing Compose-specific Alignment with our own enums
enum class HorizontalAlignment { Start, Center, End }
enum class VerticalAlignment { Top, CenterVertically, Bottom }

enum class BoxAlignment { 
    TopStart, TopCenter, TopEnd,
    CenterStart, Center, CenterEnd,
    BottomStart, BottomCenter, BottomEnd
}

@Serializable
data class ErrorFallback(
    val text: TemplateString? = null,
    val iconUrl: TemplateString? = null
)

@Serializable
data class LoadingPlaceholder(
    val showProgressIndicator: Boolean = true,
    val backgroundColor: ColorValue? = null
)

/**
 * Serializable color value
 */
@Serializable
data class ColorValue(
    val red: Int,
    val green: Int,
    val blue: Int,
    val alpha: Int = 255
)

/**
 * Serializable wrapper for TextAlign
 */
enum class TextAlignValue { Start, Center, End, Justify, Left, Right }

/**
 * Serializable wrapper for TextOverflow
 */
enum class TextOverflowValue { Clip, Ellipsis, Visible }

/* Placeholder binding */
@Serializable
data class TemplateString(val raw: String) {
    fun resolve(bindings: Map<String, String>): String =
        PLACEHOLDER_REGEX.replace(raw) { m ->
            bindings[m.groupValues[1]] ?: m.value
        }

    companion object { 
        private val PLACEHOLDER_REGEX = Regex("""\{\{(.*?)\}\}""") 
    }
}

/* ───────── Screen payload ───────── */

@Serializable
data class TokenRef(val id: String, val bind: Map<String, String> = emptyMap())

@Serializable
data class ScreenPayload(val id: String, val tokens: List<TokenRef>)

/**
 * Registry for tokens that can be referenced by ID
 */
class TokenRegistry {
    private val tokens = mutableMapOf<String, Token>()

    /**
     * Register a token in the registry
     */
    fun register(token: Token) {
        tokens[token.id] = token
    }

    /**
     * Register multiple tokens in the registry
     */
    fun registerAll(tokens: List<Token>) {
        tokens.forEach { register(it) }
    }

    /**
     * Get a token by ID
     */
    fun getToken(id: String): Token? = tokens[id]

    /**
     * Check if a token with the given ID exists
     */
    fun hasToken(id: String): Boolean = tokens.containsKey(id)

    /**
     * Get all registered tokens
     */
    fun getAllTokens(): Map<String, Token> = tokens.toMap()

    /**
     * Clear all tokens from the registry
     */
    fun clear() {
        tokens.clear()
    }
}
