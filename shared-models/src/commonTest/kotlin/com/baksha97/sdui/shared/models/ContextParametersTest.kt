package com.baksha97.sdui.shared.models

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ContextParametersTest {

    @Test
    fun testContextParametersWithUIScope() {
        // Test that context parameters work with the new DSL functions
        val column = ui {
            Column {
                text {
                    text("Hello World")
                }

                row {
                    button {
                        text("Click me")
                    }

                    spacer {
                        width = 10
                    }
                }

                box {
                    card {
                        text {
                            text("Card content")
                        }
                    }
                }
            }
        }

        assertNotNull(column)
        assertTrue(column.children.isNotEmpty())
        println("[DEBUG_LOG] Context parameters test passed - Column created with ${column.children.size} children")
    }

    @Test
    fun testNestedContextParameters() {
        // Test that nested components properly use context parameters
        val lazyColumn = ui {
            LazyColumn {
                column {
                    text {
                        text("Item 1")
                    }
                }

                column {
                    text {
                        text("Item 2")
                    }
                }
            }
        }

        assertNotNull(lazyColumn)
        assertTrue(lazyColumn.children.isNotEmpty())
        println("[DEBUG_LOG] Nested context parameters test passed - LazyColumn created with ${lazyColumn.children.size} children")
    }

}
