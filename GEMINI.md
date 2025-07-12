# Gemini Project Overview: Dynamic-Token SDUI Engine

This project is a Compose Multiplatform application designed to act as a dynamic, server-driven UI (SDUI) rendering engine. The goal is to create a single, shareable rendering core written in Kotlin that can be deployed across Android, iOS, Desktop, and WebAssembly (WasmJs) targets.

## Core Concepts

The system is based on a few key concepts outlined in the `requirements.md` document:

-   **Primitives:** Fundamental UI building blocks (`Column`, `Text`, etc.) that are hard-coded into the client application and versioned.
-   **Design Tokens:** JSON definitions that describe a piece of UI by referencing primitives and their parameters (e.g., padding, color). These are fetched from a server.
-   **Core-Token Pack:** A bundle of the most frequently used design tokens that is embedded directly into the application binary to reduce initial network latency.
-   **Token Repository:** A client-side layer responsible for fetching, caching (in-memory and on-disk), and providing design tokens to the UI layer.
-   **Primitive Version Contract:** A contract between the client and server, where the client sends a header (`X-Primitives`) listing the primitives it supports and their versions, ensuring compatibility.

## Project Structure

The project is organized into a multi-module Gradle setup:

-   `shared`: A Kotlin Multiplatform module containing the core business logic. This is where the rendering engine, `TokenRepository`, data models, and primitive definitions will reside. It is the central, shareable component.
-   `composeApp`: A Compose Multiplatform module that contains the platform-specific entry points (Android, iOS, Desktop, WasmJs) and UI-related code for bootstrapping the application on each target. It depends on the `shared` module.
-   `server`: A Ktor-based server application responsible for serving `ScreenPayload` and `DesignToken` data to the clients. It will validate client primitive versions and provide the appropriate UI definitions.
-   `iosApp`: The Xcode project required to build and run the application on iOS.

## Development Workflow

1.  **Define Primitives:** Add or update UI primitives in the `shared` module.
2.  **Implement Rendering Logic:** The `shared` module will contain a renderer that can take a `DesignToken` and map it to the corresponding Composable primitive.
3.  **Develop Server Endpoints:** The `server` module will expose endpoints (`/screens/{id}`, `/design-tokens`) to provide UI data.
4.  **Platform Integration:** Each platform in `composeApp` will initialize the `TokenRepository` and use it to fetch and render screens.
5.  **Build & Deploy:** The Gradle build system compiles all modules, including the platform-specific application bundles and the server artifact.
