package com.baksha97.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CompositeTokenTest {

    @Test
    fun testComposeFunction() {
        // Create some test tokens
        val token1 = text {
            text("Token 1")
        }
        
        val token2 = text {
            text("Token 2")
        }
        
        // Test compose with list
        val composedList = compose(
            id = "composed_list",
            tokens = listOf(token1, token2),
            version = 1
        )
        
        assertEquals("composed_list", composedList.id)
        assertEquals(1, composedList.version)
        assertEquals(2, composedList.tokens.size)
        assertEquals(token1, composedList.tokens[0])
        assertEquals(token2, composedList.tokens[1])
        
        // Test compose with vararg
        val composedVararg = compose(
            id = "composed_vararg",
            token1,
            token2,
            version = 2
        )
        
        assertEquals("composed_vararg", composedVararg.id)
        assertEquals(2, composedVararg.version)
        assertEquals(2, composedVararg.tokens.size)
        assertEquals(token1, composedVararg.tokens[0])
        assertEquals(token2, composedVararg.tokens[1])
    }
    
    @Test
    fun testWithExtensionFunction() {
        // Create some test tokens
        val token1 = text {
            text("Token 1")
        }
        
        val token2 = text {
            text("Token 2")
        }
        
        val token3 = text {
            text("Token 3")
        }
        
        // Test infix with
        val composed1 = token1 with token2
        
        assertTrue(composed1 is CompositeToken)
        assertEquals(2, composed1.tokens.size)
        assertEquals(token1, composed1.tokens[0])
        assertEquals(token2, composed1.tokens[1])
        
        // Test with vararg
        val composed2 = token1.with(token2, token3)
        
        assertTrue(composed2 is CompositeToken)
        assertEquals(3, composed2.tokens.size)
        assertEquals(token1, composed2.tokens[0])
        assertEquals(token2, composed2.tokens[1])
        assertEquals(token3, composed2.tokens[2])
        
        // Test with list
        val composed3 = token1.with(listOf(token2, token3))
        
        assertTrue(composed3 is CompositeToken)
        assertEquals(3, composed3.tokens.size)
        assertEquals(token1, composed3.tokens[0])
        assertEquals(token2, composed3.tokens[1])
        assertEquals(token3, composed3.tokens[2])
    }
    
    @Test
    fun testNestedComposition() {
        // Create some test tokens
        val token1 = text {
            text("Token 1")
        }
        
        val token2 = text {
            text("Token 2")
        }
        
        val token3 = text {
            text("Token 3")
        }
        
        // Create a nested composition
        val inner = compose(
            id = "inner",
            token2,
            token3
        )
        
        val outer = compose(
            id = "outer",
            token1,
            inner
        )
        
        assertEquals("outer", outer.id)
        assertEquals(2, outer.tokens.size)
        assertEquals(token1, outer.tokens[0])
        assertEquals(inner, outer.tokens[1])
        
        // Test with extension functions
        val nested = token1.with(token2 with token3)
        
        assertTrue(nested is CompositeToken)
        assertEquals(2, nested.tokens.size)
        assertEquals(token1, nested.tokens[0])
        assertTrue(nested.tokens[1] is CompositeToken)
        
        val innerNested = nested.tokens[1] as CompositeToken
        assertEquals(2, innerNested.tokens.size)
        assertEquals(token2, innerNested.tokens[0])
        assertEquals(token3, innerNested.tokens[1])
    }
    
    @Test
    fun testTokenRegistryWithCompositeTokens() {
        // Create some test tokens
        val token1 = text {
            text("Token 1")
        }
        
        val token2 = text {
            text("Token 2")
        }
        
        // Create a composite token
        val composite = token1 with token2
        
        // Create a registry and register the composite token
        val registry = TokenRegistry()
        registry.register(composite)
        
        // Verify the composite token is in the registry
        assertTrue(registry.hasToken(composite.id))
        assertEquals(composite, registry.getToken(composite.id))
    }
}