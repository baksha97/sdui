package com.baksha97.sdui

import androidx.compose.ui.Alignment

/**
 * Examples of using the DSL to create UI components.
 * This file demonstrates how to use the DSL to create the same UI components
 * that are defined in Models.kt, but in a more concise and readable way.
 */

// Example of a profile card using the DSL
val profileCardDSL = row("profile_card") {
    version = 2
    padding {
        all = 12
    }

    asyncImage("profile.avatar") {
        version = 2
        url("{{avatarUrl}}")
        widthDp = 56
        heightDp = 56
        clip = ClipShape.CIRCLE
    }

    column("profile.texts") {
        version = 2
        padding {
            all = 8
        }

        text("profile.username") {
            version = 2
            text("{{username}}")
            style = TextStyle.HeadlineMedium
        }

        text("profile.followers") {
            version = 2
            text("{{followers}} followers")
            style = TextStyle.BodyMedium
        }
    }
}

// Example of an article card using the DSL
val articleCardDSL = row("article_card") {
    version = 1
    padding {
        all = 12
    }

    asyncImage("article.thumb") {
        version = 1
        url("{{thumbUrl}}")
        layoutWeight = 1f
        clip = ClipShape.ROUNDED4
        contentScale = ContentScale.Crop
    }

    column("article.texts") {
        version = 1
        padding {
            all = 8
        }

        text("article.headline") {
            version = 1
            text("{{headline}}")
            style = TextStyle.BodyMedium
        }
    }
}

// Example of an enhanced card using the DSL
val enhancedCardDSL = card("enhanced_card") {
    version = 1
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

    text("enhanced_card.title") {
        version = 1
        text("{{title}}")
        style = TextStyle.HeadlineSmall
        margin {
            bottom = 8
        }
    }

    text("enhanced_card.description") {
        version = 1
        text("{{description}}")
        style = TextStyle.BodyMedium
        margin {
            bottom = 16
        }
    }

    button("enhanced_card.button") {
        version = 1
        text("{{buttonText}}")
        style = ButtonStyle.Filled
        a11y {
            role = Role.BUTTON
            label("{{buttonText}}")
        }
        onClick {
            type = ActionType.CUSTOM
            data("action" to "card_button_clicked")
        }
    }
}

// Example of a form using the DSL
val formExampleDSL = box("form_example") {
    version = 1
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

    column("form_example.content") {
        version = 1

        text("form_example.title") {
            version = 1
            text("{{formTitle}}")
            style = TextStyle.TitleLarge
            margin {
                top = 8
                start = 8
                end = 8
                bottom = 16
            }
        }

        divider("form_example.divider1") {
            version = 1
            margin {
                vertical = 8
            }
        }

        text("form_example.description") {
            version = 1
            text("{{formDescription}}")
            style = TextStyle.BodyMedium
            margin {
                start = 8
                end = 8
                bottom = 16
            }
        }

        spacer("form_example.spacer") {
            version = 1
            height = 16
        }

        row("form_example.buttons") {
            version = 1
            alignment = Alignment.CenterVertically

            button("form_example.cancel") {
                version = 1
                text("Cancel")
                style = ButtonStyle.Text
                onClick {
                    type = ActionType.CUSTOM
                    data("action" to "form_cancel")
                }
            }

            spacer("form_example.button_spacer") {
                version = 1
                width = 8
            }

            button("form_example.submit") {
                version = 1
                text("Submit")
                style = ButtonStyle.Filled
                onClick {
                    type = ActionType.CUSTOM
                    data("action" to "form_submit")
                }
            }
        }
    }
}

// Example of a home screen using the DSL
val homeScreenDSL = screenPayload("home") {
    tokenRef("profile_card") {
        bind(
            "avatarUrl" to "https://picsum.photos/56",
            "username" to "Travis",
            "followers" to 874
        )
    }

    tokenRef("article_card") {
        bind(
            "thumbUrl" to "https://picsum.photos/400/250",
            "headline" to "Server-driven UI cuts release time"
        )
    }
}

// Example of an enhanced screen using the DSL
val enhancedScreenDSL = screenPayload("enhanced_home") {
    tokenRef("enhanced_card") {
        bind(
            "title" to "Enhanced Card Example",
            "description" to "This card demonstrates the new CardToken with styling and a button.",
            "buttonText" to "Learn More"
        )
    }

    tokenRef("slider_example") {
        bind(
            "sliderTitle" to "Local State Management",
            "sliderDescription" to "This slider demonstrates local state management. Move the slider and it will maintain its position even though the server doesn't know about the change."
        )
    }

    tokenRef("form_example") {
        bind(
            "formTitle" to "Contact Form",
            "formDescription" to "Fill out this form to get in touch with us."
        )
    }

    tokenRef("lazy_list_example") {
        bind()
    }
}

// Example of a slider with local state management using the DSL
val sliderExampleDSL = card("slider_example") {
    version = 1
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

    text("slider_example.title") {
        version = 1
        text("{{sliderTitle}}")
        style = TextStyle.HeadlineSmall
        margin {
            bottom = 16
        }
    }

    text("slider_example.description") {
        version = 1
        text("{{sliderDescription}}")
        style = TextStyle.BodyMedium
        margin {
            bottom = 24
        }
    }

    slider("slider_example.slider") {
        version = 1
        initialValue = 0.5f
        valueRange = 0f..1f
        steps = 10
        margin {
            vertical = 8
        }
        a11y {
            role = Role.SLIDER
            label("Adjust value")
        }
        onChange {
            type = ActionType.CUSTOM
            data("action" to "slider_value_changed")
        }
    }

    text("slider_example.note") {
        version = 1
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
}

// Example of a list using LazyColumn with the DSL
val lazyListExampleDSL = lazyColumn("lazy_list_example") {
    version = 1
    padding {
        vertical = 8
    }

    // Create 5 items
    for (index in 0 until 5) {
        row("lazy_list_example.item_$index") {
            version = 1
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

            asyncImage("lazy_list_example.item_$index.image") {
                version = 1
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

            column("lazy_list_example.item_$index.content") {
                version = 1

                text("lazy_list_example.item_$index.title") {
                    version = 1
                    text("Item ${index + 1}")
                    style = TextStyle.TitleMedium
                }

                text("lazy_list_example.item_$index.subtitle") {
                    version = 1
                    text("This is item number ${index + 1}")
                    style = TextStyle.BodySmall
                }
            }
        }
    }
}

// Example of creating a registry using the DSL
val registryDSL = TokenRegistry().apply {
    register(profileCardDSL)
    register(articleCardDSL)
    register(enhancedCardDSL)
    register(formExampleDSL)
    register(sliderExampleDSL)
    register(lazyListExampleDSL)
}
