# SDUI DSL CLI - Annotation-Based Optimization

This project provides an annotation-based approach to optimize the Server-Driven UI (SDUI) framework. The optimizations focus on reducing boilerplate code, automating schema generation, and simplifying version management.

## Key Features

### 1. Annotations for Schema Generation

The `com.baksha97.sdui.dsl.annotations` package provides annotations for schema generation:

- `@TokenType` - Marks a class as a token type with description and default version
- `@Required` - Marks a property as required in the schema
- `@SchemaProperty` - Provides schema information for a property
- `@SchemaEnum` - Marks a property as an enum in the schema
- `@SchemaRef` - Marks a property as a reference to another schema definition
- `@SchemaChildren` - Marks a property as a container for child tokens

### 2. Annotations for Token Properties

The `com.baksha97.sdui.dsl.annotations` package also provides annotations for token properties:

- `@BaseToken` - Marks a class as a base token
- `@VersionInfo` - Provides version information for a token
- `@TokenProperty` - Marks a property as a common token property
- `@Migrateable` - Marks a property as a migrateable property

### 3. Reflection-Based Schema Generator

The `ReflectionSchemaGenerator` class uses reflection and annotations to automatically generate JSON Schema for token types. This replaces the manual schema generation in `SchemaGenerator` with a more automated approach.

### 4. Reflection-Based Version Manager

The `ReflectionVersionManager` class uses reflection and annotations to handle token migration. This replaces the manual migration code in `VersionManager` with a more automated approach.

## Usage

### 1. Define Annotated Token Models

```kotlin
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
    val style: TextStyle = TextStyle.BodyMedium
) : Token {
    override val minSupportedVersion: Int = 1
    
    companion object {
        // Custom migration function for the style property
        fun migrateStyle(value: TextStyle?, targetVersion: Int): TextStyle {
            // Example migration logic
            if (targetVersion >= 2 && value == TextStyle.BodySmall) {
                return TextStyle.BodyMedium
            }
            return value ?: TextStyle.BodyMedium
        }
    }
}
```

### 2. Register Token Types

```kotlin
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
```

### 3. Generate Schema

```kotlin
val schemaGenerator = ReflectionSchemaGenerator()
val schema = schemaGenerator.generateTokenSchema()
```

### 4. Migrate Tokens

```kotlin
val versionManager = ReflectionVersionManager()
val migratedToken = versionManager.migrateToken(token, targetVersion)
```

## Benefits

- **Less Code Duplication**: Annotations reduce the need for repetitive code in token models, schema generation, and version management.
- **More Maintainable Codebase**: Changes to the schema or version management logic can be made in one place, rather than across multiple files.
- **Easier to Add New Token Types**: New token types can be added by simply defining a new class with the appropriate annotations.
- **Better Documentation**: Annotations provide self-documenting code that clearly indicates the purpose and requirements of each property.

## Example

See the `AnnotationExample` class for a complete example of how to use the annotation-based approach.

## Implementation Details

### Schema Generation

The `ReflectionSchemaGenerator` uses Kotlin reflection to:
- Iterate through registered token and value types
- Extract schema information from annotations
- Generate JSON Schema definitions for each type
- Handle inheritance through the "allOf" JSON Schema construct
- Generate property schemas based on property types and annotations

### Version Management

The `ReflectionVersionManager` uses Kotlin reflection to:
- Create a new instance of a token with the migrated properties
- Copy properties based on migration strategy
- Recursively migrate child tokens
- Apply custom migration functions for properties

## Future Improvements

- **Code Generation**: Use KSP (Kotlin Symbol Processing) to generate builder classes for annotated token models.
- **DSL Enhancement**: Enhance the DSL to work with annotated token models.
- **Compiler Plugin**: Develop a compiler plugin to optimize the code at compile time.