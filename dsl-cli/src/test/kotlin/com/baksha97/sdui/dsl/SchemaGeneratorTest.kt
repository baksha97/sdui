package com.baksha97.sdui.dsl

import org.junit.Test
import org.junit.Assert.*
import kotlinx.serialization.json.*

class SchemaGeneratorTest {

    private val schemaGenerator = SchemaGenerator()

    @Test
    fun `test generateTokenSchema creates valid schema`() {
        val schema = schemaGenerator.generateTokenSchema()
        
        // Verify the schema has the expected structure
        assertTrue(schema.containsKey("\$schema"))
        assertTrue(schema.containsKey("title"))
        assertTrue(schema.containsKey("description"))
        assertTrue(schema.containsKey("type"))
        assertTrue(schema.containsKey("definitions"))
        assertTrue(schema.containsKey("oneOf"))
        
        // Verify the definitions section contains all expected token types
        val definitions = schema["definitions"]?.jsonObject
        assertNotNull(definitions)
        
        // Check for base token types
        assertTrue(definitions!!.containsKey("Token"))
        assertTrue(definitions.containsKey("ContainerToken"))
        assertTrue(definitions.containsKey("InteractiveToken"))
        
        // Check for concrete token types
        assertTrue(definitions.containsKey("ColumnToken"))
        assertTrue(definitions.containsKey("RowToken"))
        assertTrue(definitions.containsKey("BoxToken"))
        assertTrue(definitions.containsKey("CardToken"))
        assertTrue(definitions.containsKey("TextToken"))
        assertTrue(definitions.containsKey("ButtonToken"))
        assertTrue(definitions.containsKey("SpacerToken"))
        assertTrue(definitions.containsKey("DividerToken"))
        assertTrue(definitions.containsKey("SliderToken"))
        assertTrue(definitions.containsKey("AsyncImageToken"))
        assertTrue(definitions.containsKey("LazyColumnToken"))
        assertTrue(definitions.containsKey("LazyRowToken"))
        
        // Check for value objects
        assertTrue(definitions.containsKey("A11y"))
        assertTrue(definitions.containsKey("Padding"))
        assertTrue(definitions.containsKey("Margin"))
        assertTrue(definitions.containsKey("Background"))
        assertTrue(definitions.containsKey("Action"))
        assertTrue(definitions.containsKey("ColorValue"))
        assertTrue(definitions.containsKey("ErrorFallback"))
        assertTrue(definitions.containsKey("LoadingPlaceholder"))
        assertTrue(definitions.containsKey("TemplateString"))
    }
    
    @Test
    fun `test token schema contains required properties`() {
        val schema = schemaGenerator.generateTokenSchema()
        val definitions = schema["definitions"]?.jsonObject
        
        // Check that Token has required properties
        val tokenSchema = definitions?.get("Token")?.jsonObject
        assertNotNull(tokenSchema)
        val tokenRequired = tokenSchema?.get("required")?.jsonArray
        assertNotNull(tokenRequired)
        assertTrue(tokenRequired!!.contains(JsonPrimitive("id")))
        assertTrue(tokenRequired.contains(JsonPrimitive("version")))
        
        // Check that ContainerToken has required properties
        val containerTokenSchema = definitions?.get("ContainerToken")?.jsonObject
        assertNotNull(containerTokenSchema)
        val containerTokenRequired = containerTokenSchema?.get("required")?.jsonArray
        assertNotNull(containerTokenRequired)
        assertTrue(containerTokenRequired!!.contains(JsonPrimitive("children")))
        
        // Check that TextToken has required properties
        val textTokenSchema = definitions?.get("TextToken")?.jsonObject
        assertNotNull(textTokenSchema)
        val textTokenRequired = textTokenSchema?.get("required")?.jsonArray
        assertNotNull(textTokenRequired)
        assertTrue(textTokenRequired!!.contains(JsonPrimitive("text")))
        
        // Check that ButtonToken has required properties
        val buttonTokenSchema = definitions?.get("ButtonToken")?.jsonObject
        assertNotNull(buttonTokenSchema)
        val buttonTokenRequired = buttonTokenSchema?.get("required")?.jsonArray
        assertNotNull(buttonTokenRequired)
        assertTrue(buttonTokenRequired!!.contains(JsonPrimitive("text")))
        assertTrue(buttonTokenRequired.contains(JsonPrimitive("onClick")))
    }
    
    @Test
    fun `test token schema contains enum values`() {
        val schema = schemaGenerator.generateTokenSchema()
        val definitions = schema["definitions"]?.jsonObject
        
        // Check that TextStyle has enum values
        val textTokenSchema = definitions?.get("TextToken")?.jsonObject
        assertNotNull(textTokenSchema)
        val styleProperty = textTokenSchema?.get("properties")?.jsonObject?.get("style")?.jsonObject
        assertNotNull(styleProperty)
        val styleEnum = styleProperty?.get("enum")?.jsonArray
        assertNotNull(styleEnum)
        assertTrue(styleEnum!!.contains(JsonPrimitive("BodyMedium")))
        assertTrue(styleEnum.contains(JsonPrimitive("HeadlineLarge")))
        
        // Check that ButtonStyle has enum values
        val buttonTokenSchema = definitions?.get("ButtonToken")?.jsonObject
        assertNotNull(buttonTokenSchema)
        val buttonStyleProperty = buttonTokenSchema?.get("properties")?.jsonObject?.get("style")?.jsonObject
        assertNotNull(buttonStyleProperty)
        val buttonStyleEnum = buttonStyleProperty?.get("enum")?.jsonArray
        assertNotNull(buttonStyleEnum)
        assertTrue(buttonStyleEnum!!.contains(JsonPrimitive("Filled")))
        assertTrue(buttonStyleEnum.contains(JsonPrimitive("Outlined")))
        
        // Check that ActionType has enum values
        val actionSchema = definitions?.get("Action")?.jsonObject
        assertNotNull(actionSchema)
        val typeProperty = actionSchema?.get("properties")?.jsonObject?.get("type")?.jsonObject
        assertNotNull(typeProperty)
        val typeEnum = typeProperty?.get("enum")?.jsonArray
        assertNotNull(typeEnum)
        assertTrue(typeEnum!!.contains(JsonPrimitive("NAVIGATE")))
        assertTrue(typeEnum.contains(JsonPrimitive("CUSTOM")))
    }
}