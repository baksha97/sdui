package com.baksha97.sdui.dsl.annotations

/**
 * Annotations for schema generation and token properties.
 * These annotations are used to provide metadata for token properties
 * that can be used for automatic schema generation and validation.
 */

/**
 * Marks a class as a token type that should be included in the schema.
 * @param description Description of the token type for schema documentation.
 * @param defaultVersion Default version for this token type.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class TokenType(
    val description: String,
    val defaultVersion: Int = 1
)

/**
 * Marks a property as required in the schema.
 * @param description Description of the property for schema documentation.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Required(
    val description: String = ""
)

/**
 * Provides schema information for a property.
 * @param description Description of the property for schema documentation.
 * @param minimum Minimum value for numeric properties.
 * @param maximum Maximum value for numeric properties.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class SchemaProperty(
    val description: String = "",
    val minimum: Int = Int.MIN_VALUE,
    val maximum: Int = Int.MAX_VALUE
)

/**
 * Marks a property as an enum in the schema.
 * The enum values will be automatically extracted from the property type.
 * @param description Description of the enum property for schema documentation.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class SchemaEnum(
    val description: String = ""
)

/**
 * Marks a property as a reference to another schema definition.
 * @param ref The name of the referenced schema definition.
 * @param description Description of the reference property for schema documentation.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class SchemaRef(
    val ref: String = "",
    val description: String = ""
)

/**
 * Marks a property as a container for child tokens.
 * @param description Description of the children property for schema documentation.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class SchemaChildren(
    val description: String = "Child tokens"
)