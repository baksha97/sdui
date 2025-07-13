package com.baksha97.sdui.shared.models

/**
 * Helper functions and extensions to make parameter creation and handling easier.
 * This file provides concise ways to create and configure common parameters used in the DSL.
 */

// Color helpers
val Int.color: ColorValue
    get() = ColorValue(
        red = (this shr 16) and 0xFF,
        green = (this shr 8) and 0xFF,
        blue = this and 0xFF
    )

val Int.colorWithAlpha: ColorValue
    get() = ColorValue(
        red = (this shr 16) and 0xFF,
        green = (this shr 8) and 0xFF,
        blue = this and 0xFF,
        alpha = (this shr 24) and 0xFF
    )

// Common colors
val ColorBuilder.red: ColorValue get() = ColorValue(255, 0, 0)
val ColorBuilder.green: ColorValue get() = ColorValue(0, 255, 0)
val ColorBuilder.blue: ColorValue get() = ColorValue(0, 0, 255)
val ColorBuilder.black: ColorValue get() = ColorValue(0, 0, 0)
val ColorBuilder.white: ColorValue get() = ColorValue(255, 255, 255)
val ColorBuilder.gray: ColorValue get() = ColorValue(128, 128, 128)
val ColorBuilder.transparent: ColorValue get() = ColorValue(0, 0, 0, 0)

// RGB color helper
fun rgb(red: Int, green: Int, blue: Int): ColorValue = ColorValue(red, green, blue)

// RGBA color helper
fun rgba(red: Int, green: Int, blue: Int, alpha: Int): ColorValue = ColorValue(red, green, blue, alpha)

// Padding helpers
fun padding(all: Int): Padding = Padding(all = all)
fun padding(horizontal: Int? = null, vertical: Int? = null): Padding = Padding(horizontal = horizontal, vertical = vertical)
fun padding(start: Int? = null, top: Int? = null, end: Int? = null, bottom: Int? = null): Padding = 
    Padding(start = start, top = top, end = end, bottom = bottom)

// Margin helpers
fun margin(all: Int): Margin = Margin(all = all)
fun margin(horizontal: Int? = null, vertical: Int? = null): Margin = Margin(horizontal = horizontal, vertical = vertical)
fun margin(start: Int? = null, top: Int? = null, end: Int? = null, bottom: Int? = null): Margin = 
    Margin(start = start, top = top, end = end, bottom = bottom)

// Background helpers
fun background(color: ColorValue): Background = Background(color = color)
fun background(
    color: ColorValue? = null,
    borderColor: ColorValue? = null,
    borderWidth: Int? = null,
    cornerRadius: Int? = null
): Background = Background(
    color = color,
    borderColor = borderColor,
    borderWidth = borderWidth,
    cornerRadius = cornerRadius
)

// Action helpers
fun action(type: ActionType, vararg data: Pair<String, String>): Action = Action(type, mapOf(*data))
fun navigateAction(route: String): Action = Action(ActionType.Navigate, mapOf("route" to route))
fun deepLinkAction(uri: String): Action = Action(ActionType.DeepLink, mapOf("uri" to uri))
fun openUrlAction(url: String): Action = Action(ActionType.OpenUrl, mapOf("url" to url))
fun customAction(vararg data: Pair<String, String>): Action = Action(ActionType.Custom, mapOf(*data))

// A11y helpers
fun a11y(role: Role, label: String): A11y = A11y(role, TemplateString(label))
fun a11y(
    role: Role,
    label: String,
    liveRegion: LiveRegion = LiveRegion.Off,
    isEnabled: Boolean = true,
    isFocusable: Boolean = true
): A11y = A11y(role, TemplateString(label), liveRegion, isEnabled, isFocusable)

// Extension functions for builders to set multiple properties at once

// ColumnBuilder extensions
fun ColumnBuilder.setPadding(all: Int) {
    padding = padding(all)
}

fun ColumnBuilder.setPadding(horizontal: Int? = null, vertical: Int? = null) {
    padding = padding(horizontal, vertical)
}

fun ColumnBuilder.setPadding(start: Int? = null, top: Int? = null, end: Int? = null, bottom: Int? = null) {
    padding = padding(start, top, end, bottom)
}

fun ColumnBuilder.setMargin(all: Int) {
    margin = margin(all)
}

fun ColumnBuilder.setMargin(horizontal: Int? = null, vertical: Int? = null) {
    margin = margin(horizontal, vertical)
}

fun ColumnBuilder.setMargin(start: Int? = null, top: Int? = null, end: Int? = null, bottom: Int? = null) {
    margin = margin(start, top, end, bottom)
}

fun ColumnBuilder.setBackground(color: ColorValue) {
    background = background(color)
}

fun ColumnBuilder.setBackground(
    color: ColorValue? = null,
    borderColor: ColorValue? = null,
    borderWidth: Int? = null,
    cornerRadius: Int? = null
) {
    background = background(color, borderColor, borderWidth, cornerRadius)
}

// RowBuilder extensions
fun RowBuilder.setPadding(all: Int) {
    padding = padding(all)
}

fun RowBuilder.setPadding(horizontal: Int? = null, vertical: Int? = null) {
    padding = padding(horizontal, vertical)
}

fun RowBuilder.setPadding(start: Int? = null, top: Int? = null, end: Int? = null, bottom: Int? = null) {
    padding = padding(start, top, end, bottom)
}

fun RowBuilder.setMargin(all: Int) {
    margin = margin(all)
}

fun RowBuilder.setMargin(horizontal: Int? = null, vertical: Int? = null) {
    margin = margin(horizontal, vertical)
}

fun RowBuilder.setMargin(start: Int? = null, top: Int? = null, end: Int? = null, bottom: Int? = null) {
    margin = margin(start, top, end, bottom)
}

fun RowBuilder.setBackground(color: ColorValue) {
    background = background(color)
}

fun RowBuilder.setBackground(
    color: ColorValue? = null,
    borderColor: ColorValue? = null,
    borderWidth: Int? = null,
    cornerRadius: Int? = null
) {
    background = background(color, borderColor, borderWidth, cornerRadius)
}

// BoxBuilder extensions
fun BoxBuilder.setPadding(all: Int) {
    padding = padding(all)
}

fun BoxBuilder.setPadding(horizontal: Int? = null, vertical: Int? = null) {
    padding = padding(horizontal, vertical)
}

fun BoxBuilder.setPadding(start: Int? = null, top: Int? = null, end: Int? = null, bottom: Int? = null) {
    padding = padding(start, top, end, bottom)
}

fun BoxBuilder.setMargin(all: Int) {
    margin = margin(all)
}

fun BoxBuilder.setMargin(horizontal: Int? = null, vertical: Int? = null) {
    margin = margin(horizontal, vertical)
}

fun BoxBuilder.setMargin(start: Int? = null, top: Int? = null, end: Int? = null, bottom: Int? = null) {
    margin = margin(start, top, end, bottom)
}

fun BoxBuilder.setBackground(color: ColorValue) {
    background = background(color)
}

fun BoxBuilder.setBackground(
    color: ColorValue? = null,
    borderColor: ColorValue? = null,
    borderWidth: Int? = null,
    cornerRadius: Int? = null
) {
    background = background(color, borderColor, borderWidth, cornerRadius)
}

// TextBuilder extensions
fun TextBuilder.setMargin(all: Int) {
    margin = margin(all)
}

fun TextBuilder.setMargin(horizontal: Int? = null, vertical: Int? = null) {
    margin = margin(horizontal, vertical)
}

fun TextBuilder.setMargin(start: Int? = null, top: Int? = null, end: Int? = null, bottom: Int? = null) {
    margin = margin(start, top, end, bottom)
}

fun TextBuilder.setColor(red: Int, green: Int, blue: Int, alpha: Int = 255) {
    color = ColorValue(red, green, blue, alpha)
}

// ButtonBuilder extensions
fun ButtonBuilder.setMargin(all: Int) {
    margin = margin(all)
}

fun ButtonBuilder.setMargin(horizontal: Int? = null, vertical: Int? = null) {
    margin = margin(horizontal, vertical)
}

fun ButtonBuilder.setMargin(start: Int? = null, top: Int? = null, end: Int? = null, bottom: Int? = null) {
    margin = margin(start, top, end, bottom)
}

fun ButtonBuilder.setOnClick(type: ActionType, vararg data: Pair<String, String>) {
    onClick = action(type, *data)
}

// DividerBuilder extensions
fun DividerBuilder.setMargin(all: Int) {
    margin = margin(all)
}

fun DividerBuilder.setMargin(horizontal: Int? = null, vertical: Int? = null) {
    margin = margin(horizontal, vertical)
}

fun DividerBuilder.setMargin(start: Int? = null, top: Int? = null, end: Int? = null, bottom: Int? = null) {
    margin = margin(start, top, end, bottom)
}

fun DividerBuilder.setColor(red: Int, green: Int, blue: Int, alpha: Int = 255) {
    color = ColorValue(red, green, blue, alpha)
}

// AsyncImageBuilder extensions
fun AsyncImageBuilder.setMargin(all: Int) {
    margin = margin(all)
}

fun AsyncImageBuilder.setMargin(horizontal: Int? = null, vertical: Int? = null) {
    margin = margin(horizontal, vertical)
}

fun AsyncImageBuilder.setMargin(start: Int? = null, top: Int? = null, end: Int? = null, bottom: Int? = null) {
    margin = margin(start, top, end, bottom)
}

fun AsyncImageBuilder.setOnClick(type: ActionType, vararg data: Pair<String, String>) {
    onClick = action(type, *data)
}

// CardBuilder extensions
fun CardBuilder.setPadding(all: Int) {
    padding = padding(all)
}

fun CardBuilder.setPadding(horizontal: Int? = null, vertical: Int? = null) {
    padding = padding(horizontal, vertical)
}

fun CardBuilder.setPadding(start: Int? = null, top: Int? = null, end: Int? = null, bottom: Int? = null) {
    padding = padding(start, top, end, bottom)
}

fun CardBuilder.setMargin(all: Int) {
    margin = margin(all)
}

fun CardBuilder.setMargin(horizontal: Int? = null, vertical: Int? = null) {
    margin = margin(horizontal, vertical)
}

fun CardBuilder.setMargin(start: Int? = null, top: Int? = null, end: Int? = null, bottom: Int? = null) {
    margin = margin(start, top, end, bottom)
}

fun CardBuilder.setBackground(color: ColorValue) {
    background = background(color)
}

fun CardBuilder.setBackground(
    color: ColorValue? = null,
    borderColor: ColorValue? = null,
    borderWidth: Int? = null,
    cornerRadius: Int? = null
) {
    background = background(color, borderColor, borderWidth, cornerRadius)
}

fun CardBuilder.setOnClick(type: ActionType, vararg data: Pair<String, String>) {
    onClick = action(type, *data)
}

// SliderBuilder extensions
fun SliderBuilder.setMargin(all: Int) {
    margin = margin(all)
}

fun SliderBuilder.setMargin(horizontal: Int? = null, vertical: Int? = null) {
    margin = margin(horizontal, vertical)
}

fun SliderBuilder.setMargin(start: Int? = null, top: Int? = null, end: Int? = null, bottom: Int? = null) {
    margin = margin(start, top, end, bottom)
}

fun SliderBuilder.setOnChange(type: ActionType, vararg data: Pair<String, String>) {
    onChange = action(type, *data)
}
