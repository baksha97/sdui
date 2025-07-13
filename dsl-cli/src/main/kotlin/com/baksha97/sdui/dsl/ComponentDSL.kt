package com.baksha97.sdui.dsl

/**
 * Type-safe DSL for prebuilt components.
 * This file contains functions for creating prebuilt components with type safety.
 */

/**
 * Creates a profile card component.
 *
 * @param id Optional ID for the component. If not provided, an ID will be auto-generated.
 * @param avatarUrl URL of the avatar image.
 * @param username Username to display.
 * @param followers Number of followers to display.
 * @param version Version of the component.
 * @param init Optional initialization block for additional customization.
 * @return A RowToken representing the profile card.
 */
fun profileCard(
    id: String? = null,
    avatarUrl: String,
    username: String,
    followers: Int,
    version: Int = 2,
    init: RowBuilder.() -> Unit = {}
): RowToken {
    return row(id ?: "profile_card") {
        this.version = version
        padding {
            all = 12
        }

        asyncImage {
            url(avatarUrl)
            widthDp = 56
            heightDp = 56
            clip = ClipShape.CIRCLE
        }

        column {
            padding {
                all = 8
            }

            text {
                text(username)
                style = TextStyle.HeadlineMedium
            }

            text {
                text("$followers followers")
                style = TextStyle.BodyMedium
            }
        }

        init()
    }
}

/**
 * Creates an article card component.
 *
 * @param id Optional ID for the component. If not provided, an ID will be auto-generated.
 * @param thumbUrl URL of the thumbnail image.
 * @param headline Headline text to display.
 * @param version Version of the component.
 * @param init Optional initialization block for additional customization.
 * @return A RowToken representing the article card.
 */
fun articleCard(
    id: String? = null,
    thumbUrl: String,
    headline: String,
    version: Int = 1,
    init: RowBuilder.() -> Unit = {}
): RowToken {
    return row(id ?: "article_card") {
        this.version = version
        padding {
            all = 12
        }

        asyncImage {
            url(thumbUrl)
            layoutWeight = 1f
            clip = ClipShape.ROUNDED4
            contentScale = ContentScale.Crop
        }

        column {
            padding {
                all = 8
            }

            text {
                text(headline)
                style = TextStyle.BodyMedium
            }
        }

        init()
    }
}

/**
 * Creates an enhanced card component.
 *
 * @param id Optional ID for the component. If not provided, an ID will be auto-generated.
 * @param title Title text to display.
 * @param description Description text to display.
 * @param buttonText Text for the button.
 * @param onButtonClick Action to perform when the button is clicked.
 * @param version Version of the component.
 * @param init Optional initialization block for additional customization.
 * @return A CardToken representing the enhanced card.
 */
fun enhancedCard(
    id: String? = null,
    title: String,
    description: String,
    buttonText: String,
    onButtonClick: Pair<ActionType, Map<String, String>> = ActionType.CUSTOM to mapOf("action" to "card_button_clicked"),
    version: Int = 1,
    init: CardBuilder.() -> Unit = {}
): CardToken {
    return card(id ?: "enhanced_card") {
        this.version = version
        padding {
            all = 16
        }
        margin {
            bottom = 16
        }
        elevation = 2
        shape = CardShape.ROUNDED8
        background {
            color {
                red = 240
                green = 240
                blue = 100
            }
        }

        text {
            text(title)
            style = TextStyle.HeadlineSmall
            margin {
                bottom = 8
            }
        }

        text {
            text(description)
            style = TextStyle.BodyMedium
            margin {
                bottom = 16
            }
        }

        button {
            text(buttonText)
            style = ButtonStyle.Filled
            a11y {
                role = Role.BUTTON
                label(buttonText)
            }
            onClick {
                type = onButtonClick.first
                data = onButtonClick.second
            }
        }

        init()
    }
}

/**
 * Creates a form example component.
 *
 * @param id Optional ID for the component. If not provided, an ID will be auto-generated.
 * @param formTitle Title text for the form.
 * @param formDescription Description text for the form.
 * @param onCancelClick Action to perform when the cancel button is clicked.
 * @param onSubmitClick Action to perform when the submit button is clicked.
 * @param version Version of the component.
 * @param init Optional initialization block for additional customization.
 * @return A BoxToken representing the form example.
 */
fun formExample(
    id: String? = null,
    formTitle: String,
    formDescription: String,
    onCancelClick: Pair<ActionType, Map<String, String>> = ActionType.CUSTOM to mapOf("action" to "form_cancel"),
    onSubmitClick: Pair<ActionType, Map<String, String>> = ActionType.CUSTOM to mapOf("action" to "form_submit"),
    version: Int = 1,
    init: BoxBuilder.() -> Unit = {}
): BoxToken {
    return box(id ?: "form_example") {
        this.version = version
        padding {
            all = 16
        }
        background {
            color {
                red = 250
                green = 250
                blue = 250
            }
            borderColor {
                red = 200
                green = 200
                blue = 100
            }
            borderWidth = 1
            cornerRadius = 8
        }

        column {
            text {
                text(formTitle)
                style = TextStyle.TitleLarge
                margin {
                    top = 8
                    start = 8
                    end = 8
                    bottom = 16
                }
            }

            divider {
                margin {
                    vertical = 8
                }
            }

            text {
                text(formDescription)
                style = TextStyle.BodyMedium
                margin {
                    start = 8
                    end = 8
                    bottom = 16
                }
            }

            spacer {
                height = 16
            }

            row {
                alignment = VerticalAlignment.CenterVertically

                button {
                    text("Cancel")
                    style = ButtonStyle.Text
                    onClick {
                        type = onCancelClick.first
                        data = onCancelClick.second
                    }
                }

                spacer {
                    width = 8
                }

                button {
                    text("Submit")
                    style = ButtonStyle.Filled
                    onClick {
                        type = onSubmitClick.first
                        data = onSubmitClick.second
                    }
                }
            }
        }

        init()
    }
}

/**
 * Creates a slider example component.
 *
 * @param id Optional ID for the component. If not provided, an ID will be auto-generated.
 * @param sliderTitle Title text for the slider.
 * @param sliderDescription Description text for the slider.
 * @param initialValue Initial value for the slider.
 * @param rangeStart Start value for the slider range.
 * @param rangeEnd End value for the slider range.
 * @param steps Number of steps for the slider.
 * @param onSliderChange Action to perform when the slider value changes.
 * @param version Version of the component.
 * @param init Optional initialization block for additional customization.
 * @return A CardToken representing the slider example.
 */
fun sliderExample(
    id: String? = null,
    sliderTitle: String,
    sliderDescription: String,
    initialValue: Float = 0.5f,
    rangeStart: Float = 0f,
    rangeEnd: Float = 1f,
    steps: Int = 10,
    onSliderChange: Pair<ActionType, Map<String, String>> = ActionType.CUSTOM to mapOf("action" to "slider_value_changed"),
    version: Int = 1,
    init: CardBuilder.() -> Unit = {}
): CardToken {
    return card(id ?: "slider_example") {
        this.version = version
        padding {
            all = 16
        }
        margin {
            all = 16
        }
        background {
            color {
                red = 255
                green = 255
                blue = 255
            }
            cornerRadius = 8
        }

        text {
            text(sliderTitle)
            style = TextStyle.HeadlineSmall
            margin {
                bottom = 16
            }
        }

        text {
            text(sliderDescription)
            style = TextStyle.BodyMedium
            margin {
                bottom = 24
            }
        }

        // Create a custom slider with the specified properties
        val sliderBuilder = SliderBuilder().apply {
            margin = Margin(vertical = 8)
            a11y = A11y(
                role = Role.SLIDER,
                label = TemplateString("Adjust value"),
                liveRegion = LiveRegion.OFF,
                isEnabled = true,
                isFocusable = true
            )
            onChange = Action(
                type = onSliderChange.first,
                data = onSliderChange.second
            )
        }

        // Set the properties that can't be reassigned
        val customSlider = SliderToken(
            id = ComponentContext.generateId("slider"),
            version = 1,
            a11y = sliderBuilder.a11y,
            initialValue = initialValue,
            rangeStart = rangeStart,
            rangeEnd = rangeEnd,
            steps = steps,
            enabled = true,
            margin = sliderBuilder.margin,
            onChange = sliderBuilder.onChange
        )

        // Add the custom slider to the children
        children.add(customSlider)

        text {
            text("The slider maintains its state locally after the initial render.")
            style = TextStyle.BodySmall
            color {
                red = 100
                green = 100
                blue = 100
            }
            margin {
                top = 16
            }
        }

        init()
    }
}

/**
 * Creates a lazy list example component.
 *
 * @param id Optional ID for the component. If not provided, an ID will be auto-generated.
 * @param itemCount Number of items to display in the list.
 * @param version Version of the component.
 * @param init Optional initialization block for additional customization.
 * @return A LazyColumnToken representing the lazy list example.
 */
fun lazyListExample(
    id: String? = null,
    itemCount: Int = 5,
    version: Int = 1,
    init: LazyColumnBuilder.() -> Unit = {}
): LazyColumnToken {
    return lazyColumn(id ?: "lazy_list_example") {
        this.version = version
        padding {
            vertical = 8
        }

        // Create items
        for (index in 0 until itemCount) {
            row {
                padding {
                    all = 8
                }
                margin {
                    bottom = 8
                    horizontal = 16
                }
                background {
                    color {
                        red = 255
                        green = 255
                        blue = 255
                    }
                    borderColor {
                        red = 230
                        green = 230
                        blue = 255
                    }
                    borderWidth = 1
                    cornerRadius = 4
                }

                asyncImage {
                    url("https://picsum.photos/50/50?random=$index")
                    widthDp = 50
                    heightDp = 50
                    clip = ClipShape.ROUNDED4
                    margin {
                        end = 16
                    }
                    errorFallback {
                        text("Failed to load image")
                    }
                }

                column {
                    text {
                        text("Item ${index + 1}")
                        style = TextStyle.TitleMedium
                    }

                    text {
                        text("This is item number ${index + 1}")
                        style = TextStyle.BodySmall
                    }
                }
            }
        }

        init()
    }
}
