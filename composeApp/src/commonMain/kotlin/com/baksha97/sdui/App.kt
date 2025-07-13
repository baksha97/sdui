package com.baksha97.sdui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.baksha97.sdui.shared.models.*

/**
 * Enhanced SDUI Demo App with DSL Playground and Live Preview
 * Demonstrates the full capabilities of the SDUI DSL with interactive examples
 */
@Composable
fun App() {
    MaterialTheme {
        var currentTab by remember { mutableStateOf(0) }
        val tabs = listOf("Examples", "UI Builder", "Live Preview")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
        ) {
            // Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SDUI DSL Demonstration",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Interactive examples and live DSL editor",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }

            // Tab Navigation
            TabRow(selectedTabIndex = currentTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = currentTab == index,
                        onClick = { currentTab = index },
                        text = { Text(title) }
                    )
                }
            }

            // Tab Content
            when (currentTab) {
                0 -> ExamplesTab()
                1 -> DSLPlaygroundTab()
                2 -> LivePreviewTab()
            }
        }
    }
}

/**
 * Examples Tab - Shows comprehensive examples of DSL components
 */
@Composable
fun ExamplesTab() {
    var selectedExample by remember { mutableStateOf(0) }
    var lastAction by remember { mutableStateOf<String?>(null) }
    var showActionLog by remember { mutableStateOf(false) }

    val examples = listOf(
        "Basic Components",
        "Layout Examples", 
        "Interactive Elements",
        "Complex Forms",
        "Image Gallery",
        "Data Lists"
    )

    // Action handler for interactive elements
    val actionHandler: (Action, Map<String, Any>) -> Unit = { action, bindings ->
        lastAction = "Action: ${action.type}, Data: ${action.data}, Bindings: $bindings"
        showActionLog = true
        println(lastAction)
    }

    Row(modifier = Modifier.fillMaxSize()) {
        // Example selector
        Card(
            modifier = Modifier
                .width(200.dp)
                .fillMaxHeight()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Examples",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                examples.forEachIndexed { index, example ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedExample == index) 
                                MaterialTheme.colorScheme.primaryContainer 
                            else MaterialTheme.colorScheme.surface
                        ),
                        onClick = { selectedExample = index }
                    ) {
                        Text(
                            text = example,
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        // Example content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(8.dp)
        ) {
            // Action Log
            AnimatedVisibility(showActionLog) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Action Log:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = lastAction ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Button(
                            onClick = { showActionLog = false },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text("Dismiss")
                        }
                    }
                }
            }

            // Example content
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    when (selectedExample) {
                        0 -> BasicComponentsExample(actionHandler)
                        1 -> LayoutExamples(actionHandler)
                        2 -> InteractiveElementsExample(actionHandler)
                        3 -> ComplexFormsExample(actionHandler)
                        4 -> ImageGalleryExample(actionHandler)
                        5 -> DataListsExample(actionHandler)
                    }
                }
            }
        }
    }
}

/**
 * UI Builder Tab - Visual UI builder using the registry
 */
@Composable
fun DSLPlaygroundTab() {
    var builtTokens by remember { mutableStateOf<List<Token>>(emptyList()) }
    var selectedTokenIndex by remember { mutableStateOf<Int?>(null) }
    var lastAction by remember { mutableStateOf<String?>(null) }
    var showActionLog by remember { mutableStateOf(false) }
    var generatedDSL by remember { mutableStateOf("") }

    // Action handler for interactive elements
    val actionHandler: (Action, Map<String, Any>) -> Unit = { action, bindings ->
        lastAction = "Action: ${action.type}, Data: ${action.data}, Bindings: $bindings"
        showActionLog = true
        println(lastAction)
    }

    // Available component types for the palette
    val componentTypes = listOf(
        "Text" to "Add a text component",
        "Button" to "Add a button component", 
        "Column" to "Add a column layout",
        "Row" to "Add a row layout",
        "Box" to "Add a box layout",
        "Card" to "Add a card container",
        "Spacer" to "Add spacing",
        "Divider" to "Add a divider line",
        "Slider" to "Add a slider input"
    )

    // Counter for generating unique IDs
    var componentCounter by remember { mutableStateOf(0) }

    // Function to add a component to the canvas
    fun addComponent(type: String) {
        componentCounter++
        val newToken = when (type) {
            "Text" -> TextToken(
                id = "text_$componentCounter",
                version = 1,
                text = TemplateString("Sample Text"),
                style = TextStyle.BodyMedium
            )
            "Button" -> ButtonToken(
                id = "button_$componentCounter",
                version = 1,
                text = TemplateString("Click Me"),
                onClick = Action(ActionType.CUSTOM, mapOf("action" to "button_clicked"))
            )
            "Column" -> ColumnToken(
                id = "column_$componentCounter",
                version = 1,
                children = emptyList()
            )
            "Row" -> RowToken(
                id = "row_$componentCounter",
                version = 1,
                children = emptyList()
            )
            "Box" -> BoxToken(
                id = "box_$componentCounter",
                version = 1,
                children = emptyList()
            )
            "Card" -> CardToken(
                id = "card_$componentCounter",
                version = 1,
                children = emptyList()
            )
            "Spacer" -> SpacerToken(
                id = "spacer_$componentCounter",
                version = 1,
                height = 16
            )
            "Divider" -> DividerToken(
                id = "divider_$componentCounter",
                version = 1,
                thickness = 1
            )
            "Slider" -> SliderToken(
                id = "slider_$componentCounter",
                version = 1,
                initialValue = 0.5f,
                valueRange = 0f..1f
            )
            else -> return
        }
        builtTokens = builtTokens + newToken
    }

    // Function to remove a component
    fun removeComponent(index: Int) {
        builtTokens = builtTokens.filterIndexed { i, _ -> i != index }
        selectedTokenIndex = null
    }

    // Function to generate DSL code from built tokens
    fun generateDSL() {
        if (builtTokens.isEmpty()) {
            generatedDSL = "// Your generated DSL will appear here\n// Add components from the palette to get started"
            return
        }

        val dslBuilder = StringBuilder()
        dslBuilder.append("Column {\n")

        builtTokens.forEach { token ->
            when (token) {
                is TextToken -> {
                    dslBuilder.append("    text {\n")
                    dslBuilder.append("        textContent = \"${token.text.raw}\"\n")
                    dslBuilder.append("        style = TextStyle.${token.style.name}\n")
                    dslBuilder.append("    }\n\n")
                }
                is ButtonToken -> {
                    dslBuilder.append("    button {\n")
                    dslBuilder.append("        textContent = \"${token.text.raw}\"\n")
                    dslBuilder.append("        onClick {\n")
                    dslBuilder.append("            type = ActionType.${token.onClick.type.name}\n")
                    token.onClick.data.forEach { (key, value) ->
                        dslBuilder.append("            data(\"$key\" to \"$value\")\n")
                    }
                    dslBuilder.append("        }\n")
                    dslBuilder.append("    }\n\n")
                }
                is SpacerToken -> {
                    dslBuilder.append("    spacer {\n")
                    dslBuilder.append("        height = ${token.height}\n")
                    dslBuilder.append("    }\n\n")
                }
                is DividerToken -> {
                    dslBuilder.append("    divider {\n")
                    dslBuilder.append("        thickness = ${token.thickness}\n")
                    dslBuilder.append("    }\n\n")
                }
                is SliderToken -> {
                    dslBuilder.append("    slider {\n")
                    dslBuilder.append("        initialValue = ${token.initialValue}f\n")
                    dslBuilder.append("        valueRange = ${token.valueRange.start}f..${token.valueRange.endInclusive}f\n")
                    dslBuilder.append("    }\n\n")
                }
                else -> {
                    dslBuilder.append("    // ${token::class.simpleName} (${token.id})\n\n")
                }
            }
        }

        dslBuilder.append("}")
        generatedDSL = dslBuilder.toString()
    }

    Row(modifier = Modifier.fillMaxSize()) {
        // Component Palette
        Card(
            modifier = Modifier
                .width(250.dp)
                .fillMaxHeight()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Component Palette",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                componentTypes.forEach { (type, description) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        onClick = { addComponent(type) }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = type,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Clear all button
                if (builtTokens.isNotEmpty()) {
                    OutlinedButton(
                        onClick = { 
                            builtTokens = emptyList()
                            selectedTokenIndex = null
                            componentCounter = 0
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Clear All")
                    }
                }
            }
        }

        // Canvas and Preview
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(8.dp)
        ) {
            // Action Log
            AnimatedVisibility(showActionLog) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Action Log:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = lastAction ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Button(
                            onClick = { showActionLog = false },
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text("Dismiss")
                        }
                    }
                }
            }

            Row(modifier = Modifier.weight(1f)) {
                // Canvas - Built Components List
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(end = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Canvas (${builtTokens.size} components)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        if (builtTokens.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Click components from the palette\nto add them to your UI",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                builtTokens.forEachIndexed { index, token ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (selectedTokenIndex == index)
                                                MaterialTheme.colorScheme.tertiaryContainer
                                            else MaterialTheme.colorScheme.surfaceVariant
                                        ),
                                        onClick = { selectedTokenIndex = if (selectedTokenIndex == index) null else index }
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(12.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = token::class.simpleName ?: "Unknown",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = FontWeight.Medium
                                                )
                                                Text(
                                                    text = token.id,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                                )
                                            }

                                            IconButton(
                                                onClick = { removeComponent(index) }
                                            ) {
                                                Text("×", style = MaterialTheme.typography.titleLarge)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Live Preview
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(start = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Live Preview",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(16.dp)
                        ) {
                            if (builtTokens.isNotEmpty()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(rememberScrollState()),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    builtTokens.forEach { token ->
                                        RenderToken(
                                            token = token,
                                            bindings = emptyMap(),
                                            onAction = actionHandler
                                        )
                                    }
                                }
                            } else {
                                Text(
                                    text = "Preview will appear here\nwhen you add components",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.align(Alignment.Center),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            // Generated DSL Code
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(top = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Generated DSL Code",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            text = generatedDSL,
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.verticalScroll(rememberScrollState())
                        )
                    }
                }
            }
        }
    }

    // Generate DSL whenever builtTokens changes
    LaunchedEffect(builtTokens) {
        generateDSL()
    }
}

/**
 * Live Preview Tab - Shows real-time DSL rendering
 */
@Composable
fun LivePreviewTab() {
    var specSource by remember { mutableStateOf(SpecSource.LOCAL) }
    var isLoading by remember { mutableStateOf(false) }
    var screen by remember { mutableStateOf<com.baksha97.sdui.shared.models.ScreenPayload?>(null) }
    var registry by remember { mutableStateOf<com.baksha97.sdui.shared.models.TokenRegistry?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var lastAction by remember { mutableStateOf<String?>(null) }
    var showActionLog by remember { mutableStateOf(false) }

    // Create the spec provider based on the selected source
    val specProvider = remember(specSource) { 
        SpecProviderFactoryImpl.create(specSource)
    }

    // Coroutine scope for loading specs
    val coroutineScope = rememberCoroutineScope()

    // Function to load specs
    fun loadSpecs(screenId: String = "enhanced_home") {
        isLoading = true
        errorMessage = null
        coroutineScope.launch {
            try {
                registry = specProvider.getRegistry()
                screen = specProvider.getScreen(screenId)

                if (screen == null) {
                    errorMessage = "Screen not found: $screenId"
                }
            } catch (e: Exception) {
                errorMessage = "Error loading specs: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    // Load specs on first composition
    LaunchedEffect(specSource) {
        loadSpecs()
    }

    // Action handler for interactive elements
    val actionHandler: (Action, Map<String, Any>) -> Unit = { action, bindings ->
        lastAction = "Action: ${action.type}, Data: ${action.data}, Bindings: $bindings"
        showActionLog = true
        println(lastAction)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Controls
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Live Preview Controls",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Spec Source:")

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = specSource == SpecSource.LOCAL,
                            onClick = { specSource = SpecSource.LOCAL }
                        )
                        Text("Local")
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = specSource == SpecSource.SERVER,
                            onClick = { specSource = SpecSource.SERVER }
                        )
                        Text("Server")
                    }

                    Button(onClick = { loadSpecs() }) {
                        Text("Refresh")
                    }
                }
            }
        }

        // Action Log
        AnimatedVisibility(showActionLog) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Action Log:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = lastAction ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Button(
                        onClick = { showActionLog = false },
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        }

        // Content
        Card(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    errorMessage != null -> {
                        Text(
                            text = errorMessage!!,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    screen != null && registry != null -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            RenderScreen(
                                screen = screen!!,
                                registry = registry!!,
                                onMissingToken = { tokenId ->
                                    println("Missing token: $tokenId")
                                },
                                onAction = actionHandler
                            )
                        }
                    }

                    else -> {
                        Text(
                            text = "No content available",
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Example functions for demonstrating DSL components
 */
@Composable
fun BasicComponentsExample(actionHandler: (Action, Map<String, Any>) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Basic Components Example",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text("This demonstrates basic SDUI components using the DSL:")

        // Show DSL code
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Text(
                text = """
Column {
    text {
        textContent = "Welcome to SDUI!"
        style = TextStyle.HeadlineMedium
    }

    button {
        textContent = "Click Me"
        onClick {
            type = ActionType.Custom
            data("action" to "button_clicked")
        }
    }

    spacer { height = 16 }

    divider { thickness = 2 }
}
                """.trimIndent(),
                modifier = Modifier.padding(16.dp),
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Show rendered result
        Text("Rendered Result:", fontWeight = FontWeight.Medium)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Welcome to SDUI!",
                    style = MaterialTheme.typography.headlineMedium
                )

                Button(
                    onClick = { 
                        actionHandler(
                            Action(ActionType.CUSTOM, mapOf("action" to "button_clicked")),
                            emptyMap()
                        )
                    }
                ) {
                    Text("Click Me")
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(thickness = 2.dp)
            }
        }
    }
}

@Composable
fun LayoutExamples(actionHandler: (Action, Map<String, Any>) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Layout Examples",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text("Demonstrates different layout containers:")

        // Row example
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Row Layout:", fontWeight = FontWeight.Medium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(3) { index ->
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Text(
                                text = "Item ${index + 1}",
                                modifier = Modifier.padding(16.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // Box example
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Box Layout:", fontWeight = FontWeight.Medium)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Centered Content")
                }
            }
        }
    }
}

@Composable
fun InteractiveElementsExample(actionHandler: (Action, Map<String, Any>) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Interactive Elements",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text("Demonstrates interactive SDUI components:")

        var sliderValue by remember { mutableStateOf(0.5f) }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Buttons:", fontWeight = FontWeight.Medium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { 
                            actionHandler(
                                Action(ActionType.CUSTOM, mapOf("button" to "primary")),
                                emptyMap()
                            )
                        }
                    ) {
                        Text("Primary")
                    }

                    OutlinedButton(
                        onClick = { 
                            actionHandler(
                                Action(ActionType.CUSTOM, mapOf("button" to "secondary")),
                                emptyMap()
                            )
                        }
                    ) {
                        Text("Secondary")
                    }
                }

                Text("Slider:", fontWeight = FontWeight.Medium)
                Slider(
                    value = sliderValue,
                    onValueChange = { 
                        sliderValue = it
                        actionHandler(
                            Action(ActionType.CUSTOM, mapOf("slider" to it.toString())),
                            emptyMap()
                        )
                    }
                )
                Text("Value: ${(sliderValue * 100).toInt()}%")
            }
        }
    }
}

@Composable
fun ComplexFormsExample(actionHandler: (Action, Map<String, Any>) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Complex Forms",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text("Demonstrates form-like layouts with multiple components:")

        var textValue by remember { mutableStateOf("") }
        var selectedOption by remember { mutableStateOf(0) }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("User Information Form", fontWeight = FontWeight.Medium)

                OutlinedTextField(
                    value = textValue,
                    onValueChange = { textValue = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Preferred Contact Method:")
                Column {
                    listOf("Email", "Phone", "SMS").forEachIndexed { index, option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedOption == index,
                                onClick = { selectedOption = index }
                            )
                            Text(option, modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { 
                            actionHandler(
                                Action(ActionType.CUSTOM, mapOf("form" to "cancel")),
                                emptyMap()
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = { 
                            actionHandler(
                                Action(ActionType.CUSTOM, mapOf(
                                    "form" to "submit",
                                    "name" to textValue,
                                    "contact" to listOf("Email", "Phone", "SMS")[selectedOption]
                                )),
                                emptyMap()
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}

@Composable
fun ImageGalleryExample(actionHandler: (Action, Map<String, Any>) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Image Gallery",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text("Demonstrates image components and grid layouts:")

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Image Grid:", fontWeight = FontWeight.Medium)

                // Simulate image grid with colored boxes
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(2) { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            repeat(3) { col ->
                                val colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.tertiary,
                                    MaterialTheme.colorScheme.error,
                                    MaterialTheme.colorScheme.outline,
                                    MaterialTheme.colorScheme.surfaceVariant
                                )

                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f),
                                    colors = CardDefaults.cardColors(
                                        containerColor = colors[(row * 3 + col) % colors.size]
                                    ),
                                    onClick = {
                                        actionHandler(
                                            Action(ActionType.CUSTOM, mapOf(
                                                "image" to "clicked",
                                                "position" to "${row}_$col"
                                            )),
                                            emptyMap()
                                        )
                                    }
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "${row * 3 + col + 1}",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DataListsExample(actionHandler: (Action, Map<String, Any>) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Data Lists",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text("Demonstrates list components and data presentation:")

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("User List:", fontWeight = FontWeight.Medium)

                val users = listOf(
                    "John Doe" to "john@example.com",
                    "Jane Smith" to "jane@example.com",
                    "Bob Johnson" to "bob@example.com",
                    "Alice Brown" to "alice@example.com"
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    users.forEachIndexed { index, (name, email) ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            onClick = {
                                actionHandler(
                                    Action(ActionType.CUSTOM, mapOf(
                                        "user" to "selected",
                                        "name" to name,
                                        "email" to email
                                    )),
                                    emptyMap()
                                )
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            MaterialTheme.colorScheme.primary,
                                            androidx.compose.foundation.shape.CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = name.first().toString(),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 16.dp)
                                ) {
                                    Text(
                                        text = name,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = email,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AppPreview() {
    App()
}
