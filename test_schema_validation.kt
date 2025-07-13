import com.baksha97.sdui.shared.models.*

fun main() {
    println("Testing enhanced schema validation...")
    
    val registry = TokenRegistry()
    
    // Test 1: Valid token should pass validation
    val validToken = TextToken(
        id = "valid_text",
        version = 1,
        text = TemplateString("Hello World"),
        color = ColorValue(red = 255, green = 0, blue = 0, alpha = 255)
    )
    
    registry.registerWithValidation(validToken)
    val validationErrors = registry.validateRegistry()
    println("Valid token validation errors: ${validationErrors.size}")
    
    // Test 2: Invalid color values should be caught
    val invalidColorToken = TextToken(
        id = "invalid_color",
        version = 1,
        text = TemplateString("Invalid Color"),
        color = ColorValue(red = 300, green = -50, blue = 0, alpha = 500) // Invalid values
    )
    
    registry.register(invalidColorToken)
    val colorValidationErrors = registry.validateRegistry()
    println("Invalid color validation errors: ${colorValidationErrors.size}")
    colorValidationErrors.forEach { println("  - $it") }
    
    // Test 3: Invalid action should be caught
    val invalidActionButton = ButtonToken(
        id = "invalid_action",
        version = 1,
        text = TemplateString("Click me"),
        onClick = Action(ActionType.Navigate, emptyMap()) // Missing required 'target' data
    )
    
    registry.register(invalidActionButton)
    val actionValidationErrors = registry.validateRegistry()
    println("Invalid action validation errors: ${actionValidationErrors.size}")
    actionValidationErrors.forEach { println("  - $it") }
    
    // Test 4: Invalid slider range
    val invalidSlider = SliderToken(
        id = "invalid_slider",
        version = 1,
        initialValue = 5.0f,
        valueRange = 0f..1f // Initial value outside range
    )
    
    registry.register(invalidSlider)
    val sliderValidationErrors = registry.validateRegistry()
    println("Invalid slider validation errors: ${sliderValidationErrors.size}")
    sliderValidationErrors.forEach { println("  - $it") }
    
    println("Schema validation testing completed!")
}