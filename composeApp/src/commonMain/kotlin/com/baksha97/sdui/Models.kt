package com.baksha97.sdui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Slider
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.jvm.JvmInline
import androidx.compose.ui.layout.ContentScale as ComposeContentScale

/* ───────── Core token hierarchy ───────── */

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
 * Base class for container tokens that hold other tokens
 */
sealed interface ContainerToken : Token {
    val children: List<Token>
}

/**
 * Base class for interactive tokens that can be clicked
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
    val alignment: Alignment.Horizontal = Alignment.Start,
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
    val alignment: Alignment.Vertical = Alignment.CenterVertically,
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
    val alignment: Alignment.Horizontal = Alignment.Start,
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
    val alignment: Alignment.Vertical = Alignment.CenterVertically,
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

/* ───────── AsyncImageToken with sizing / weight / clipping ───────── */

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

@Serializable data class Padding(
    val all: Int? = null,
    val horizontal: Int? = null,
    val vertical: Int? = null,
    val start: Int? = null,
    val top: Int? = null,
    val end: Int? = null,
    val bottom: Int? = null
)

@Serializable data class Margin(
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
 * Serializable color value that can be converted to Compose Color
 */
@Serializable
data class ColorValue(
    val red: Int,
    val green: Int,
    val blue: Int,
    val alpha: Int = 255
) {
    fun toComposeColor(): Color = Color(red, green, blue, alpha)

    companion object {
        fun fromComposeColor(color: Color): ColorValue = 
            ColorValue(
                (color.red * 255).toInt(),
                (color.green * 255).toInt(),
                (color.blue * 255).toInt(),
                (color.alpha * 255).toInt()
            )
    }
}

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
@JvmInline
value class TemplateString(val raw: String) {
    fun resolve(bindings: Map<String, Any>): String =
        PLACEHOLDER_REGEX.replace(raw) { m ->
            bindings[m.groupValues[1]]?.toString() ?: m.value
        }
    companion object { private val PLACEHOLDER_REGEX = Regex("""\{\{(.*?)\}\}""") }
}

/* ───────── Renderer ───────── */

/**
 * Renders a token with version compatibility checking
 */
@Composable
fun RenderToken(
    token: Token, 
    bindings: Map<String, Any>,
    onAction: ((Action, Map<String, Any>) -> Unit)? = null
) {
    // Version compatibility check
    if (token.version < token.minSupportedVersion) {
        RenderVersionIncompatible(token)
        return
    }

    when (token) {
        is ColumnToken -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .applyPadding(token.padding)
                    .applyMargin(token.margin)
                    .applyBackground(token.background),
                horizontalAlignment = token.alignment
            ) {
                token.children.forEach { RenderToken(it, bindings) }
            }
        }

        is RowToken -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .applyPadding(token.padding)
                    .applyMargin(token.margin)
                    .applyBackground(token.background),
                verticalAlignment = token.alignment
            ) {
                token.children.forEach { RenderToken(it, bindings) }
            }
        }

        is BoxToken -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .applyPadding(token.padding)
                    .applyMargin(token.margin)
                    .applyBackground(token.background),
                contentAlignment = when (token.contentAlignment) {
                    BoxAlignment.TopStart -> Alignment.TopStart
                    BoxAlignment.TopCenter -> Alignment.TopCenter
                    BoxAlignment.TopEnd -> Alignment.TopEnd
                    BoxAlignment.CenterStart -> Alignment.CenterStart
                    BoxAlignment.Center -> Alignment.Center
                    BoxAlignment.CenterEnd -> Alignment.CenterEnd
                    BoxAlignment.BottomStart -> Alignment.BottomStart
                    BoxAlignment.BottomCenter -> Alignment.BottomCenter
                    BoxAlignment.BottomEnd -> Alignment.BottomEnd
                }
            ) {
                token.children.forEach { RenderToken(it, bindings) }
            }
        }

        is LazyColumnToken -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .applyPadding(token.padding)
                    .applyMargin(token.margin)
                    .applyBackground(token.background),
                horizontalAlignment = token.alignment
            ) {
                items(token.children) { child ->
                    RenderToken(child, bindings)
                }
            }
        }

        is LazyRowToken -> {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .applyPadding(token.padding)
                    .applyMargin(token.margin)
                    .applyBackground(token.background),
                verticalAlignment = token.alignment
            ) {
                items(token.children) { child ->
                    RenderToken(child, bindings)
                }
            }
        }

        is CardToken -> {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .applyMargin(token.margin)
                    .let { mod ->
                        if (token.onClick != null) {
                            mod.clickable { handleAction(token.onClick, bindings, onAction) }
                        } else {
                            mod
                        }
                    },
                shape = when (token.shape) {
                    CardShape.ROUNDED4 -> RoundedCornerShape(4.dp)
                    CardShape.ROUNDED8 -> RoundedCornerShape(8.dp)
                    CardShape.ROUNDED12 -> RoundedCornerShape(12.dp)
                    CardShape.ROUNDED16 -> RoundedCornerShape(16.dp)
                },
                elevation = CardDefaults.cardElevation(defaultElevation = token.elevation.dp),
                colors = if (token.background?.color != null) {
                    CardDefaults.cardColors(containerColor = token.background.color.toComposeColor())
                } else {
                    CardDefaults.cardColors()
                }
            ) {
                Column(
                    modifier = Modifier.applyPadding(token.padding),
                    horizontalAlignment = Alignment.Start
                ) {
                    token.children.forEach { RenderToken(it, bindings, onAction) }
                }
            }
        }

        is TextToken -> {
            Text(
                text = token.text.resolve(bindings),
                style = mapTextStyle(token.style),
                color = token.color?.toComposeColor() ?: Color.Unspecified,
                maxLines = token.maxLines ?: Int.MAX_VALUE,
                overflow = when (token.overflow) {
                    TextOverflowValue.Clip -> TextOverflow.Clip
                    TextOverflowValue.Ellipsis -> TextOverflow.Ellipsis
                    TextOverflowValue.Visible -> TextOverflow.Visible
                },
                textAlign = when (token.textAlign) {
                    TextAlignValue.Start -> TextAlign.Start
                    TextAlignValue.Center -> TextAlign.Center
                    TextAlignValue.End -> TextAlign.End
                    TextAlignValue.Justify -> TextAlign.Justify
                    TextAlignValue.Left -> TextAlign.Left
                    TextAlignValue.Right -> TextAlign.Right
                    null -> null
                },
                modifier = Modifier.applyMargin(token.margin)
            )
        }

        is ButtonToken -> {
            val buttonModifier = Modifier.applyMargin(token.margin)

            when (token.style) {
                ButtonStyle.Filled -> {
                    Button(
                        onClick = { handleAction(token.onClick, bindings, onAction) },
                        enabled = token.enabled,
                        modifier = buttonModifier
                    ) {
                        Text(token.text.resolve(bindings))
                    }
                }
                ButtonStyle.Outlined -> {
                    OutlinedButton(
                        onClick = { handleAction(token.onClick, bindings, onAction) },
                        enabled = token.enabled,
                        modifier = buttonModifier
                    ) {
                        Text(token.text.resolve(bindings))
                    }
                }
                ButtonStyle.Text -> {
                    TextButton(
                        onClick = { handleAction(token.onClick, bindings, onAction) },
                        enabled = token.enabled,
                        modifier = buttonModifier
                    ) {
                        Text(token.text.resolve(bindings))
                    }
                }
                ButtonStyle.Elevated -> {
                    ElevatedButton(
                        onClick = { handleAction(token.onClick, bindings, onAction) },
                        enabled = token.enabled,
                        modifier = buttonModifier
                    ) {
                        Text(token.text.resolve(bindings))
                    }
                }
                ButtonStyle.FilledTonal -> {
                    FilledTonalButton(
                        onClick = { handleAction(token.onClick, bindings, onAction) },
                        enabled = token.enabled,
                        modifier = buttonModifier
                    ) {
                        Text(token.text.resolve(bindings))
                    }
                }
            }
        }

        is SpacerToken -> {
            Spacer(
                modifier = Modifier
                    .let { mod ->
                        if (token.width != null) mod.width(token.width.dp) else mod
                    }
                    .let { mod ->
                        if (token.height != null) mod.height(token.height.dp) else mod
                    }
            )
        }

        is DividerToken -> {
            Divider(
                thickness = token.thickness.dp,
                color = token.color?.toComposeColor() ?: MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f),
                modifier = Modifier.applyMargin(token.margin)
            )
        }

        is AsyncImageToken -> {
            val base = when {
                token.widthDp != null && token.heightDp != null ->
                    Modifier.size(token.widthDp.dp, token.heightDp.dp)
                token.layoutWeight != null ->
                    // Note: weight only works in Row/Column scope
                    Modifier.fillMaxWidth().wrapContentHeight()
                else -> Modifier.wrapContentSize()
            }

            val clipped = when (token.clip) {
                ClipShape.CIRCLE -> base.clip(CircleShape)
                ClipShape.ROUNDED4 -> base.clip(RoundedCornerShape(4.dp))
                ClipShape.ROUNDED8 -> base.clip(RoundedCornerShape(8.dp))
                ClipShape.ROUNDED12 -> base.clip(RoundedCornerShape(12.dp))
                ClipShape.ROUNDED16 -> base.clip(RoundedCornerShape(16.dp))
                null -> base
            }

            val withMargin = clipped.applyMargin(token.margin)

            val clickableModifier = if (token.onClick != null) {
                withMargin.clickable { handleAction(token.onClick, bindings, onAction) }
            } else {
                withMargin
            }

            // Use SubcomposeAsyncImage for better error handling and loading states
            SubcomposeAsyncImage(
                model = token.url.resolve(bindings),
                contentDescription = token.a11y?.label?.resolve(bindings),
                modifier = clickableModifier,
                contentScale = when (token.contentScale) {
                    ContentScale.FillWidth -> ComposeContentScale.FillWidth
                    ContentScale.FillHeight -> ComposeContentScale.FillHeight
                    ContentScale.Crop -> ComposeContentScale.Crop
                    ContentScale.Inside -> ComposeContentScale.Inside
                    ContentScale.Fit -> ComposeContentScale.Fit
                    ContentScale.FillBounds -> ComposeContentScale.FillBounds
                },
                loading = {
                    if (token.loadingPlaceholder?.showProgressIndicator == true) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    if (token.loadingPlaceholder?.backgroundColor != null) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(token.loadingPlaceholder.backgroundColor.toComposeColor())
                        )
                    }
                },
                error = {
                    if (token.errorFallback != null) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            token.errorFallback.iconUrl?.let { iconUrl ->
                                AsyncImage(
                                    model = iconUrl.resolve(bindings),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            token.errorFallback.text?.let { text ->
                                Text(
                                    text = text.resolve(bindings),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    } else {
                        // Default error state
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.LightGray.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            // Simple error indicator
                            Text(
                                text = "!",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.Gray,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            )
        }

        is SliderToken -> {
            // Use remember to maintain local state after initial render
            var sliderValue by remember { mutableStateOf(token.initialValue) }

            Slider(
                value = sliderValue,
                onValueChange = { newValue ->
                    sliderValue = newValue
                    // Trigger onChange action if provided
                    token.onChange?.let { action ->
                        // Add the current value to the action data
                        val actionWithValue = Action(
                            type = action.type,
                            data = action.data + ("value" to newValue.toString())
                        )
                        handleAction(actionWithValue, bindings, onAction)
                    }
                },
                valueRange = token.valueRange,
                steps = token.steps ?: 0,
                enabled = token.enabled,
                modifier = Modifier.applyMargin(token.margin)
            )
        }
    }
}

/**
 * Renders a fallback UI for tokens with incompatible versions
 */
@Composable
private fun RenderVersionIncompatible(token: Token) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.Red.copy(alpha = 0.1f))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Incompatible token version: ${token.id} (v${token.version} < v${token.minSupportedVersion})",
            color = Color.Red,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * Maps TextStyle enum to MaterialTheme typography
 */
@Composable
private fun mapTextStyle(style: TextStyle) = when (style) {
    TextStyle.DisplayLarge -> MaterialTheme.typography.displayLarge
    TextStyle.DisplayMedium -> MaterialTheme.typography.displayMedium
    TextStyle.DisplaySmall -> MaterialTheme.typography.displaySmall
    TextStyle.HeadlineLarge -> MaterialTheme.typography.headlineLarge
    TextStyle.HeadlineMedium -> MaterialTheme.typography.headlineMedium
    TextStyle.HeadlineSmall -> MaterialTheme.typography.headlineSmall
    TextStyle.TitleLarge -> MaterialTheme.typography.titleLarge
    TextStyle.TitleMedium -> MaterialTheme.typography.titleMedium
    TextStyle.TitleSmall -> MaterialTheme.typography.titleSmall
    TextStyle.BodyLarge -> MaterialTheme.typography.bodyLarge
    TextStyle.BodyMedium -> MaterialTheme.typography.bodyMedium
    TextStyle.BodySmall -> MaterialTheme.typography.bodySmall
    TextStyle.LabelLarge -> MaterialTheme.typography.labelLarge
    TextStyle.LabelMedium -> MaterialTheme.typography.labelMedium
    TextStyle.LabelSmall -> MaterialTheme.typography.labelSmall
}

/**
 * Handles actions from interactive tokens
 */
private fun handleAction(
    action: Action, 
    bindings: Map<String, Any>,
    onAction: ((Action, Map<String, Any>) -> Unit)?
) {
    // Call the provided action handler if available
    onAction?.invoke(action, bindings)

    // Default handling (logging)
    println("Action triggered: ${action.type} with data: ${action.data}")
}

/* Padding and Margin helpers */
private fun Modifier.applyPadding(p: Padding?): Modifier {
    if (p == null) return this

    return when {
        p.all != null -> padding(p.all.dp)
        p.horizontal != null || p.vertical != null -> 
            padding(
                horizontal = p.horizontal?.dp ?: 0.dp,
                vertical = p.vertical?.dp ?: 0.dp
            )
        else -> padding(
            start = p.start?.dp ?: 0.dp,
            top = p.top?.dp ?: 0.dp,
            end = p.end?.dp ?: 0.dp,
            bottom = p.bottom?.dp ?: 0.dp
        )
    }
}

private fun Modifier.applyMargin(m: Margin?): Modifier {
    if (m == null) return this

    // Since Compose doesn't have a direct margin concept, we use padding for margin
    return when {
        m.all != null -> padding(m.all.dp)
        m.horizontal != null || m.vertical != null -> 
            padding(
                horizontal = m.horizontal?.dp ?: 0.dp,
                vertical = m.vertical?.dp ?: 0.dp
            )
        else -> padding(
            start = m.start?.dp ?: 0.dp,
            top = m.top?.dp ?: 0.dp,
            end = m.end?.dp ?: 0.dp,
            bottom = m.bottom?.dp ?: 0.dp
        )
    }
}

private fun Modifier.applyBackground(bg: Background?): Modifier {
    if (bg == null) return this

    var modifier = this

    // Apply background color if specified
    if (bg.color != null) {
        modifier = modifier.background(bg.color.toComposeColor())
    }

    // Apply border if specified
    if (bg.borderColor != null && bg.borderWidth != null) {
        val shape = if (bg.cornerRadius != null) {
            RoundedCornerShape(bg.cornerRadius.dp)
        } else {
            RoundedCornerShape(0.dp)
        }

        modifier = modifier.border(
            width = bg.borderWidth.dp,
            color = bg.borderColor.toComposeColor(),
            shape = shape
        )
    }

    // Apply corner radius if specified
    if (bg.cornerRadius != null) {
        modifier = modifier.clip(RoundedCornerShape(bg.cornerRadius.dp))
    }

    return modifier
}

/* ───────── Token catalogue ───────── */

val promoBanner = ColumnToken(
    id = "promo_banner",
    version = 3,
    a11y = A11y(Role.BANNER, TemplateString("Promotional banner: {{title}}")),
    padding = Padding(16),
    alignment = Alignment.CenterHorizontally,
    children = listOf(
        AsyncImageToken(
            id = "promo_banner.image",
            version = 3,
            url = TemplateString("{{imageUrl}}"),
            widthDp = 320,
            heightDp = 160,
            clip = ClipShape.ROUNDED4
        ),
        TextToken(
            id = "promo_banner.title",
            version = 3,
            text = TemplateString("{{title}}"),
            style = TextStyle.HeadlineMedium
        )
    )
)

val profileCard = RowToken(
    id = "profile_card",
    version = 2,
    padding = Padding(12),
    children = listOf(
        AsyncImageToken(
            id = "profile.avatar",
            version = 2,
            url = TemplateString("{{avatarUrl}}"),
            widthDp = 56,
            heightDp = 56,
            clip = ClipShape.CIRCLE
        ),
        ColumnToken(
            id = "profile.texts",
            version = 2,
            padding = Padding(8),
            children = listOf(
                TextToken(
                    id = "profile.username",
                    version = 2,
                    text = TemplateString("{{username}}"),
                    style = TextStyle.HeadlineMedium
                ),
                TextToken(
                    id = "profile.followers",
                    version = 2,
                    text = TemplateString("{{followers}} followers"),
                    style = TextStyle.BodyMedium
                )
            )
        )
    )
)

val articleCard = RowToken(
    id = "article_card",
    version = 1,
    padding = Padding(12),
    children = listOf(
        AsyncImageToken(
            id = "article.thumb",
            version = 1,
            url = TemplateString("{{thumbUrl}}"),
            layoutWeight = 1f,
            clip = ClipShape.ROUNDED4,
            contentScale = ContentScale.Crop
        ),
        ColumnToken(
            id = "article.texts",
            version = 1,
            padding = Padding(8),
            children = listOf(
                TextToken(
                    id = "article.headline",
                    version = 1,
                    text = TemplateString("{{headline}}"),
                    style = TextStyle.BodyMedium
                )
            )
        )
    )
)

/* ───────── Screen payload ───────── */

data class TokenRef(val id: String, val bind: Map<String, Any> = emptyMap())
data class ScreenPayload(val id: String, val tokens: List<TokenRef>)

val homeScreen = ScreenPayload(
    id = "home",
    tokens = listOf(
        TokenRef(
            id = profileCard.id,
            bind = mapOf(
                "avatarUrl" to "https://picsum.photos/56",
                "username"  to "Travis",
                "followers" to 874
            )
        ),
        TokenRef(
            id = articleCard.id,
            bind = mapOf(
                "thumbUrl" to "https://picsum.photos/400/250",
                "headline" to "Server-driven UI cuts release time"
            )
        )
    )
)

/* ───────── Token Registry ───────── */

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

    companion object {
        // Default instance with predefined tokens
        val default by lazy {
            TokenRegistry().apply {
                registerAll(listOf(promoBanner, profileCard, articleCard))
                // Will register enhanced examples after they're defined
            }
        }
    }
}

/* ───────── Screen renderer ───────── */

/**
 * Renders a screen with tokens from the provided registry
 */
@Composable
fun RenderScreen(
    screen: ScreenPayload,
    registry: TokenRegistry = TokenRegistry.default,
    onMissingToken: ((String) -> Unit)? = null,
    onAction: ((Action, Map<String, Any>) -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(rememberScrollState(), Orientation.Vertical)
    ) {
        screen.tokens.forEach { ref ->
            registry.getToken(ref.id)?.let { 
                RenderToken(it, ref.bind, onAction) 
            } ?: run {
                // Missing token handling
                onMissingToken?.invoke(ref.id)

                // Render fallback UI for missing token
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color.Red.copy(alpha = 0.1f))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Missing token: ${ref.id}",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

/* ───────── Enhanced examples ───────── */

// Example of a card with button
val enhancedCard = CardToken(
    id = "enhanced_card",
    version = 1,
    padding = Padding(all = 16),
    margin = Margin(bottom = 16),
    elevation = 2,
    shape = CardShape.ROUNDED8,
    background = Background(
        color = ColorValue(240, 240, 250)
    ),
    children = listOf(
        TextToken(
            id = "enhanced_card.title",
            version = 1,
            text = TemplateString("{{title}}"),
            style = TextStyle.HeadlineSmall,
            margin = Margin(bottom = 8)
        ),
        TextToken(
            id = "enhanced_card.description",
            version = 1,
            text = TemplateString("{{description}}"),
            style = TextStyle.BodyMedium,
            margin = Margin(bottom = 16)
        ),
        ButtonToken(
            id = "enhanced_card.button",
            version = 1,
            text = TemplateString("{{buttonText}}"),
            style = ButtonStyle.Filled,
            a11y = A11y(
                role = Role.BUTTON,
                label = TemplateString("{{buttonText}}")
            ),
            onClick = Action(
                type = ActionType.CUSTOM,
                data = mapOf("action" to "card_button_clicked")
            )
        )
    )
)

// Example of a form with multiple input types
val formExample = BoxToken(
    id = "form_example",
    version = 1,
    padding = Padding(all = 16),
    background = Background(
        color = ColorValue(250, 250, 250),
        borderColor = ColorValue(200, 200, 200),
        borderWidth = 1,
        cornerRadius = 8
    ),
    children = listOf(
        ColumnToken(
            id = "form_example.content",
            version = 1,
            padding = Padding(all = 0),
            children = listOf(
                TextToken(
                    id = "form_example.title",
                    version = 1,
                    text = TemplateString("{{formTitle}}"),
                    style = TextStyle.TitleLarge,
                    margin = Margin(top = 8, start = 8, end = 8, bottom = 16)
                ),
                DividerToken(
                    id = "form_example.divider1",
                    version = 1,
                    margin = Margin(vertical = 8)
                ),
                TextToken(
                    id = "form_example.description",
                    version = 1,
                    text = TemplateString("{{formDescription}}"),
                    style = TextStyle.BodyMedium,
                    margin = Margin(start = 8, end = 8, bottom = 16)
                ),
                SpacerToken(
                    id = "form_example.spacer",
                    version = 1,
                    height = 16
                ),
                RowToken(
                    id = "form_example.buttons",
                    version = 1,
                    alignment = Alignment.CenterVertically,
                    children = listOf(
                        ButtonToken(
                            id = "form_example.cancel",
                            version = 1,
                            text = TemplateString("Cancel"),
                            style = ButtonStyle.Text,
                            onClick = Action(
                                type = ActionType.CUSTOM,
                                data = mapOf("action" to "form_cancel")
                            )
                        ),
                        SpacerToken(
                            id = "form_example.button_spacer",
                            version = 1,
                            width = 8
                        ),
                        ButtonToken(
                            id = "form_example.submit",
                            version = 1,
                            text = TemplateString("Submit"),
                            style = ButtonStyle.Filled,
                            onClick = Action(
                                type = ActionType.CUSTOM,
                                data = mapOf("action" to "form_submit")
                            )
                        )
                    )
                )
            )
        )
    )
)

// Example of a list using LazyColumn
val lazyListExample = LazyColumnToken(
    id = "lazy_list_example",
    version = 1,
    padding = Padding(vertical = 8),
    children = List(5) { index ->
        RowToken(
            id = "lazy_list_example.item_$index",
            version = 1,
            padding = Padding(all = 8),
            margin = Margin(
                bottom = 8,
                horizontal = 16
            ),
            background = Background(
                color = ColorValue(255, 255, 255),
                borderColor = ColorValue(230, 230, 230),
                borderWidth = 1,
                cornerRadius = 4
            ),
            children = listOf(
                AsyncImageToken(
                    id = "lazy_list_example.item_$index.image",
                    version = 1,
                    url = TemplateString("https://picsum.photos/50/50?random=$index"),
                    widthDp = 50,
                    heightDp = 50,
                    clip = ClipShape.ROUNDED4,
                    margin = Margin(end = 16),
                    errorFallback = ErrorFallback(
                        text = TemplateString("Failed to load image")
                    )
                ),
                ColumnToken(
                    id = "lazy_list_example.item_$index.content",
                    version = 1,
                    children = listOf(
                        TextToken(
                            id = "lazy_list_example.item_$index.title",
                            version = 1,
                            text = TemplateString("Item ${index + 1}"),
                            style = TextStyle.TitleMedium
                        ),
                        TextToken(
                            id = "lazy_list_example.item_$index.subtitle",
                            version = 1,
                            text = TemplateString("This is item number ${index + 1}"),
                            style = TextStyle.BodySmall
                        )
                    )
                )
            )
        )
    }
)

// Example of a slider with local state management
val sliderExample = CardToken(
    id = "slider_example",
    version = 1,
    padding = Padding(all = 16),
    margin = Margin(all = 16),
    background = Background(
        color = ColorValue(255, 255, 255),
        cornerRadius = 8
    ),
    children = listOf(
        TextToken(
            id = "slider_example.title",
            version = 1,
            text = TemplateString("{{sliderTitle}}"),
            style = TextStyle.HeadlineSmall,
            margin = Margin(bottom = 16)
        ),
        TextToken(
            id = "slider_example.description",
            version = 1,
            text = TemplateString("{{sliderDescription}}"),
            style = TextStyle.BodyMedium,
            margin = Margin(bottom = 24)
        ),
        SliderToken(
            id = "slider_example.slider",
            version = 1,
            initialValue = 0.5f,
            valueRange = 0f..1f,
            steps = 10,
            margin = Margin(vertical = 8),
            a11y = A11y(
                role = Role.SLIDER,
                label = TemplateString("Adjust value")
            ),
            onChange = Action(
                type = ActionType.CUSTOM,
                data = mapOf("action" to "slider_value_changed")
            )
        ),
        TextToken(
            id = "slider_example.note",
            version = 1,
            text = TemplateString("The slider maintains its state locally after the initial render."),
            style = TextStyle.BodySmall,
            color = ColorValue(100, 100, 100),
            margin = Margin(top = 16)
        )
    )
)

// Register enhanced examples
val enhancedExamples = listOf(enhancedCard, formExample, lazyListExample, sliderExample)

// Enhanced screen payload with registry initialization
val enhancedScreen = ScreenPayload(
    id = "enhanced_home",
    tokens = listOf(
        TokenRef(
            id = enhancedCard.id,
            bind = mapOf(
                "title" to "Enhanced Card Example",
                "description" to "This card demonstrates the new CardToken with styling and a button.",
                "buttonText" to "Learn More"
            )
        ),
        TokenRef(
            id = sliderExample.id,
            bind = mapOf(
                "sliderTitle" to "Local State Management",
                "sliderDescription" to "This slider demonstrates local state management. Move the slider and it will maintain its position even though the server doesn't know about the change."
            )
        ),
        TokenRef(
            id = formExample.id,
            bind = mapOf(
                "formTitle" to "Contact Form",
                "formDescription" to "Fill out this form to get in touch with us."
            )
        ),
        TokenRef(
            id = lazyListExample.id,
            bind = emptyMap<String, Any>()
        )
    )
)

// Register enhanced examples in the default registry
val enhancedRegistry = TokenRegistry().apply {
    registerAll(listOf(promoBanner, profileCard, articleCard))
    registerAll(enhancedExamples)
}

/* ───────── Preview ───────── */

@Preview
@Composable
fun HomeScreenPreview() {
    MaterialTheme { Surface { RenderScreen(homeScreen) } }
}

@Preview
@Composable
fun EnhancedScreenPreview() {
    MaterialTheme { Surface { RenderScreen(enhancedScreen, enhancedRegistry) } }
}
