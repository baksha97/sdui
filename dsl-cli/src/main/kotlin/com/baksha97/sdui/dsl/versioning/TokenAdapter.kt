package com.baksha97.sdui.dsl.versioning

import com.baksha97.sdui.dsl.*

/**
 * Adapter class that bridges between the existing Token classes and the new semantic versioning system.
 * 
 * This adapter allows existing Token classes to be used with the new SemanticVersionManager
 * without requiring changes to the existing codebase.
 */
object TokenAdapter {
    
    /**
     * Converts a Token to a VersionedToken.
     * 
     * @param token The token to convert
     * @return The converted token as a VersionedToken
     */
    fun toVersionedToken(token: Token): VersionedToken {
        return when (token) {
            is ContainerToken -> toVersionedContainerToken(token)
            else -> toVersionedSimpleToken(token)
        }
    }
    
    /**
     * Converts a ContainerToken to a VersionedContainerToken.
     * 
     * @param token The container token to convert
     * @return The converted token as a VersionedContainerToken
     */
    private fun toVersionedContainerToken(token: ContainerToken): VersionedContainerToken {
        return VersionedContainerTokenImpl(
            originalToken = token,
            version = SemanticVersion(token.version, 0, 0),
            children = token.children.map { toVersionedToken(it) }
        )
    }
    
    /**
     * Converts a simple Token to a VersionedToken.
     * 
     * @param token The simple token to convert
     * @return The converted token as a VersionedToken
     */
    private fun toVersionedSimpleToken(token: Token): VersionedToken {
        return VersionedTokenImpl(
            originalToken = token,
            version = SemanticVersion(token.version, 0, 0)
        )
    }
    
    /**
     * Converts a VersionedToken back to a Token.
     * 
     * @param token The versioned token to convert
     * @return The converted token as a Token
     */
    fun fromVersionedToken(token: VersionedToken): Token {
        return when (token) {
            is VersionedContainerTokenImpl -> token.originalToken.copy(version = token.version.major)
            is VersionedTokenImpl -> token.originalToken.copy(version = token.version.major)
            else -> throw IllegalArgumentException("Unsupported token type: ${token.javaClass.name}")
        }
    }
    
    /**
     * Implementation of VersionedToken that wraps an existing Token.
     */
    private data class VersionedTokenImpl(
        val originalToken: Token,
        override val version: SemanticVersion
    ) : VersionedToken {
        override fun withVersion(newVersion: SemanticVersion): VersionedToken {
            return copy(version = newVersion)
        }
    }
    
    /**
     * Implementation of VersionedContainerToken that wraps an existing ContainerToken.
     */
    private data class VersionedContainerTokenImpl(
        val originalToken: ContainerToken,
        override val version: SemanticVersion,
        override val children: List<VersionedToken>
    ) : VersionedContainerToken {
        override fun withVersion(newVersion: SemanticVersion): VersionedToken {
            return copy(version = newVersion)
        }
        
        override fun withChildrenAndVersion(
            newChildren: List<VersionedToken>,
            newVersion: SemanticVersion
        ): VersionedContainerToken {
            return copy(children = newChildren, version = newVersion)
        }
    }
    
    /**
     * Extension function to copy a Token with a new version.
     */
    private fun Token.copy(version: Int): Token {
        return when (this) {
            is ColumnToken -> copy(version = version)
            is RowToken -> copy(version = version)
            is BoxToken -> copy(version = version)
            is CardToken -> copy(version = version)
            is TextToken -> copy(version = version)
            is ButtonToken -> copy(version = version)
            is SpacerToken -> copy(version = version)
            is DividerToken -> copy(version = version)
            is SliderToken -> copy(version = version)
            is AsyncImageToken -> copy(version = version)
            is LazyColumnToken -> copy(version = version)
            is LazyRowToken -> copy(version = version)
            else -> throw IllegalArgumentException("Unsupported token type: ${this.javaClass.name}")
        }
    }
}