package com.baksha97.sdui.dsl.versioning

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import java.io.File

/**
 * A central registry for managing versions across the project.
 * 
 * This class provides a simple way to register, track, and query versions
 * for different components of the system.
 */
class VersionRegistry {
    
    private val versions = mutableMapOf<String, SemanticVersion>()
    private val migrationPaths = mutableMapOf<VersionKey, MigrationPath>()
    
    /**
     * Registers a component with its current version.
     * 
     * @param componentId The unique identifier for the component
     * @param version The current version of the component
     */
    fun registerVersion(componentId: String, version: SemanticVersion) {
        versions[componentId] = version
    }
    
    /**
     * Registers a component with its current version from a string.
     * 
     * @param componentId The unique identifier for the component
     * @param versionString The current version of the component as a string (MAJOR.MINOR.PATCH)
     */
    fun registerVersion(componentId: String, versionString: String) {
        registerVersion(componentId, SemanticVersion.fromString(versionString))
    }
    
    /**
     * Gets the current version of a component.
     * 
     * @param componentId The unique identifier for the component
     * @return The current version of the component, or null if not registered
     */
    fun getVersion(componentId: String): SemanticVersion? {
        return versions[componentId]
    }
    
    /**
     * Checks if a component is registered.
     * 
     * @param componentId The unique identifier for the component
     * @return True if the component is registered, false otherwise
     */
    fun isRegistered(componentId: String): Boolean {
        return versions.containsKey(componentId)
    }
    
    /**
     * Updates the version of a component.
     * 
     * @param componentId The unique identifier for the component
     * @param newVersion The new version of the component
     * @return True if the component was updated, false if it wasn't registered
     */
    fun updateVersion(componentId: String, newVersion: SemanticVersion): Boolean {
        if (!isRegistered(componentId)) {
            return false
        }
        
        versions[componentId] = newVersion
        return true
    }
    
    /**
     * Registers a migration path between two versions of a component.
     * 
     * @param componentId The unique identifier for the component
     * @param fromVersion The source version
     * @param toVersion The target version
     * @param migrationPath The migration path information
     */
    fun registerMigrationPath(
        componentId: String,
        fromVersion: SemanticVersion,
        toVersion: SemanticVersion,
        migrationPath: MigrationPath
    ) {
        val key = VersionKey(componentId, fromVersion, toVersion)
        migrationPaths[key] = migrationPath
    }
    
    /**
     * Gets the migration path between two versions of a component.
     * 
     * @param componentId The unique identifier for the component
     * @param fromVersion The source version
     * @param toVersion The target version
     * @return The migration path, or null if not registered
     */
    fun getMigrationPath(
        componentId: String,
        fromVersion: SemanticVersion,
        toVersion: SemanticVersion
    ): MigrationPath? {
        val key = VersionKey(componentId, fromVersion, toVersion)
        return migrationPaths[key]
    }
    
    /**
     * Saves the version registry to a JSON file.
     * 
     * @param file The file to save to
     */
    fun saveToFile(file: File) {
        val registryData = RegistryData(
            versions = versions.map { (id, version) -> ComponentVersion(id, version) },
            migrationPaths = migrationPaths.map { (key, path) -> 
                MigrationPathEntry(key.componentId, key.fromVersion, key.toVersion, path)
            }
        )
        
        val json = Json { prettyPrint = true }
        file.writeText(json.encodeToString(RegistryData.serializer(), registryData))
    }
    
    /**
     * Loads the version registry from a JSON file.
     * 
     * @param file The file to load from
     * @return A new VersionRegistry instance loaded from the file
     */
    companion object {
        fun loadFromFile(file: File): VersionRegistry {
            val json = Json { ignoreUnknownKeys = true }
            val registryData = json.decodeFromString(RegistryData.serializer(), file.readText())
            
            val registry = VersionRegistry()
            
            registryData.versions.forEach { (id, version) ->
                registry.registerVersion(id, version)
            }
            
            registryData.migrationPaths.forEach { entry ->
                registry.registerMigrationPath(
                    entry.componentId,
                    entry.fromVersion,
                    entry.toVersion,
                    entry.migrationPath
                )
            }
            
            return registry
        }
    }
    
    /**
     * Key for looking up migration paths.
     */
    private data class VersionKey(
        val componentId: String,
        val fromVersion: SemanticVersion,
        val toVersion: SemanticVersion
    )
}

/**
 * Represents a migration path between two versions.
 */
@Serializable
data class MigrationPath(
    val description: String,
    val isBreaking: Boolean,
    val migrationSteps: List<String> = emptyList(),
    val customData: JsonObject? = null
)

/**
 * Data class for serializing component versions.
 */
@Serializable
data class ComponentVersion(
    val id: String,
    val version: SemanticVersion
)

/**
 * Data class for serializing migration paths.
 */
@Serializable
data class MigrationPathEntry(
    val componentId: String,
    val fromVersion: SemanticVersion,
    val toVersion: SemanticVersion,
    val migrationPath: MigrationPath
)

/**
 * Data class for serializing the entire registry.
 */
@Serializable
data class RegistryData(
    val versions: List<ComponentVersion>,
    val migrationPaths: List<MigrationPathEntry>
)