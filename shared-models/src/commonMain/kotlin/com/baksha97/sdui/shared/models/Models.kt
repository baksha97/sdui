package com.baksha97.sdui.shared.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

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
    val shape: CardShape = CardShape.Rounded8,
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
 * 
 * @param id Unique identifier for the token
 * @param version Version of the token
 * @param a11y Accessibility properties
 * @param initialValue Initial value of the slider (between 0f and 1f)
 * @param valueRange Range of values for the slider (default is 0f to 1f)
 * @param steps Number of discrete steps (null for continuous slider)
 * @param enabled Whether the slider is enabled
 * @param margin Margin around the slider
 * @param onChange Action to trigger when the slider value changes
 */
@Serializable
data class SliderToken(
    override val id: String,
    override val version: Int,
    override val a11y: A11y? = null,
    val initialValue: Float = 0f,
    val valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
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
    val liveRegion: LiveRegion = LiveRegion.Off,
    val isEnabled: Boolean = true,
    val isFocusable: Boolean = true
)

@Serializable
sealed class Role {
    @Serializable
    @SerialName("Banner")
    data object Banner : Role()

    @Serializable
    @SerialName("Image")
    data object Image : Role()

    @Serializable
    @SerialName("Button")
    data object Button : Role()

    @Serializable
    @SerialName("Checkbox")
    data object Checkbox : Role()

    @Serializable
    @SerialName("Header")
    data object Header : Role()

    @Serializable
    @SerialName("Link")
    data object Link : Role()

    @Serializable
    @SerialName("Switch")
    data object Switch : Role()

    @Serializable
    @SerialName("TextField")
    data object TextField : Role()

    @Serializable
    @SerialName("Slider")
    data object Slider : Role()

    @Serializable
    @SerialName("ProgressBar")
    data object ProgressBar : Role()

    @Serializable
    @SerialName("RadioButton")
    data object RadioButton : Role()

    @Serializable
    @SerialName("None")
    data object None : Role()
}

@Serializable
sealed class LiveRegion {
    @Serializable
    @SerialName("Off")
    data object Off : LiveRegion()

    @Serializable
    @SerialName("Polite")
    data object Polite : LiveRegion()

    @Serializable
    @SerialName("Assertive")
    data object Assertive : LiveRegion()
}

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

@Serializable
sealed class ActionType {
    @Serializable
    @SerialName("Navigate")
    data object Navigate : ActionType()

    @Serializable
    @SerialName("DeepLink")
    data object DeepLink : ActionType()

    @Serializable
    @SerialName("OpenUrl")
    data object OpenUrl : ActionType()

    @Serializable
    @SerialName("Custom")
    data object Custom : ActionType()
}

@Serializable
sealed class TextStyle {
    @Serializable
    @SerialName("DisplayLarge")
    data object DisplayLarge : TextStyle()

    @Serializable
    @SerialName("DisplayMedium")
    data object DisplayMedium : TextStyle()

    @Serializable
    @SerialName("DisplaySmall")
    data object DisplaySmall : TextStyle()

    @Serializable
    @SerialName("HeadlineLarge")
    data object HeadlineLarge : TextStyle()

    @Serializable
    @SerialName("HeadlineMedium")
    data object HeadlineMedium : TextStyle()

    @Serializable
    @SerialName("HeadlineSmall")
    data object HeadlineSmall : TextStyle()

    @Serializable
    @SerialName("TitleLarge")
    data object TitleLarge : TextStyle()

    @Serializable
    @SerialName("TitleMedium")
    data object TitleMedium : TextStyle()

    @Serializable
    @SerialName("TitleSmall")
    data object TitleSmall : TextStyle()

    @Serializable
    @SerialName("BodyLarge")
    data object BodyLarge : TextStyle()

    @Serializable
    @SerialName("BodyMedium")
    data object BodyMedium : TextStyle()

    @Serializable
    @SerialName("BodySmall")
    data object BodySmall : TextStyle()

    @Serializable
    @SerialName("LabelLarge")
    data object LabelLarge : TextStyle()

    @Serializable
    @SerialName("LabelMedium")
    data object LabelMedium : TextStyle()

    @Serializable
    @SerialName("LabelSmall")
    data object LabelSmall : TextStyle()
}

@Serializable
sealed class ContentScale {
    @Serializable
    @SerialName("FillWidth")
    data object FillWidth : ContentScale()

    @Serializable
    @SerialName("FillHeight")
    data object FillHeight : ContentScale()

    @Serializable
    @SerialName("Crop")
    data object Crop : ContentScale()

    @Serializable
    @SerialName("Inside")
    data object Inside : ContentScale()

    @Serializable
    @SerialName("Fit")
    data object Fit : ContentScale()

    @Serializable
    @SerialName("FillBounds")
    data object FillBounds : ContentScale()
}
@Serializable
sealed class ClipShape {
    @Serializable
    @SerialName("Circle")
    data object Circle : ClipShape()

    @Serializable
    @SerialName("Rounded4")
    data object Rounded4 : ClipShape()

    @Serializable
    @SerialName("Rounded8")
    data object Rounded8 : ClipShape()

    @Serializable
    @SerialName("Rounded12")
    data object Rounded12 : ClipShape()

    @Serializable
    @SerialName("Rounded16")
    data object Rounded16 : ClipShape()
}
@Serializable
sealed class CardShape {
    @Serializable
    @SerialName("Rounded4")
    data object Rounded4 : CardShape()

    @Serializable
    @SerialName("Rounded8")
    data object Rounded8 : CardShape()

    @Serializable
    @SerialName("Rounded12")
    data object Rounded12 : CardShape()

    @Serializable
    @SerialName("Rounded16")
    data object Rounded16 : CardShape()
}
@Serializable
sealed class ButtonStyle {
    @Serializable
    @SerialName("Filled")
    data object Filled : ButtonStyle()

    @Serializable
    @SerialName("Outlined")
    data object Outlined : ButtonStyle()

    @Serializable
    @SerialName("Text")
    data object Text : ButtonStyle()

    @Serializable
    @SerialName("Elevated")
    data object Elevated : ButtonStyle()

    @Serializable
    @SerialName("FilledTonal")
    data object FilledTonal : ButtonStyle()
}

// Alignment enums
@Serializable
sealed class HorizontalAlignment {
    @Serializable
    @SerialName("Start")
    data object Start : HorizontalAlignment()

    @Serializable
    @SerialName("Center")
    data object Center : HorizontalAlignment()

    @Serializable
    @SerialName("End")
    data object End : HorizontalAlignment()
}
@Serializable
sealed class VerticalAlignment {
    @Serializable
    @SerialName("Top")
    data object Top : VerticalAlignment()

    @Serializable
    @SerialName("CenterVertically")
    data object CenterVertically : VerticalAlignment()

    @Serializable
    @SerialName("Bottom")
    data object Bottom : VerticalAlignment()
}

@Serializable
sealed class BoxAlignment {
    @Serializable
    @SerialName("TopStart")
    data object TopStart : BoxAlignment()

    @Serializable
    @SerialName("TopCenter")
    data object TopCenter : BoxAlignment()

    @Serializable
    @SerialName("TopEnd")
    data object TopEnd : BoxAlignment()

    @Serializable
    @SerialName("CenterStart")
    data object CenterStart : BoxAlignment()

    @Serializable
    @SerialName("Center")
    data object Center : BoxAlignment()

    @Serializable
    @SerialName("CenterEnd")
    data object CenterEnd : BoxAlignment()

    @Serializable
    @SerialName("BottomStart")
    data object BottomStart : BoxAlignment()

    @Serializable
    @SerialName("BottomCenter")
    data object BottomCenter : BoxAlignment()

    @Serializable
    @SerialName("BottomEnd")
    data object BottomEnd : BoxAlignment()
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
@Serializable
sealed class TextAlignValue {
    @Serializable
    @SerialName("Start")
    data object Start : TextAlignValue()

    @Serializable
    @SerialName("Center")
    data object Center : TextAlignValue()

    @Serializable
    @SerialName("End")
    data object End : TextAlignValue()

    @Serializable
    @SerialName("Justify")
    data object Justify : TextAlignValue()

    @Serializable
    @SerialName("Left")
    data object Left : TextAlignValue()

    @Serializable
    @SerialName("Right")
    data object Right : TextAlignValue()
}

/**
 * Serializable wrapper for TextOverflow
 */
@Serializable
sealed class TextOverflowValue {
    @Serializable
    @SerialName("Clip")
    data object Clip : TextOverflowValue()

    @Serializable
    @SerialName("Ellipsis")
    data object Ellipsis : TextOverflowValue()

    @Serializable
    @SerialName("Visible")
    data object Visible : TextOverflowValue()
}

/* Placeholder binding */
@Serializable
data class TemplateString(val raw: String) {
    fun resolve(bindings: Map<String, Any>): String =
        PLACEHOLDER_REGEX.replace(raw) { m ->
            bindings[m.groupValues[1]]?.toString() ?: m.value
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

    /**
     * Validate that all token references in a screen payload are registered
     * @param screenPayload The screen payload to validate
     * @return List of missing token IDs, empty if all tokens are registered
     */
    fun validateScreenPayload(screenPayload: ScreenPayload): List<String> {
        val missingTokens = mutableListOf<String>()

        fun validateTokenRefs(tokenRefs: List<TokenRef>) {
            tokenRefs.forEach { tokenRef ->
                if (!hasToken(tokenRef.id)) {
                    missingTokens.add(tokenRef.id)
                } else {
                    // Recursively validate child tokens
                    val token = getToken(tokenRef.id)
                    when (token) {
                        is ColumnToken -> validateTokenRefs(token.children.map { TokenRef(it.id) })
                        is RowToken -> validateTokenRefs(token.children.map { TokenRef(it.id) })
                        is BoxToken -> validateTokenRefs(token.children.map { TokenRef(it.id) })
                        is CardToken -> validateTokenRefs(token.children.map { TokenRef(it.id) })
                        is LazyColumnToken -> validateTokenRefs(token.children.map { TokenRef(it.id) })
                        is LazyRowToken -> validateTokenRefs(token.children.map { TokenRef(it.id) })
                        is TextToken, is ButtonToken, is SpacerToken, is DividerToken, is SliderToken, is AsyncImageToken -> {
                            // These tokens don't have children, no further validation needed
                        }
                        null -> {
                            // This shouldn't happen since we checked hasToken above, but handle gracefully
                        }
                    }
                }
            }
        }

        validateTokenRefs(screenPayload.tokens)
        return missingTokens.distinct()
    }

    /**
     * Validate that all tokens in the registry are properly formed
     * @return List of validation errors, empty if all tokens are valid
     */
    fun validateRegistry(): List<String> {
        val errors = mutableListOf<String>()

        tokens.values.forEach { token ->
            // Check for empty or invalid IDs
            if (token.id.isBlank()) {
                errors.add("Token has empty or blank ID")
            }

            // Check for duplicate IDs (shouldn't happen with map, but good to verify)
            val duplicateCount = tokens.values.count { it.id == token.id }
            if (duplicateCount > 1) {
                errors.add("Duplicate token ID found: ${token.id}")
            }

            // Validate token-specific properties
            errors.addAll(validateTokenProperties(token))

            // Validate child token references exist
            when (token) {
                is ColumnToken -> {
                    token.children.forEach { child ->
                        if (!hasToken(child.id)) {
                            errors.add("Token '${token.id}' references missing child token '${child.id}'")
                        }
                    }
                }
                is RowToken -> {
                    token.children.forEach { child ->
                        if (!hasToken(child.id)) {
                            errors.add("Token '${token.id}' references missing child token '${child.id}'")
                        }
                    }
                }
                is BoxToken -> {
                    token.children.forEach { child ->
                        if (!hasToken(child.id)) {
                            errors.add("Token '${token.id}' references missing child token '${child.id}'")
                        }
                    }
                }
                is CardToken -> {
                    token.children.forEach { child ->
                        if (!hasToken(child.id)) {
                            errors.add("Token '${token.id}' references missing child token '${child.id}'")
                        }
                    }
                }
                is LazyColumnToken -> {
                    token.children.forEach { child ->
                        if (!hasToken(child.id)) {
                            errors.add("Token '${token.id}' references missing child token '${child.id}'")
                        }
                    }
                }
                is LazyRowToken -> {
                    token.children.forEach { child ->
                        if (!hasToken(child.id)) {
                            errors.add("Token '${token.id}' references missing child token '${child.id}'")
                        }
                    }
                }
                is TextToken, is ButtonToken, is SpacerToken, is DividerToken, is SliderToken, is AsyncImageToken -> {
                    // These tokens don't have children, no validation needed
                }
            }
        }

        return errors
    }

    /**
     * Validate token-specific properties for enhanced schema validation
     * @param token The token to validate
     * @return List of validation errors for the token's properties
     */
    private fun validateTokenProperties(token: Token): List<String> {
        val errors = mutableListOf<String>()

        // Validate version is positive
        if (token.version <= 0) {
            errors.add("Token '${token.id}' has invalid version: ${token.version}")
        }

        // Validate common properties
        token.a11y?.let { a11y ->
            errors.addAll(validateA11yProperties(token.id, a11y))
        }

        // Validate token-specific properties
        when (token) {
            is TextToken -> {
                errors.addAll(validateTextTokenProperties(token))
            }
            is ButtonToken -> {
                errors.addAll(validateButtonTokenProperties(token))
            }
            is AsyncImageToken -> {
                errors.addAll(validateAsyncImageTokenProperties(token))
            }
            is SliderToken -> {
                errors.addAll(validateSliderTokenProperties(token))
            }
            is SpacerToken -> {
                errors.addAll(validateSpacerTokenProperties(token))
            }
            // Container tokens
            is ColumnToken, is RowToken, is BoxToken, is CardToken, is LazyColumnToken, is LazyRowToken -> {
                // Container-specific validation handled in child reference validation
            }
            is DividerToken -> {
                errors.addAll(validateDividerTokenProperties(token))
            }
        }

        return errors
    }

    /**
     * Validate A11y properties
     */
    private fun validateA11yProperties(tokenId: String, a11y: A11y): List<String> {
        val errors = mutableListOf<String>()

        if (a11y.label.raw.isBlank()) {
            errors.add("Token '$tokenId' has empty accessibility label")
        }

        return errors
    }

    /**
     * Validate TextToken properties
     */
    private fun validateTextTokenProperties(token: TextToken): List<String> {
        val errors = mutableListOf<String>()

        if (token.text.raw.isBlank()) {
            errors.add("TextToken '${token.id}' has empty text content")
        }

        token.color?.let { color ->
            errors.addAll(validateColorValue(token.id, color))
        }

        token.maxLines?.let { maxLines ->
            if (maxLines <= 0) {
                errors.add("TextToken '${token.id}' has invalid maxLines: $maxLines")
            }
        }

        return errors
    }

    /**
     * Validate ButtonToken properties
     */
    private fun validateButtonTokenProperties(token: ButtonToken): List<String> {
        val errors = mutableListOf<String>()

        if (token.text.raw.isBlank()) {
            errors.add("ButtonToken '${token.id}' has empty text content")
        }

        errors.addAll(validateActionProperties(token.id, token.onClick))

        return errors
    }

    /**
     * Validate AsyncImageToken properties
     */
    private fun validateAsyncImageTokenProperties(token: AsyncImageToken): List<String> {
        val errors = mutableListOf<String>()

        if (token.url.raw.isBlank()) {
            errors.add("AsyncImageToken '${token.id}' has empty URL")
        }

        token.widthDp?.let { width ->
            if (width <= 0) {
                errors.add("AsyncImageToken '${token.id}' has invalid width: $width")
            }
        }

        token.heightDp?.let { height ->
            if (height <= 0) {
                errors.add("AsyncImageToken '${token.id}' has invalid height: $height")
            }
        }

        token.layoutWeight?.let { weight ->
            if (weight < 0) {
                errors.add("AsyncImageToken '${token.id}' has invalid layout weight: $weight")
            }
        }

        token.onClick?.let { action ->
            errors.addAll(validateActionProperties(token.id, action))
        }

        return errors
    }

    /**
     * Validate SliderToken properties
     */
    private fun validateSliderTokenProperties(token: SliderToken): List<String> {
        val errors = mutableListOf<String>()

        if (token.initialValue < token.valueRange.start || token.initialValue > token.valueRange.endInclusive) {
            errors.add("SliderToken '${token.id}' initial value ${token.initialValue} is outside range ${token.valueRange}")
        }

        if (token.valueRange.start >= token.valueRange.endInclusive) {
            errors.add("SliderToken '${token.id}' has invalid value range: ${token.valueRange}")
        }

        token.steps?.let { steps ->
            if (steps <= 0) {
                errors.add("SliderToken '${token.id}' has invalid steps: $steps")
            }
        }

        token.onChange?.let { action ->
            errors.addAll(validateActionProperties(token.id, action))
        }

        return errors
    }

    /**
     * Validate SpacerToken properties
     */
    private fun validateSpacerTokenProperties(token: SpacerToken): List<String> {
        val errors = mutableListOf<String>()

        token.width?.let { width ->
            if (width < 0) {
                errors.add("SpacerToken '${token.id}' has invalid width: $width")
            }
        }

        token.height?.let { height ->
            if (height < 0) {
                errors.add("SpacerToken '${token.id}' has invalid height: $height")
            }
        }

        return errors
    }

    /**
     * Validate DividerToken properties
     */
    private fun validateDividerTokenProperties(token: DividerToken): List<String> {
        val errors = mutableListOf<String>()

        if (token.thickness <= 0) {
            errors.add("DividerToken '${token.id}' has invalid thickness: ${token.thickness}")
        }

        token.color?.let { color ->
            errors.addAll(validateColorValue(token.id, color))
        }

        return errors
    }

    /**
     * Validate ColorValue properties
     */
    private fun validateColorValue(tokenId: String, color: ColorValue): List<String> {
        val errors = mutableListOf<String>()

        if (color.red < 0 || color.red > 255) {
            errors.add("Token '$tokenId' has invalid red color value: ${color.red}")
        }

        if (color.green < 0 || color.green > 255) {
            errors.add("Token '$tokenId' has invalid green color value: ${color.green}")
        }

        if (color.blue < 0 || color.blue > 255) {
            errors.add("Token '$tokenId' has invalid blue color value: ${color.blue}")
        }

        if (color.alpha < 0 || color.alpha > 255) {
            errors.add("Token '$tokenId' has invalid alpha color value: ${color.alpha}")
        }

        return errors
    }

    /**
     * Validate Action properties
     */
    private fun validateActionProperties(tokenId: String, action: Action): List<String> {
        val errors = mutableListOf<String>()

        when (action.type) {
            ActionType.Navigate -> {
                if (!action.data.containsKey("target")) {
                    errors.add("Token '$tokenId' Navigate action missing required 'target' data")
                }
            }
            ActionType.DeepLink -> {
                if (!action.data.containsKey("url")) {
                    errors.add("Token '$tokenId' DeepLink action missing required 'url' data")
                }
            }
            ActionType.OpenUrl -> {
                if (!action.data.containsKey("url")) {
                    errors.add("Token '$tokenId' OpenUrl action missing required 'url' data")
                }
            }
            ActionType.Custom -> {
                // Custom actions can have any data, no specific validation needed
            }
        }

        return errors
    }

    /**
     * Register a token with validation
     * @param token The token to register
     * @throws IllegalArgumentException if token ID is invalid
     */
    fun registerWithValidation(token: Token) {
        if (token.id.isBlank()) {
            throw IllegalArgumentException("Token ID cannot be empty or blank")
        }

        if (hasToken(token.id)) {
            println("Warning: Overwriting existing token with ID '${token.id}'")
        }

        register(token)
    }

    /**
     * Get registration statistics
     * @return Map containing registration statistics
     */
    fun getRegistrationStats(): Map<String, Any> {
        val tokensByType = tokens.values.groupBy { it::class.simpleName }
        return mapOf(
            "totalTokens" to tokens.size,
            "tokensByType" to tokensByType.mapValues { it.value.size },
            "tokenIds" to tokens.keys.sorted()
        )
    }
}
