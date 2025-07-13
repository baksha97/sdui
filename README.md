# Server‑Driven UI (SDUI) Framework

> **Hybrid sections + primitives**

This Kotlin Multiplatform project implements a *hybrid* SDUI system that combines **pre‑compiled sections** (for rich, high‑performance widgets) with **dynamic primitive trees** (for rapid iteration). It targets Android, iOS, Web, Desktop, and ships a Ktor‑based server.

---

## What is Server‑Driven UI?

Server‑Driven UI (SDUI) moves UI *structure* to the server while rendering occurs on the client. Benefits:

* **Unified look & feel** across all clients
* **Real‑time changes** without app‑store delays
* **A/B experiments & feature flags** in JSON, not binaries
* **Lean clients** – business logic belongs to the server

Our hybrid model adds two further benefits:

| Capability                              | Native sections           | Primitive trees         |
| --------------------------------------- | ------------------------- | ----------------------- |
| Powerful animations / gestures          | ✅ (compiled into the app) | 🚫                      |
| Ship new layouts without update         | 🚫                        | ✅                       |
| Works fully offline after cache warm‑up | ✅                         | ✅ (via directive cache) |

---

## High‑Level Architecture

```
╭─────────────╮     ╭──────────────╮
│    Server   │     │    Client    │
│┌───────────┐│     │┌────────────┐│
││ Screen API│◀────▶│ Section reg ││ ① top‑level sections only
│└───────────┘│     │└────────────┘│
│┌───────────┐│     │┌────────────┐│
││Directive  │◀────▶│DirectiveCache││ ② unknown → fetch primitives
│└───────────┘│     │└────────────┘│
╰─────────────╯     ╰──────────────╯
```

1. **Screen API** returns a JSON tree of **section nodes** only. Each node looks like:

   ```json
   { "t": "@Section", "id": "hero‑213", "type": "HeroBanner" }
   ```
2. The client resolves each node:

  * **Registry hit** → render using the native renderer.
  * **Miss** → ask **Directive API** for a *primitive* tree; cache the result keyed by `id` + `rev`.

### Registry handshake

At launch the client posts:

```json
{
  "appBuild": 14230,
  "primitiveSet": 3,
  "deviceSections": ["HeroBanner", "ReviewCarousel"]
}
```

*The server uses this to decide which sections can be sent natively.*

### Directive cache

* Key: `(sectionId, rev)`
* Value: primitive JSON tree + `expiresInSec`
* `GET /directives/{id}?rev=X&appBuild=Y` returns **200** (new tree), **304** (unchanged) or **410** (deprecated – purge cache).

---

## Token‑Based Rendering

Primitive trees consist of **tokens** – serialisable data objects mapped to Compose/SwiftUI views.

```kotlin
sealed interface Token { val id: String; val version: Int; val a11y: A11y? }
```

* **Container** tokens – `ColumnToken`, `RowToken`, `LazyColumnToken`, …
* **Leaf / interactive** tokens – `TextToken`, `ImageToken`, `SliderToken`, …

### Versioning

| Field                   | Applies to          | Purpose                     |
| ----------------------- | ------------------- | --------------------------- |
| `version`               | each token          | schema evolution (additive) |
| `primitiveSet`          | app build           | coarse client capability    |
| `appBuild` + `minBuild` | section & directive | prevent legacy cache bloat  |

When modifying a token:

1. **Additive** → bump `version`, keep `minSupportedVersion` unchanged.
2. **Breaking** → bump `version` *and* raise `minSupportedVersion`; old clients fall back to a placeholder.

---

## Local State Management

Interactive tokens (e.g. `SliderToken`, `ToggleToken`) manage state locally using Compose `remember`/SwiftUI `@State`. They may optionally fire **actions** back to the server.

Example (Kotlin Compose):

```kotlin
@Composable
fun RenderSlider(token: SliderToken, bindings: Map<String, Any>, onAction: (Action)->Unit) {
    var value by remember { mutableStateOf(token.initialValue) }
    Slider(value, onValueChange = { v ->
        value = v
        token.onChange?.let { onAction(it.with("value", v)) }
    })
}
```

---

## Workflow Cheatsheet

1. **New layout using existing primitives**  → edit JSON / CMS, deploy – *no client release*.
2. **New complex widget**

  1. Implement `HeroCarousel` on Android/iOS; call `SectionRegistry.register("HeroCarousel",::renderer)`.
  2. Add `"HeroCarousel"` to the handshake list.
  3. Server starts emitting the section once ≥ 95 % of DAU use a supported build.
  4. After 90 days raise `minBuild` so legacy clients stop caching the fallback directive.

---

## Schema Documentation

The SDUI system uses multiple schema layers for type safety and validation:

- **[Complete Schema Documentation](SCHEMA_DOCUMENTATION.md)** - Comprehensive guide to all schemas
- **Protocol Buffer Schema** (`schema/sdui.proto`) - Core token definitions
- **Kotlin Data Models** (`shared-models/`) - Runtime type-safe classes with validation
- **DSL Builder Schema** - Fluent API for programmatic UI construction
- **JSON Schema** - Generated validation schemas (use `dsl-cli schema` command)
- **Component Registration** - Annotation-based component management

### Schema Validation

The system provides multiple validation layers:

```kotlin
// Runtime validation
val registry = TokenRegistry()
val errors = registry.validateScreenPayload(screenPayload)
if (errors.isNotEmpty()) {
    // Handle validation errors
}

// Registration validation
registry.registerWithValidation(token) // Throws on invalid tokens
```

### Schema Tools

```bash
# Generate JSON schema for external validation
dsl-cli schema token-schema.json

# Validate components
dsl-cli validate component.json

# Generate sample components
dsl-cli generate profile-card output.json
```

---

## Project Layout

| Path                                          | Purpose                                                 |
| --------------------------------------------- | ------------------------------------------------------- |
| `composeApp/commonMain`                       | Shared Kotlin code incl. token definitions & renderer   |
| `composeApp/desktop`, `composeApp/android`, … | Platform‑specific bits (registry wiring, image loaders) |
| `iosApp`                                      | iOS entry point + Swift registry hooks                  |
| `server`                                      | Ktor service implementing Screen API & Directive API    |
| `shared`                                      | KMP shared models & actions                             |

---

## Getting Started

| Target         | Gradle Task / Action                      |
| -------------- | ----------------------------------------- |
| **Web (Wasm)** | `:composeApp:wasmJsBrowserDevelopmentRun` |
| **Android**    | Run in Android Studio                     |
| **iOS**        | Open `iosApp.xcodeproj` and run           |
| **Desktop**    | `:composeApp:desktopRun`                  |
| **Server**     | `:server:run`                             |

---

## Contributing & Feedback

* Discuss in **#compose-web** Slack.
* File bugs on YouTrack.
* PRs welcome — please follow the versioning guidelines above.
