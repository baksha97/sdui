package com.baksha97.sdui.dsl.reflection

import com.baksha97.sdui.dsl.annotations.*
import kotlinx.serialization.json.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.superclasses

/**
 * Schema generator that uses reflection and annotations to generate JSON Schema.
 * This class replaces the manual schema generation in SchemaGenerator with a more
 * automated approach using reflection and annotations.
 */
class ReflectionSchemaGenerator {

    /**
     * Generates a JSON Schema for all token types using reflection.
     * @return A JsonObject containing the schema definitions for all token types.
     */
    fun generateTokenSchema(): JsonObject {
        val definitions = buildJsonObject {
            // Add schema for all annotated token types
            TokenRegistry.tokenTypes.forEach { tokenClass ->
                val tokenName = tokenClass.simpleName ?: return@forEach
                put(tokenName, generateTokenSchema(tokenClass))
            }

            // Add schema for value objects
            TokenRegistry.valueTypes.forEach { valueClass ->
                val valueName = valueClass.simpleName ?: return@forEach
                put(valueName, generateValueSchema(valueClass))
            }
        }

        return buildJsonObject {
            put("\$schema", JsonPrimitive("http://json-schema.org/draft-07/schema#"))
            put("title", JsonPrimitive("Server-Driven UI Token Schema"))
            put("description", JsonPrimitive("Schema for Server-Driven UI tokens"))
            put("type", JsonPrimitive("object"))
            put("definitions", definitions)

            // The root schema allows any token type
            put("oneOf", buildJsonArray {
                TokenRegistry.tokenTypes.forEach { tokenClass ->
                    val tokenName = tokenClass.simpleName ?: return@forEach
                    add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/$tokenName")) })
                }
            })
        }
    }

    /**
     * Generates a schema for a token type using reflection.
     * @param tokenClass The token class to generate schema for.
     * @return A JsonObject containing the schema for the token type.
     */
    private fun generateTokenSchema(tokenClass: KClass<*>): JsonObject {
        val tokenAnnotation = tokenClass.findAnnotation<TokenType>()
        val description = tokenAnnotation?.description ?: "A UI token"

        return buildJsonObject {
            put("type", JsonPrimitive("object"))
            put("title", JsonPrimitive(tokenClass.simpleName))
            put("description", JsonPrimitive(description))

            // Add allOf for inheritance
            val superClasses = tokenClass.superclasses.filter { it != Any::class }
            if (superClasses.isNotEmpty()) {
                put("allOf", buildJsonArray {
                    superClasses.forEach { superClass ->
                        val superName = superClass.simpleName ?: return@forEach
                        add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/$superName")) })
                    }
                })
            }

            // Add properties
            put("properties", generatePropertiesSchema(tokenClass))

            // Add required properties
            val requiredProperties = tokenClass.memberProperties
                .filter { it.findAnnotation<Required>() != null || it.findAnnotation<TokenProperty>()?.required == true }
                .mapNotNull { it.name }

            if (requiredProperties.isNotEmpty()) {
                put("required", buildJsonArray {
                    requiredProperties.forEach { add(JsonPrimitive(it)) }
                })
            }
        }
    }

    /**
     * Generates a schema for a value object using reflection.
     * @param valueClass The value class to generate schema for.
     * @return A JsonObject containing the schema for the value object.
     */
    private fun generateValueSchema(valueClass: KClass<*>): JsonObject {
        return buildJsonObject {
            put("type", JsonPrimitive("object"))
            put("title", JsonPrimitive(valueClass.simpleName))
            put("description", JsonPrimitive("Value object"))

            // Add properties
            put("properties", generatePropertiesSchema(valueClass))

            // Add required properties
            val requiredProperties = valueClass.memberProperties
                .filter { it.findAnnotation<Required>() != null || it.findAnnotation<TokenProperty>()?.required == true }
                .mapNotNull { it.name }

            if (requiredProperties.isNotEmpty()) {
                put("required", buildJsonArray {
                    requiredProperties.forEach { add(JsonPrimitive(it)) }
                })
            }
        }
    }

    /**
     * Generates a schema for the properties of a class using reflection.
     * @param klass The class to generate property schema for.
     * @return A JsonObject containing the schema for the properties.
     */
    private fun generatePropertiesSchema(klass: KClass<*>): JsonObject {
        return buildJsonObject {
            klass.memberProperties.forEach { property ->
                val propertySchema = generatePropertySchema(property)
                if (propertySchema != null) {
                    put(property.name, propertySchema)
                }
            }
        }
    }

    /**
     * Generates a schema for a property using reflection and annotations.
     * @param property The property to generate schema for.
     * @return A JsonObject containing the schema for the property, or null if the property should be skipped.
     */
    private fun generatePropertySchema(property: KProperty1<*, *>): JsonObject? {
        // Check for annotations
        val schemaProperty = property.findAnnotation<SchemaProperty>()
        val schemaEnum = property.findAnnotation<SchemaEnum>()
        val schemaRef = property.findAnnotation<SchemaRef>()
        val schemaChildren = property.findAnnotation<SchemaChildren>()
        val tokenProperty = property.findAnnotation<TokenProperty>()

        // Get description from annotations
        val description = when {
            schemaProperty != null -> schemaProperty.description
            schemaEnum != null -> schemaEnum.description
            schemaRef != null -> schemaRef.description
            schemaChildren != null -> schemaChildren.description
            tokenProperty != null -> tokenProperty.description
            else -> ""
        }

        return when {
            // Reference to another schema definition
            schemaRef != null -> {
                val refName = schemaRef.ref.ifEmpty { property.returnType.classifier?.let { (it as? KClass<*>)?.simpleName } ?: "Unknown" }
                buildJsonObject {
                    put("\$ref", JsonPrimitive("#/definitions/$refName"))
                    if (description.isNotEmpty()) {
                        put("description", JsonPrimitive(description))
                    }
                }
            }

            // Enum property
            schemaEnum != null -> {
                val enumClass = property.returnType.classifier as? KClass<*> ?: return null
                if (!enumClass.java.isEnum) return null

                buildJsonObject {
                    put("type", JsonPrimitive("string"))
                    put("enum", buildJsonArray {
                        enumClass.java.enumConstants.forEach { enumValue ->
                            add(JsonPrimitive(enumValue.toString()))
                        }
                    })
                    if (description.isNotEmpty()) {
                        put("description", JsonPrimitive(description))
                    }
                }
            }

            // Children property
            schemaChildren != null -> {
                buildJsonObject {
                    put("type", JsonPrimitive("array"))
                    put("description", JsonPrimitive(description.ifEmpty { "Child tokens" }))
                    put("items", buildJsonObject {
                        put("oneOf", buildJsonArray {
                            TokenRegistry.tokenTypes.forEach { tokenClass ->
                                val tokenName = tokenClass.simpleName ?: return@forEach
                                add(buildJsonObject { put("\$ref", JsonPrimitive("#/definitions/$tokenName")) })
                            }
                        })
                    })
                }
            }

            // Regular property
            else -> {
                val type = when (property.returnType.classifier) {
                    Int::class -> "integer"
                    Float::class, Double::class -> "number"
                    Boolean::class -> "boolean"
                    String::class -> "string"
                    else -> return null // Skip complex types without annotations
                }

                buildJsonObject {
                    put("type", JsonPrimitive(type))
                    if (description.isNotEmpty()) {
                        put("description", JsonPrimitive(description))
                    }

                    // Add min/max for numeric types
                    if (type == "integer" || type == "number") {
                        schemaProperty?.let {
                            if (it.minimum != Int.MIN_VALUE) {
                                put("minimum", JsonPrimitive(it.minimum))
                            }
                            if (it.maximum != Int.MAX_VALUE) {
                                put("maximum", JsonPrimitive(it.maximum))
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Registry for token and value types that should be included in the schema.
 */
object TokenRegistry {
    /**
     * List of token types to include in the schema.
     */
    val tokenTypes = mutableListOf<KClass<*>>()

    /**
     * List of value types to include in the schema.
     */
    val valueTypes = mutableListOf<KClass<*>>()

    /**
     * Register a token type to be included in the schema.
     */
    fun registerTokenType(tokenClass: KClass<*>) {
        tokenTypes.add(tokenClass)
    }

    /**
     * Register a value type to be included in the schema.
     */
    fun registerValueType(valueClass: KClass<*>) {
        valueTypes.add(valueClass)
    }

    /**
     * Register multiple token types to be included in the schema.
     */
    fun registerTokenTypes(vararg tokenClasses: KClass<*>) {
        tokenClasses.forEach { registerTokenType(it) }
    }

    /**
     * Register multiple value types to be included in the schema.
     */
    fun registerValueTypes(vararg valueClasses: KClass<*>) {
        valueClasses.forEach { registerValueType(it) }
    }

    /**
     * Clear all registered types.
     */
    fun clear() {
        tokenTypes.clear()
        valueTypes.clear()
    }
}