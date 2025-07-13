package com.baksha97.sdui.dsl.versioning

import com.baksha97.sdui.dsl.Token
import com.baksha97.sdui.dsl.ContainerToken
import kotlinx.serialization.json.*

/**
 * A version manager that uses semantic versioning for tokens.
 * 
 * This class replaces the integer-based versioning in the original VersionManager
 * with a more flexible semantic versioning system.
 */
class SemanticVersionManager(
    private val registry: VersionRegistry = VersionRegistry()
) {
    /**
     * Checks if a token is compatible with the client version.
     * 
     * @param token The token to check
     * @param clientVersion The client version to check against
     * @return True if the token is compatible with the client version, false otherwise
     */
    fun isCompatible(token: VersionedToken, clientVersion: SemanticVersion): Boolean {
        return token.version.isCompatibleWith(clientVersion)
    }
    
    /**
     * Migrates a token to a specific version.
     * 
     * @param token The token to migrate
     * @param targetVersion The target version to migrate to
     * @return The migrated token, or null if migration is not possible
     */
    fun migrateToken(token: VersionedToken, targetVersion: SemanticVersion): VersionedToken? {
        // If the token is already at the target version, return it as is
        if (token.version == targetVersion) {
            return token
        }
        
        // If the token version is greater than the target version, we can't migrate
        if (!token.version.canMigrateTo(targetVersion)) {
            return null
        }
        
        // Get the component ID for this token type
        val componentId = token.javaClass.simpleName
        
        // Check if we have a registered migration path
        val migrationPath = registry.getMigrationPath(componentId, token.version, targetVersion)
        
        // If we have a migration path, use it
        if (migrationPath != null) {
            // Apply custom migration logic if available
            return applyCustomMigration(token, targetVersion, migrationPath)
        }
        
        // Otherwise, use default migration logic
        return when (token) {
            is VersionedContainerToken -> migrateContainerToken(token, targetVersion)
            else -> migrateSimpleToken(token, targetVersion)
        }
    }
    
    /**
     * Migrates a token from JSON to a specific version.
     * 
     * @param tokenJson The JSON representation of the token to migrate
     * @param targetVersion The target version to migrate to
     * @return The migrated token JSON, or null if migration is not possible
     */
    fun migrateTokenJson(tokenJson: JsonObject, targetVersion: SemanticVersion): JsonObject? {
        // Get the token version
        val versionString = tokenJson["version"]?.jsonPrimitive?.content ?: "1.0.0"
        val tokenVersion = try {
            SemanticVersion.fromString(versionString)
        } catch (e: Exception) {
            // If the version is an integer, convert it to semantic version
            val versionInt = tokenJson["version"]?.jsonPrimitive?.int ?: 1
            SemanticVersion(versionInt, 0, 0)
        }
        
        // If the token is already at the target version, return it as is
        if (tokenVersion == targetVersion) {
            return tokenJson
        }
        
        // If the token version is greater than the target version, we can't migrate
        if (!tokenVersion.canMigrateTo(targetVersion)) {
            return null
        }
        
        // Determine the token type
        val tokenType = determineTokenType(tokenJson)
        
        // Check if we have a registered migration path
        val migrationPath = registry.getMigrationPath(tokenType, tokenVersion, targetVersion)
        
        // If we have a migration path, use it
        if (migrationPath != null) {
            // Apply custom migration logic if available
            return applyCustomJsonMigration(tokenJson, targetVersion, migrationPath)
        }
        
        // Create a mutable copy of the token JSON
        val mutableJson = tokenJson.toMutableMap()
        
        // Update the version
        mutableJson["version"] = JsonPrimitive(targetVersion.toString())
        
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
    
    /**
     * Determines the type of a token based on its JSON representation.
     * 
     * @param tokenJson The JSON representation of the token
     * @return The type of the token as a string
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
            tokenJson.containsKey("initialValue") && tokenJson.containsKey("rangeStart") -> "SliderToken"
            tokenJson.containsKey("url") && tokenJson.containsKey("contentScale") -> "AsyncImageToken"
            else -> "UnknownToken"
        }
    }
    
    /**
     * Applies custom migration logic to a token.
     * 
     * @param token The token to migrate
     * @param targetVersion The target version to migrate to
     * @param migrationPath The migration path to use
     * @return The migrated token, or null if migration fails
     */
    private fun applyCustomMigration(
        token: VersionedToken,
        targetVersion: SemanticVersion,
        migrationPath: MigrationPath
    ): VersionedToken? {
        // This is a placeholder for custom migration logic
        // In a real implementation, this would use the migration path to apply
        // specific transformations to the token
        
        // For now, just update the version
        return token.withVersion(targetVersion)
    }
    
    /**
     * Applies custom migration logic to a token JSON.
     * 
     * @param tokenJson The token JSON to migrate
     * @param targetVersion The target version to migrate to
     * @param migrationPath The migration path to use
     * @return The migrated token JSON, or null if migration fails
     */
    private fun applyCustomJsonMigration(
        tokenJson: JsonObject,
        targetVersion: SemanticVersion,
        migrationPath: MigrationPath
    ): JsonObject? {
        // This is a placeholder for custom migration logic
        // In a real implementation, this would use the migration path to apply
        // specific transformations to the token JSON
        
        // For now, just update the version
        val mutableJson = tokenJson.toMutableMap()
        mutableJson["version"] = JsonPrimitive(targetVersion.toString())
        return JsonObject(mutableJson)
    }
    
    /**
     * Migrates a container token to a specific version.
     * 
     * @param token The container token to migrate
     * @param targetVersion The target version to migrate to
     * @return The migrated container token, or null if migration fails
     */
    private fun migrateContainerToken(
        token: VersionedContainerToken,
        targetVersion: SemanticVersion
    ): VersionedContainerToken? {
        // Migrate children
        val migratedChildren = token.children.mapNotNull { 
            migrateToken(it, targetVersion) 
        }
        
        // Create a new token with the migrated properties
        return token.withChildrenAndVersion(migratedChildren, targetVersion)
    }
    
    /**
     * Migrates a simple token to a specific version.
     * 
     * @param token The simple token to migrate
     * @param targetVersion The target version to migrate to
     * @return The migrated simple token, or null if migration fails
     */
    private fun migrateSimpleToken(
        token: VersionedToken,
        targetVersion: SemanticVersion
    ): VersionedToken? {
        // For simple tokens, just update the version
        return token.withVersion(targetVersion)
    }
}

/**
 * Interface for tokens that use semantic versioning.
 */
interface VersionedToken {
    val version: SemanticVersion
    
    /**
     * Creates a new token with the specified version.
     * 
     * @param newVersion The new version
     * @return A new token with the specified version
     */
    fun withVersion(newVersion: SemanticVersion): VersionedToken
}

/**
 * Interface for container tokens that use semantic versioning.
 */
interface VersionedContainerToken : VersionedToken {
    val children: List<VersionedToken>
    
    /**
     * Creates a new token with the specified children and version.
     * 
     * @param newChildren The new children
     * @param newVersion The new version
     * @return A new token with the specified children and version
     */
    fun withChildrenAndVersion(newChildren: List<VersionedToken>, newVersion: SemanticVersion): VersionedContainerToken
}