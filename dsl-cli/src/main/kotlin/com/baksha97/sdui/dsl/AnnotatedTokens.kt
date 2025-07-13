package com.baksha97.sdui.dsl

import com.baksha97.sdui.dsl.annotations.*
import com.baksha97.sdui.dsl.reflection.TokenRegistry
import kotlinx.serialization.Serializable

/**
 * Example of how to use annotations to define token models.
 * This file demonstrates how annotations can be used to reduce boilerplate
 * and enable automatic schema generation and version management.
 */

/**
 * Annotated version of TextToken.
 * This demonstrates how annotations can be used to provide schema information
 * and version management for token properties.
 */
@Serializable
@TokenType(description = "A text component")
@VersionInfo(defaultVersion = 1, minSupportedVersion = 1)
data class AnnotatedTextToken(
    @TokenProperty(required = true, description = "Unique identifier for the token")
    override val id: String,

    @TokenProperty(required = true, description = "Version of the token")
    override val version: Int,

    @SchemaRef(description = "Accessibility properties")
    override val a11y: A11y? = null,

    @Required(description = "Text content")
    @SchemaRef(ref = "TemplateString")
    val text: TemplateString,

    @SchemaEnum(description = "Text style")
    val style: TextStyle = TextStyle.BodyMedium,

    @SchemaRef(description = "Text color")
    val color: ColorValue? = null,

    @SchemaProperty(description = "Maximum number of lines")
    val maxLines: Int? = null,

    @SchemaEnum(description = "Text overflow behavior")
    val overflow: TextOverflowValue = TextOverflowValue.Clip,

    @SchemaEnum(description = "Text alignment")
    val textAlign: TextAlignValue? = null,

    @SchemaRef(description = "Margin settings")
    val margin: Margin? = null
) : Token {
    override val minSupportedVersion: Int = 1

    companion object {
        /**
         * Custom migration function for the style property.
         * This demonstrates how to implement custom migration logic for a property.
         */
        fun migrateStyle(value: TextStyle?, targetVersion: Int): TextStyle {
            // Example migration logic - in version 2, we might want to upgrade certain styles
            if (targetVersion >= 2 && value == TextStyle.BodySmall) {
                return TextStyle.BodyMedium
            }
            return value ?: TextStyle.BodyMedium
        }
    }
}

/**
 * Annotated version of ColumnToken.
 * This demonstrates how annotations can be used for container tokens.
 */
@Serializable
@TokenType(description = "A vertical container that arranges its children in a column")
@VersionInfo(defaultVersion = 1, minSupportedVersion = 1)
@BaseToken
data class AnnotatedColumnToken(
    @TokenProperty(required = true, description = "Unique identifier for the token")
    override val id: String,

    @TokenProperty(required = true, description = "Version of the token")
    override val version: Int,

    @SchemaRef(description = "Accessibility properties")
    override val a11y: A11y? = null,

    @SchemaRef(description = "Padding settings")
    val padding: Padding? = null,

    @SchemaRef(description = "Margin settings")
    val margin: Margin? = null,

    @SchemaRef(description = "Background settings")
    val background: Background? = null,

    @SchemaEnum(description = "Horizontal alignment of children")
    val alignment: HorizontalAlignment = HorizontalAlignment.Start,

    @SchemaChildren(description = "Child tokens")
    @Migrateable(migrationStrategy = MigrationStrategy.RECURSIVE)
    override val children: List<Token>
) : ContainerToken {
    override val minSupportedVersion: Int = 1
}

/**
 * Annotated version of ButtonToken.
 * This demonstrates how annotations can be used for interactive tokens.
 */
@Serializable
@TokenType(description = "A button component")
@VersionInfo(defaultVersion = 1, minSupportedVersion = 1)
@BaseToken
data class AnnotatedButtonToken(
    @TokenProperty(required = true, description = "Unique identifier for the token")
    override val id: String,

    @TokenProperty(required = true, description = "Version of the token")
    override val version: Int,

    @SchemaRef(description = "Accessibility properties")
    override val a11y: A11y? = null,

    @Required(description = "Button text")
    @SchemaRef(ref = "TemplateString")
    val text: TemplateString,

    @SchemaEnum(description = "Button style")
    val style: ButtonStyle = ButtonStyle.Filled,

    @SchemaProperty(description = "Whether the button is enabled")
    val enabled: Boolean = true,

    @SchemaRef(description = "Margin settings")
    val margin: Margin? = null,

    @Required(description = "Action to perform when clicked")
    @SchemaRef(description = "Action to perform when clicked")
    override val onClick: Action
) : InteractiveToken {
    override val minSupportedVersion: Int = 1
}

/**
 * Function to register all annotated token types with the TokenRegistry.
 */
fun registerAnnotatedTokensWithRegistry() {
    // Register token types
    TokenRegistry.registerTokenTypes(
        AnnotatedTextToken::class,
        AnnotatedColumnToken::class,
        AnnotatedButtonToken::class
    )

    // Register value types
    TokenRegistry.registerValueTypes(
        A11y::class,
        Padding::class,
        Margin::class,
        Background::class,
        Action::class,
        ColorValue::class,
        ErrorFallback::class,
        LoadingPlaceholder::class,
        TemplateString::class
    )
}
