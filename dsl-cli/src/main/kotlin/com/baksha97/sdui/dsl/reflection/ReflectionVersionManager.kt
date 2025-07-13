package com.baksha97.sdui.dsl.reflection

import com.baksha97.sdui.dsl.Token
import com.baksha97.sdui.dsl.annotations.Migrateable
import com.baksha97.sdui.dsl.annotations.MigrationStrategy
import com.baksha97.sdui.dsl.annotations.VersionInfo
import kotlinx.serialization.json.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

/**
 * Version manager that uses reflection and annotations to handle token migration.
 * This class replaces the manual migration code in VersionManager with a more
 * automated approach using reflection and annotations.
 */
class ReflectionVersionManager {

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
     * Migrates a token to a specific version using reflection.
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

        // Get the token class
        val tokenClass = token::class

        // Create a new instance of the token with the migrated properties
        return try {
            // Get the primary constructor
            val constructor = tokenClass.primaryConstructor ?: return null

            // Prepare the arguments for the constructor
            val arguments = mutableMapOf<String, Any?>()

            // Set the version to the target version
            arguments["version"] = targetVersion

            // Copy all other properties based on migration strategy
            tokenClass.memberProperties.forEach { property ->
                val propertyName = property.name
                if (propertyName != "version") {
                    val migrateable = property.findAnnotation<Migrateable>()
                    val strategy = migrateable?.migrationStrategy ?: MigrationStrategy.COPY

                    when (strategy) {
                        MigrationStrategy.COPY -> {
                            // Simply copy the property value
                            arguments[propertyName] = property.getter.call(token)
                        }
                        MigrationStrategy.RECURSIVE -> {
                            // Recursively migrate child tokens
                            val value = property.getter.call(token)
                            if (value is List<*>) {
                                // Handle list of tokens
                                val migratedList = value.mapNotNull { item ->
                                    if (item is Token) {
                                        migrateToken(item, targetVersion)
                                    } else {
                                        item
                                    }
                                }
                                arguments[propertyName] = migratedList
                            } else if (value is Token) {
                                // Handle single token
                                arguments[propertyName] = migrateToken(value, targetVersion)
                            } else {
                                // Not a token or list of tokens, just copy
                                arguments[propertyName] = value
                            }
                        }
                        MigrationStrategy.CUSTOM -> {
                            // Custom migration strategy - look for a migration function
                            val migrationFunction = findMigrationFunction(tokenClass, propertyName)
                            if (migrationFunction != null) {
                                val value = property.getter.call(token)
                                arguments[propertyName] = migrationFunction.invoke(value, targetVersion)
                            } else {
                                // No migration function found, just copy
                                arguments[propertyName] = property.getter.call(token)
                            }
                        }
                    }
                }
            }

            // Create a new instance with the migrated properties
            constructor.callBy(constructor.parameters.associateWith { param ->
                arguments[param.name]
            }) as Token
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Migrates a token from JSON to a specific version using reflection.
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

    /**
     * Finds a migration function for a property.
     * @param tokenClass The token class.
     * @param propertyName The name of the property.
     * @return The migration function, or null if not found.
     */
    private fun findMigrationFunction(tokenClass: KClass<*>, propertyName: String): ((Any?, Int) -> Any?)? {
        // Look for a migration function in the companion object
        val companionObject = tokenClass.nestedClasses.find { it.simpleName == "Companion" } ?: return null
        val companion = try {
            companionObject.objectInstance
        } catch (e: Exception) {
            null
        } ?: return null

        val migrationFunctionName = "migrate${propertyName.capitalize()}"

        return try {
            val method = companion::class.members.find { it.name == migrationFunctionName }
            if (method != null) {
                { value, targetVersion ->
                    method.call(companion, value, targetVersion)
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}

/**
 * Extension function to capitalize the first letter of a string.
 */
private fun String.capitalize(): String {
    return if (isNotEmpty()) {
        this[0].uppercaseChar() + substring(1)
    } else {
        this
    }
}
