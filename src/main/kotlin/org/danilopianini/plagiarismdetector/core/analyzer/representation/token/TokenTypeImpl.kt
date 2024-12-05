package org.danilopianini.plagiarismdetector.core.analyzer.representation.token

import kotlinx.serialization.Serializable

/**
 * A simple class implementing a [TokenType].
 */
@Serializable
data class TokenTypeImpl(
    override val name: String,
    override val languageConstructs: Set<String>,
) : TokenType {
    override fun equals(other: Any?) =
        when {
            this === other -> true
            other !is TokenTypeImpl -> false
            else -> name == other.name
        }

    override fun hashCode() = name.hashCode()

    override fun toString(): String = "name: $name, constructs: {${languageConstructs.joinToString()}}"
}
