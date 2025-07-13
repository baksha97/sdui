package com.baksha97.sdui.dsl

import com.baksha97.sdui.shared.models.*

// Import constants from shared-models
// CardShape constants
val ROUNDED4 = CardShape.Rounded4
val ROUNDED8 = CardShape.Rounded8
val ROUNDED12 = CardShape.Rounded12
val ROUNDED16 = CardShape.Rounded16

// ClipShape constants
val CIRCLE = ClipShape.Circle
val CLIP_ROUNDED4 = ClipShape.Rounded4
val CLIP_ROUNDED8 = ClipShape.Rounded8
val CLIP_ROUNDED12 = ClipShape.Rounded12
val CLIP_ROUNDED16 = ClipShape.Rounded16

// ActionType constants
val NAVIGATE = ActionType.Navigate
val DEEP_LINK = ActionType.DeepLink
val OPEN_URL = ActionType.OpenUrl
val CUSTOM = ActionType.Custom

// Role constants
val BANNER = Role.Banner
val IMAGE = Role.Image
val BUTTON = Role.Button
val CHECKBOX = Role.Checkbox
val HEADER = Role.Header
val LINK = Role.Link
val SWITCH = Role.Switch
val TEXT_FIELD = Role.TextField
val SLIDER = Role.Slider
val PROGRESS_BAR = Role.ProgressBar
val RADIO_BUTTON = Role.RadioButton
val NONE = Role.None

// LiveRegion constants
val OFF = LiveRegion.Off
val POLITE = LiveRegion.Polite
val ASSERTIVE = LiveRegion.Assertive

// This file is intentionally left mostly empty to avoid redeclaration errors.
// The dsl-cli module now uses its own definitions of the types, but imports
// the constants from the shared-models module.
