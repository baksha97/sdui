This is a Kotlin Multiplatform project targeting Android, iOS, Web, Desktop, Server.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* `/server` is for the Ktor server application.

* `/shared` is for the code that will be shared between all targets in the project.
  The most important subfolder is `commonMain`. If preferred, you can add code to the platform-specific folders here too.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).
If you face any issues, please report them on [YouTrack](https://youtrack.jetbrains.com/newIssue?project=CMP).

You can open the web application by running the `:composeApp:wasmJsBrowserDevelopmentRun` Gradle task.
# Server-Driven UI (SDUI) Framework

This is a Kotlin Multiplatform project implementing a flexible and robust Server-Driven UI (SDUI) framework. The project targets Android, iOS, Web, Desktop, and includes a server component.

## What is Server-Driven UI?

Server-Driven UI is an architectural pattern where the UI structure and content are defined on the server and sent to the client for rendering. This approach offers several advantages:

- **Consistent UI across platforms**: The same UI definition works on all platforms
- **Dynamic updates without app releases**: Change UI layouts and flows without requiring app updates
- **A/B testing and feature flagging**: Easily test different UI variations or roll out features to specific users
- **Reduced client-side complexity**: Move UI logic to the server, simplifying client implementation

## Architecture Overview

The SDUI system in this project is built around a token-based architecture:

1. **Tokens**: UI components represented as serializable data structures
2. **Token Registry**: A registry for managing and retrieving tokens
3. **Renderer**: A component that renders tokens into actual UI elements
4. **Screen Payload**: A collection of token references with their bindings

### Token Hierarchy

The system uses a hierarchical token structure:

- `Token`: Base interface for all UI components
- `ContainerToken`: Tokens that can contain other tokens (e.g., Column, Row, Box)
- `InteractiveToken`: Tokens that can respond to user interactions (e.g., Button, Card)

Specific token implementations include:
- Layout tokens: `ColumnToken`, `RowToken`, `BoxToken`, `LazyColumnToken`, `LazyRowToken`
- UI element tokens: `TextToken`, `ButtonToken`, `AsyncImageToken`, `CardToken`
- Utility tokens: `SpacerToken`, `DividerToken`

## Versioning and Compatibility

The system includes built-in versioning to ensure backward compatibility:

- Each token has a `version` property indicating its current version
- Tokens define a `minSupportedVersion` property to specify compatibility requirements
- The renderer checks version compatibility before rendering
- Incompatible tokens are rendered as fallback UI elements

This versioning system allows for evolving the UI components over time while maintaining compatibility with older clients.

## Extending the System

The SDUI framework is designed to be extensible:

### Adding New Token Types

1. Define a new token class implementing the `Token` interface
2. Add rendering logic for the new token in the `RenderToken` function
3. Register examples of the new token in the `TokenRegistry`

### Modifying Existing Tokens

When modifying existing tokens:
1. Increment the token's `version` property
2. Update the `minSupportedVersion` if the change is breaking
3. Add fallback behavior in the renderer for backward compatibility

## Best Practices for Evolution

To evolve the SDUI system with minimal breaking changes:

1. **Additive Changes**: Add new properties with default values rather than modifying existing ones
2. **Version Carefully**: Increment versions for all changes, but only update `minSupportedVersion` for breaking changes
3. **Fallback Rendering**: Provide graceful fallbacks for missing or incompatible properties
4. **Comprehensive Testing**: Test with different client versions to ensure compatibility
5. **Documentation**: Document all changes, especially breaking ones

## Project Structure

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that's common for all targets and contains the core SDUI implementation.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.

* `/iosApp` contains iOS applications. Even if you're sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app.

* `/server` is for the Ktor server application that can serve UI definitions.

* `/shared` is for the code that will be shared between all targets in the project.
  The most important subfolder is `commonMain`.

## Getting Started

You can run the application on different platforms:

- **Web**: Run the `:composeApp:wasmJsBrowserDevelopmentRun` Gradle task
- **Android**: Open the project in Android Studio and run the Android configuration
- **iOS**: Open the Xcode project in the `iosApp` directory and run it
- **Desktop**: Run the `:composeApp:desktopRun` Gradle task

## Learn More

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html),
[Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/#compose-multiplatform),
[Kotlin/Wasm](https://kotl.in/wasm/)…

We would appreciate your feedback on Compose/Web and Kotlin/Wasm in the public Slack channel [#compose-web](https://slack-chats.kotlinlang.org/c/compose-web).
If you face any issues, please report them on [YouTrack](https://youtrack.jetbrains.com/newIssue?project=CMP).