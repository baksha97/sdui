package com.baksha97.sdui.shared.models.annotations

/**
 * Annotation to mark a component definition for automatic registration.
 * This annotation can be applied to functions that create Token instances.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class RegisterComponent(
    /**
     * The ID of the component. If not specified, the function name will be used.
     */
    val id: String = "",
    
    /**
     * The version of the component.
     */
    val version: Int = 1,
    
    /**
     * Dependencies that this component requires to be registered first.
     * These should be the IDs of other components.
     */
    val dependencies: Array<String> = []
)

/**
 * Annotation to mark a class that contains component definitions.
 * All functions annotated with @RegisterComponent in this class will be processed.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class ComponentRegistry(
    /**
     * The name of the generated registry class.
     */
    val name: String = "GeneratedComponentRegistry"
)

/**
 * Annotation to mark parameters that should be bound from context.
 * This helps the annotation processor understand which parameters need template binding.
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class BindFromContext(
    /**
     * The key to bind from context. If not specified, the parameter name will be used.
     */
    val key: String = ""
)

/**
 * Annotation to mark a component as a screen definition.
 * This will generate screen payload registration in addition to component registration.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class RegisterScreen(
    /**
     * The ID of the screen.
     */
    val id: String,
    
    /**
     * The components that make up this screen.
     */
    val components: Array<String> = []
)