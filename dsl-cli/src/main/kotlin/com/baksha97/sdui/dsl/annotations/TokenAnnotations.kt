package com.baksha97.sdui.dsl.annotations

/**
 * Annotations for token properties and behavior.
 * These annotations are used to provide metadata for tokens
 * that can be used for automatic code generation and optimization.
 */

/**
 * Marks a class as a base token that can be extended by other tokens.
 * This annotation is used to identify base token types for inheritance.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseToken

/**
 * Marks a class as a container token that can hold other tokens.
 * This annotation is used to identify container token types.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ContainerToken

/**
 * Marks a class as an interactive token that can be clicked.
 * This annotation is used to identify interactive token types.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class InteractiveToken

/**
 * Provides version information for a token.
 * @param defaultVersion Default version for this token type.
 * @param minSupportedVersion Minimum supported version for this token type.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class VersionInfo(
    val defaultVersion: Int = 1,
    val minSupportedVersion: Int = 1
)

/**
 * Marks a property as a common token property.
 * This annotation is used to identify properties that are common to all tokens.
 * @param required Whether the property is required.
 * @param description Description of the property for documentation.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class TokenProperty(
    val required: Boolean = false,
    val description: String = ""
)

/**
 * Marks a property as a migrateable property.
 * This annotation is used to identify properties that should be migrated when the token version changes.
 * @param migrationStrategy The strategy to use when migrating this property.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Migrateable(
    val migrationStrategy: MigrationStrategy = MigrationStrategy.COPY
)

/**
 * Enum defining migration strategies for token properties.
 */
enum class MigrationStrategy {
    /**
     * Copy the property value as-is during migration.
     */
    COPY,
    
    /**
     * Apply a custom migration function to the property value.
     */
    CUSTOM,
    
    /**
     * Recursively migrate child tokens.
     */
    RECURSIVE
}