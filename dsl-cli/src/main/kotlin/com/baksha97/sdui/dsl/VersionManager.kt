package com.baksha97.sdui.dsl

import kotlinx.serialization.json.*
import com.baksha97.sdui.shared.models.*

/**
 * Manages versioning for tokens and handles migrations between different versions.
 * This class provides functionality to check version compatibility and migrate tokens
 * between different versions of the schema.
 */
class VersionManager {

    /**
     * Checks if a token is compatible with the current client version.
     * @param token The token to check.
     * @param clientVersion The client version to check against.
     * @return True if the token is compatible with the client version, false otherwise.
     */
    fun isCompatible(token: Token, clientVersion: Int): Boolean {
        // Check if the token version is greater than the minimum supported version
        // and less than or equal to the client version
        return token.version >= token.minSupportedVersion && token.version <= clientVersion
    }

    /**
     * Migrates a token to a specific version.
     * @param token The token to migrate.
     * @param targetVersion The target version to migrate to.
     * @return The migrated token, or null if migration is not possible.
     */
    fun migrateToken(token: Token, targetVersion: Int): Token? {
        // If the token is already at the target version, return it as is
        if (token.version == targetVersion) {
            return token
        }

        // If the token version is greater than the target version, we can't migrate
        if (token.version > targetVersion) {
            return null
        }

        // Migrate the token based on its type
        return when (token) {
            is ColumnToken -> migrateColumnToken(token, targetVersion)
            is RowToken -> migrateRowToken(token, targetVersion)
            is BoxToken -> migrateBoxToken(token, targetVersion)
            is CardToken -> migrateCardToken(token, targetVersion)
            is TextToken -> migrateTextToken(token, targetVersion)
            is ButtonToken -> migrateButtonToken(token, targetVersion)
            is SpacerToken -> migrateSpacerToken(token, targetVersion)
            is DividerToken -> migrateDividerToken(token, targetVersion)
            is SliderToken -> migrateSliderToken(token, targetVersion)
            is AsyncImageToken -> migrateAsyncImageToken(token, targetVersion)
            is LazyColumnToken -> migrateLazyColumnToken(token, targetVersion)
            is LazyRowToken -> migrateLazyRowToken(token, targetVersion)
            else -> null
        }
    }

    /**
     * Migrates a token from JSON to a specific version.
     * @param tokenJson The JSON representation of the token to migrate.
     * @param targetVersion The target version to migrate to.
     * @return The migrated token JSON, or null if migration is not possible.
     */
    fun migrateTokenJson(tokenJson: JsonObject, targetVersion: Int): JsonObject? {
        // Get the token version
        val tokenVersion = tokenJson["version"]?.jsonPrimitive?.int ?: 1

        // If the token is already at the target version, return it as is
        if (tokenVersion == targetVersion) {
            return tokenJson
        }

        // If the token version is greater than the target version, we can't migrate
        if (tokenVersion > targetVersion) {
            return null
        }

        // Determine the token type
        val tokenType = determineTokenType(tokenJson)

        // Migrate the token based on its type
        return when (tokenType) {
            "ColumnToken" -> migrateColumnTokenJson(tokenJson, targetVersion)
            "RowToken" -> migrateRowTokenJson(tokenJson, targetVersion)
            "BoxToken" -> migrateBoxTokenJson(tokenJson, targetVersion)
            "CardToken" -> migrateCardTokenJson(tokenJson, targetVersion)
            "TextToken" -> migrateTextTokenJson(tokenJson, targetVersion)
            "ButtonToken" -> migrateButtonTokenJson(tokenJson, targetVersion)
            "SpacerToken" -> migrateSpacerTokenJson(tokenJson, targetVersion)
            "DividerToken" -> migrateDividerTokenJson(tokenJson, targetVersion)
            "SliderToken" -> migrateSliderTokenJson(tokenJson, targetVersion)
            "AsyncImageToken" -> migrateAsyncImageTokenJson(tokenJson, targetVersion)
            "LazyColumnToken" -> migrateLazyColumnTokenJson(tokenJson, targetVersion)
            "LazyRowToken" -> migrateLazyRowTokenJson(tokenJson, targetVersion)
            else -> null
        }
    }

    /**
     * Determines the type of a token based on its JSON representation.
     * @param tokenJson The JSON representation of the token.
     * @return The type of the token as a string.
     */
    private fun determineTokenType(tokenJson: JsonObject): String {
        // Check if the token has a type property
        val typeProperty = tokenJson["type"]?.jsonPrimitive?.content
        if (typeProperty != null) {
            return typeProperty
        }

        // If no type property, try to infer the type from the properties
        if (tokenJson.containsKey("children")) {
            // It's a container token
            if (tokenJson.containsKey("alignment")) {
                val alignment = tokenJson["alignment"]?.jsonPrimitive?.content
                return if (alignment in listOf("Start", "Center", "End")) {
                    "ColumnToken"
                } else if (alignment in listOf("Top", "CenterVertically", "Bottom")) {
                    "RowToken"
                } else {
                    "BoxToken"
                }
            } else if (tokenJson.containsKey("contentAlignment")) {
                return "BoxToken"
            } else if (tokenJson.containsKey("elevation") || tokenJson.containsKey("shape")) {
                return "CardToken"
            }
        }

        // Check for specific properties of each token type
        return when {
            tokenJson.containsKey("text") && tokenJson.containsKey("style") -> "TextToken"
            tokenJson.containsKey("text") && tokenJson.containsKey("onClick") -> "ButtonToken"
            tokenJson.containsKey("width") || tokenJson.containsKey("height") -> "SpacerToken"
            tokenJson.containsKey("thickness") -> "DividerToken"
            tokenJson.containsKey("initialValue") && tokenJson.containsKey("valueRange") -> "SliderToken"
            tokenJson.containsKey("url") && tokenJson.containsKey("contentScale") -> "AsyncImageToken"
            else -> "UnknownToken"
        }
    }

    // Migration methods for token objects

    private fun migrateColumnToken(token: ColumnToken, targetVersion: Int): ColumnToken {
        // Migrate children
        val migratedChildren = token.children.mapNotNull { migrateToken(it, targetVersion) }

        // Create a new token with the migrated properties
        return ColumnToken(
            id = token.id,
            version = targetVersion,
            a11y = token.a11y,
            padding = token.padding,
            margin = token.margin,
            background = token.background,
            alignment = token.alignment,
            children = migratedChildren
        )
    }

    private fun migrateRowToken(token: RowToken, targetVersion: Int): RowToken {
        // Migrate children
        val migratedChildren = token.children.mapNotNull { migrateToken(it, targetVersion) }

        // Create a new token with the migrated properties
        return RowToken(
            id = token.id,
            version = targetVersion,
            a11y = token.a11y,
            padding = token.padding,
            margin = token.margin,
            background = token.background,
            alignment = token.alignment,
            children = migratedChildren
        )
    }

    private fun migrateBoxToken(token: BoxToken, targetVersion: Int): BoxToken {
        // Migrate children
        val migratedChildren = token.children.mapNotNull { migrateToken(it, targetVersion) }

        // Create a new token with the migrated properties
        return BoxToken(
            id = token.id,
            version = targetVersion,
            a11y = token.a11y,
            padding = token.padding,
            margin = token.margin,
            background = token.background,
            contentAlignment = token.contentAlignment,
            children = migratedChildren
        )
    }

    private fun migrateCardToken(token: CardToken, targetVersion: Int): CardToken {
        // Migrate children
        val migratedChildren = token.children.mapNotNull { migrateToken(it, targetVersion) }

        // Create a new token with the migrated properties
        return CardToken(
            id = token.id,
            version = targetVersion,
            a11y = token.a11y,
            padding = token.padding,
            margin = token.margin,
            elevation = token.elevation,
            shape = token.shape,
            background = token.background,
            onClick = token.onClick,
            children = migratedChildren
        )
    }

    private fun migrateTextToken(token: TextToken, targetVersion: Int): TextToken {
        // Create a new token with the migrated properties
        return TextToken(
            id = token.id,
            version = targetVersion,
            a11y = token.a11y,
            text = token.text,
            style = token.style,
            color = token.color,
            maxLines = token.maxLines,
            overflow = token.overflow,
            textAlign = token.textAlign,
            margin = token.margin
        )
    }

    private fun migrateButtonToken(token: ButtonToken, targetVersion: Int): ButtonToken {
        // Create a new token with the migrated properties
        return ButtonToken(
            id = token.id,
            version = targetVersion,
            a11y = token.a11y,
            text = token.text,
            style = token.style,
            enabled = token.enabled,
            margin = token.margin,
            onClick = token.onClick
        )
    }

    private fun migrateSpacerToken(token: SpacerToken, targetVersion: Int): SpacerToken {
        // Create a new token with the migrated properties
        return SpacerToken(
            id = token.id,
            version = targetVersion,
            a11y = token.a11y,
            width = token.width,
            height = token.height
        )
    }

    private fun migrateDividerToken(token: DividerToken, targetVersion: Int): DividerToken {
        // Create a new token with the migrated properties
        return DividerToken(
            id = token.id,
            version = targetVersion,
            a11y = token.a11y,
            thickness = token.thickness,
            color = token.color,
            margin = token.margin
        )
    }

    private fun migrateSliderToken(token: SliderToken, targetVersion: Int): SliderToken {
        // Create a new token with the migrated properties
        return SliderToken(
            id = token.id,
            version = targetVersion,
            a11y = token.a11y,
            initialValue = token.initialValue,
            valueRange = token.valueRange,
            steps = token.steps,
            enabled = token.enabled,
            margin = token.margin,
            onChange = token.onChange
        )
    }

    private fun migrateAsyncImageToken(token: AsyncImageToken, targetVersion: Int): AsyncImageToken {
        // Create a new token with the migrated properties
        return AsyncImageToken(
            id = token.id,
            version = targetVersion,
            a11y = token.a11y,
            url = token.url,
            widthDp = token.widthDp,
            heightDp = token.heightDp,
            layoutWeight = token.layoutWeight,
            clip = token.clip,
            contentScale = token.contentScale,
            margin = token.margin,
            errorFallback = token.errorFallback,
            loadingPlaceholder = token.loadingPlaceholder,
            onClick = token.onClick
        )
    }

    private fun migrateLazyColumnToken(token: LazyColumnToken, targetVersion: Int): LazyColumnToken {
        // Migrate children
        val migratedChildren = token.children.mapNotNull { migrateToken(it, targetVersion) }

        // Create a new token with the migrated properties
        return LazyColumnToken(
            id = token.id,
            version = targetVersion,
            a11y = token.a11y,
            padding = token.padding,
            margin = token.margin,
            background = token.background,
            alignment = token.alignment,
            children = migratedChildren
        )
    }

    private fun migrateLazyRowToken(token: LazyRowToken, targetVersion: Int): LazyRowToken {
        // Migrate children
        val migratedChildren = token.children.mapNotNull { migrateToken(it, targetVersion) }

        // Create a new token with the migrated properties
        return LazyRowToken(
            id = token.id,
            version = targetVersion,
            a11y = token.a11y,
            padding = token.padding,
            margin = token.margin,
            background = token.background,
            alignment = token.alignment,
            children = migratedChildren
        )
    }

    // Migration methods for token JSON

    private fun migrateColumnTokenJson(tokenJson: JsonObject, targetVersion: Int): JsonObject {
        // Create a mutable copy of the token JSON
        val mutableJson = tokenJson.toMutableMap()

        // Update the version
        mutableJson["version"] = JsonPrimitive(targetVersion)

        // Migrate children if present
        val children = tokenJson["children"]?.jsonArray
        if (children != null) {
            val migratedChildren = JsonArray(children.mapNotNull { 
                migrateTokenJson(it.jsonObject, targetVersion) 
            })
            mutableJson["children"] = migratedChildren
        }

        return JsonObject(mutableJson)
    }

    private fun migrateRowTokenJson(tokenJson: JsonObject, targetVersion: Int): JsonObject {
        // Create a mutable copy of the token JSON
        val mutableJson = tokenJson.toMutableMap()

        // Update the version
        mutableJson["version"] = JsonPrimitive(targetVersion)

        // Migrate children if present
        val children = tokenJson["children"]?.jsonArray
        if (children != null) {
            val migratedChildren = JsonArray(children.mapNotNull { 
                migrateTokenJson(it.jsonObject, targetVersion) 
            })
            mutableJson["children"] = migratedChildren
        }

        return JsonObject(mutableJson)
    }

    private fun migrateBoxTokenJson(tokenJson: JsonObject, targetVersion: Int): JsonObject {
        // Create a mutable copy of the token JSON
        val mutableJson = tokenJson.toMutableMap()

        // Update the version
        mutableJson["version"] = JsonPrimitive(targetVersion)

        // Migrate children if present
        val children = tokenJson["children"]?.jsonArray
        if (children != null) {
            val migratedChildren = JsonArray(children.mapNotNull { 
                migrateTokenJson(it.jsonObject, targetVersion) 
            })
            mutableJson["children"] = migratedChildren
        }

        return JsonObject(mutableJson)
    }

    private fun migrateCardTokenJson(tokenJson: JsonObject, targetVersion: Int): JsonObject {
        // Create a mutable copy of the token JSON
        val mutableJson = tokenJson.toMutableMap()

        // Update the version
        mutableJson["version"] = JsonPrimitive(targetVersion)

        // Migrate children if present
        val children = tokenJson["children"]?.jsonArray
        if (children != null) {
            val migratedChildren = JsonArray(children.mapNotNull { 
                migrateTokenJson(it.jsonObject, targetVersion) 
            })
            mutableJson["children"] = migratedChildren
        }

        return JsonObject(mutableJson)
    }

    private fun migrateTextTokenJson(tokenJson: JsonObject, targetVersion: Int): JsonObject {
        // Create a mutable copy of the token JSON
        val mutableJson = tokenJson.toMutableMap()

        // Update the version
        mutableJson["version"] = JsonPrimitive(targetVersion)

        return JsonObject(mutableJson)
    }

    private fun migrateButtonTokenJson(tokenJson: JsonObject, targetVersion: Int): JsonObject {
        // Create a mutable copy of the token JSON
        val mutableJson = tokenJson.toMutableMap()

        // Update the version
        mutableJson["version"] = JsonPrimitive(targetVersion)

        return JsonObject(mutableJson)
    }

    private fun migrateSpacerTokenJson(tokenJson: JsonObject, targetVersion: Int): JsonObject {
        // Create a mutable copy of the token JSON
        val mutableJson = tokenJson.toMutableMap()

        // Update the version
        mutableJson["version"] = JsonPrimitive(targetVersion)

        return JsonObject(mutableJson)
    }

    private fun migrateDividerTokenJson(tokenJson: JsonObject, targetVersion: Int): JsonObject {
        // Create a mutable copy of the token JSON
        val mutableJson = tokenJson.toMutableMap()

        // Update the version
        mutableJson["version"] = JsonPrimitive(targetVersion)

        return JsonObject(mutableJson)
    }

    private fun migrateSliderTokenJson(tokenJson: JsonObject, targetVersion: Int): JsonObject {
        // Create a mutable copy of the token JSON
        val mutableJson = tokenJson.toMutableMap()

        // Update the version
        mutableJson["version"] = JsonPrimitive(targetVersion)

        return JsonObject(mutableJson)
    }

    private fun migrateAsyncImageTokenJson(tokenJson: JsonObject, targetVersion: Int): JsonObject {
        // Create a mutable copy of the token JSON
        val mutableJson = tokenJson.toMutableMap()

        // Update the version
        mutableJson["version"] = JsonPrimitive(targetVersion)

        return JsonObject(mutableJson)
    }

    private fun migrateLazyColumnTokenJson(tokenJson: JsonObject, targetVersion: Int): JsonObject {
        // Create a mutable copy of the token JSON
        val mutableJson = tokenJson.toMutableMap()

        // Update the version
        mutableJson["version"] = JsonPrimitive(targetVersion)

        // Migrate children if present
        val children = tokenJson["children"]?.jsonArray
        if (children != null) {
            val migratedChildren = JsonArray(children.mapNotNull { 
                migrateTokenJson(it.jsonObject, targetVersion) 
            })
            mutableJson["children"] = migratedChildren
        }

        return JsonObject(mutableJson)
    }

    private fun migrateLazyRowTokenJson(tokenJson: JsonObject, targetVersion: Int): JsonObject {
        // Create a mutable copy of the token JSON
        val mutableJson = tokenJson.toMutableMap()

        // Update the version
        mutableJson["version"] = JsonPrimitive(targetVersion)

        // Migrate children if present
        val children = tokenJson["children"]?.jsonArray
        if (children != null) {
            val migratedChildren = JsonArray(children.mapNotNull { 
                migrateTokenJson(it.jsonObject, targetVersion) 
            })
            mutableJson["children"] = migratedChildren
        }

        return JsonObject(mutableJson)
    }
}
