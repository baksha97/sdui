package com.baksha97.sdui

import kotlinx.serialization.Serializable

/**
 * A token that represents a composition of multiple tokens.
 * This allows for programmatic composition of tokens without requiring a parent-child relationship.
 */
@Serializable
data class CompositeToken(
    override val id: String,
    override val version: Int,
    override val a11y: A11y? = null,
    val tokens: List<Token>
) : Token {
    override val minSupportedVersion: Int = 1
}

/**
 * Composes multiple tokens into a single CompositeToken.
 * This allows for programmatic composition of tokens without requiring a parent-child relationship.
 *
 * @param id Optional ID for the composite token. If not provided, an ID will be auto-generated.
 * @param tokens The tokens to compose together.
 * @param version Version of the composite token.
 * @param a11y Optional accessibility properties for the composite token.
 * @return A CompositeToken containing all the provided tokens.
 */
fun compose(
    id: String? = null,
    tokens: List<Token>,
    version: Int = 1,
    a11y: A11y? = null
): CompositeToken {
    val actualId = id ?: ComponentContext.generateId("composite")
    return CompositeToken(
        id = actualId,
        version = version,
        a11y = a11y,
        tokens = tokens
    )
}

/**
 * Composes multiple tokens into a single CompositeToken.
 * This allows for programmatic composition of tokens without requiring a parent-child relationship.
 *
 * @param id Optional ID for the composite token. If not provided, an ID will be auto-generated.
 * @param tokens The tokens to compose together.
 * @param version Version of the composite token.
 * @param a11y Optional accessibility properties for the composite token.
 * @return A CompositeToken containing all the provided tokens.
 */
fun compose(
    id: String? = null,
    vararg tokens: Token,
    version: Int = 1,
    a11y: A11y? = null
): CompositeToken = compose(id, tokens.toList(), version, a11y)

/**
 * Extension function to compose this token with another token.
 *
 * @param other The token to compose with this token.
 * @param id Optional ID for the composite token. If not provided, an ID will be auto-generated.
 * @param version Version of the composite token.
 * @param a11y Optional accessibility properties for the composite token.
 * @return A CompositeToken containing this token and the other token.
 */
infix fun Token.with(other: Token): CompositeToken = compose(tokens = listOf(this, other))

/**
 * Extension function to compose this token with multiple other tokens.
 *
 * @param others The tokens to compose with this token.
 * @param id Optional ID for the composite token. If not provided, an ID will be auto-generated.
 * @param version Version of the composite token.
 * @param a11y Optional accessibility properties for the composite token.
 * @return A CompositeToken containing this token and the other tokens.
 */
fun Token.with(vararg others: Token): CompositeToken = compose(tokens = listOf(this) + others)

/**
 * Extension function to compose this token with a list of other tokens.
 *
 * @param others The tokens to compose with this token.
 * @param id Optional ID for the composite token. If not provided, an ID will be auto-generated.
 * @param version Version of the composite token.
 * @param a11y Optional accessibility properties for the composite token.
 * @return A CompositeToken containing this token and the other tokens.
 */
fun Token.with(others: List<Token>): CompositeToken = compose(tokens = listOf(this) + others)