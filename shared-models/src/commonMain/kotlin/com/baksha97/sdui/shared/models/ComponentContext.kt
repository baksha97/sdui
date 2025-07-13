package com.baksha97.sdui.shared.models

/**
 * Context for tracking component hierarchy and generating IDs
 */
class ComponentContext {
    private val idStack = mutableListOf<String>()
    private var counter = mutableMapOf<String, Int>()

    /**
     * Push an ID onto the stack
     */
    fun pushId(id: String) {
        idStack.add(id)
    }

    /**
     * Pop the top ID from the stack
     */
    fun popId() {
        if (idStack.isNotEmpty()) {
            idStack.removeAt(idStack.size - 1)
        }
    }

    /**
     * Get the current path as a dot-separated string
     */
    fun getCurrentPath(): String {
        return idStack.joinToString(".")
    }

    /**
     * Generate a unique ID for a component type
     */
    fun generateId(type: String): String {
        val prefix = if (idStack.isEmpty()) "" else "${getCurrentPath()}."
        val key = "$prefix$type"
        val count = counter[key] ?: 0
        val newCount = count + 1
        counter[key] = newCount
        return if (newCount == 1) "$prefix$type" else "$prefix${type}_$newCount"
    }

    /**
     * Reset the context
     */
    fun reset() {
        idStack.clear()
        counter.clear()
    }

}

/**
 * Scope interface for UI building with context parameters
 */
interface UIScope {
    val componentContext: ComponentContext
}

/**
 * Base implementation of UIScope
 */
class UIScopeImpl(
    override val componentContext: ComponentContext = ComponentContext()
) : UIScope

/**
 * Creates a UI scope and executes the given block within it
 */
fun <T : Token> ui(block: context(UIScope) () -> T): T =
    with(UIScopeImpl()) { block() }

/**
 * Creates a UI scope with a custom component context and executes the given block within it
 */
fun ui(componentContext: ComponentContext, block: (UIScope) -> Unit) {
    val scope = UIScopeImpl(componentContext)
    block(scope)
}
