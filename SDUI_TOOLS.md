# SDUI Tools

This document describes the tools available for working with the Server-Driven UI (SDUI) framework.

## DSL CLI Tool

The DSL CLI tool provides commands for generating, validating, rendering, and migrating UI components.

### Installation

The DSL CLI tool is included in the `dsl-cli` module of the project. You can build it using Gradle:

```bash
./gradlew :dsl-cli:build
```

### Commands

#### Generate a Component

```bash
dsl-cli generate <component-type> <output-file>
```

This command generates a JSON representation of a predefined component and writes it to the specified output file.

Available component types:
- `profile-card`: A card displaying a user profile
- `article-card`: A card displaying an article
- `enhanced-card`: A card with enhanced styling and a button
- `form`: A form component
- `slider`: A slider component
- `lazy-list`: A lazy-loading list component

Example:
```bash
dsl-cli generate profile-card profile-card.json
```

#### Generate a Schema

```bash
dsl-cli schema <output-file>
```

This command generates a JSON Schema for all token types and writes it to the specified output file. The schema can be used to validate UI definitions and provide documentation for the UI components.

Example:
```bash
dsl-cli schema token-schema.json
```

#### Render a Component

```bash
dsl-cli render <input-file> <output-file>
```

This command renders a UI component from a JSON file and writes the rendered component to the specified output file. This is useful for testing how a UI component will be rendered by the client.

Example:
```bash
dsl-cli render profile-card.json rendered-profile-card.json
```

#### Migrate a Component

```bash
dsl-cli migrate <input-file> <output-file> <target-version>
```

This command migrates a UI component from a JSON file to the specified target version and writes the migrated component to the specified output file. This is useful for testing how a UI component will be migrated when the schema changes.

Example:
```bash
dsl-cli migrate profile-card.json migrated-profile-card.json 2
```

## Schema Generation

The schema generator creates a JSON Schema definition for the UI components. This schema can be used to validate UI definitions and provide documentation for the UI components.

The schema includes:
- Definitions for all token types (ColumnToken, RowToken, BoxToken, etc.)
- Properties for each token type
- Required properties for each token type
- Enum values for properties with a fixed set of values
- Descriptions for each property

Example schema for a TextToken:
```json
{
  "type": "object",
  "title": "TextToken",
  "description": "A text component",
  "allOf": [
    {
      "$ref": "#/definitions/Token"
    }
  ],
  "properties": {
    "text": {
      "$ref": "#/definitions/TemplateString",
      "description": "Text content"
    },
    "style": {
      "type": "string",
      "enum": [
        "DisplayLarge",
        "DisplayMedium",
        "DisplaySmall",
        "HeadlineLarge",
        "HeadlineMedium",
        "HeadlineSmall",
        "TitleLarge",
        "TitleMedium",
        "TitleSmall",
        "BodyLarge",
        "BodyMedium",
        "BodySmall",
        "LabelLarge",
        "LabelMedium",
        "LabelSmall"
      ],
      "description": "Text style"
    },
    "color": {
      "$ref": "#/definitions/ColorValue"
    },
    "maxLines": {
      "type": "integer",
      "description": "Maximum number of lines"
    },
    "overflow": {
      "type": "string",
      "enum": [
        "Clip",
        "Ellipsis",
        "Visible"
      ],
      "description": "Text overflow behavior"
    },
    "textAlign": {
      "type": "string",
      "enum": [
        "Start",
        "Center",
        "End",
        "Justify",
        "Left",
        "Right"
      ],
      "description": "Text alignment"
    },
    "margin": {
      "$ref": "#/definitions/Margin"
    }
  },
  "required": [
    "text"
  ]
}
```

## Rendering UI Components

The renderer takes a JSON representation of a UI component and renders it as a native UI component. This allows the client to display UI components defined on the server.

The rendering process involves:
1. Parsing the JSON representation of the UI component
2. Determining the type of the UI component
3. Creating a native UI component based on the type
4. Setting the properties of the native UI component based on the JSON representation
5. Recursively rendering any child components

## Versioning Management

The version manager handles versioning and migrations between different versions of the UI components. This allows for backward compatibility and smooth transitions when making breaking changes.

When making breaking changes to a UI component, follow these steps:

1. Increment the version number of the component
2. Update the minSupportedVersion property if necessary
3. Implement migration logic in the version manager

The version manager provides methods for:
- Checking if a token is compatible with a specific client version
- Migrating tokens to a specific version
- Migrating token JSON to a specific version

Example migration logic:
```kotlin
fun migrateTextToken(token: TextToken, targetVersion: Int): TextToken {
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
```

## Conclusion

These tools provide a comprehensive solution for working with the Server-Driven UI framework. They allow you to generate, validate, render, and migrate UI components, making it easier to develop and maintain your SDUI application.