package com.baksha97.sdui.dsl.versioning

import kotlinx.serialization.Serializable

/**
 * Represents a semantic version (MAJOR.MINOR.PATCH).
 * 
 * - MAJOR version when you make incompatible API changes
 * - MINOR version when you add functionality in a backward compatible manner
 * - PATCH version when you make backward compatible bug fixes
 */
@Serializable
data class SemanticVersion(
    val major: Int,
    val minor: Int,
    val patch: Int
) : Comparable<SemanticVersion> {
    
    companion object {
        /**
         * Creates a SemanticVersion from a version string in the format "MAJOR.MINOR.PATCH".
         */
        fun fromString(versionString: String): SemanticVersion {
            val parts = versionString.split(".")
            require(parts.size == 3) { "Version string must be in the format MAJOR.MINOR.PATCH" }
            
            return SemanticVersion(
                major = parts[0].toInt(),
                minor = parts[1].toInt(),
                patch = parts[2].toInt()
            )
        }
        
        /**
         * The initial version (1.0.0).
         */
        val INITIAL = SemanticVersion(1, 0, 0)
    }
    
    /**
     * Returns a string representation of this version in the format "MAJOR.MINOR.PATCH".
     */
    override fun toString(): String = "$major.$minor.$patch"
    
    /**
     * Compares this version with another version.
     */
    override fun compareTo(other: SemanticVersion): Int {
        return when {
            major != other.major -> major.compareTo(other.major)
            minor != other.minor -> minor.compareTo(other.minor)
            else -> patch.compareTo(other.patch)
        }
    }
    
    /**
     * Checks if this version is compatible with the specified version.
     * 
     * A version is compatible if:
     * - The major versions are the same (no breaking changes)
     * - This version's minor and patch are greater than or equal to the other version
     */
    fun isCompatibleWith(other: SemanticVersion): Boolean {
        return major == other.major && (minor > other.minor || (minor == other.minor && patch >= other.patch))
    }
    
    /**
     * Checks if this version can be migrated to the target version.
     * 
     * A version can be migrated if:
     * - The target version is greater than or equal to this version
     */
    fun canMigrateTo(targetVersion: SemanticVersion): Boolean {
        return this <= targetVersion
    }
    
    /**
     * Creates a new version with the major version incremented.
     */
    fun incrementMajor(): SemanticVersion = copy(major = major + 1, minor = 0, patch = 0)
    
    /**
     * Creates a new version with the minor version incremented.
     */
    fun incrementMinor(): SemanticVersion = copy(minor = minor + 1, patch = 0)
    
    /**
     * Creates a new version with the patch version incremented.
     */
    fun incrementPatch(): SemanticVersion = copy(patch = patch + 1)
}