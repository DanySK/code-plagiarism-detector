package org.danilopianini.plagiarismdetector.analyzer.representation.token

import kotlinx.serialization.Serializable

/**
 * A simple class implementing a [TokenType].
 */
@Serializable
data class TokenTypeImpl(
    override val name: String,
    override val languageConstructs: Set<String>,
) : TokenType {
    override fun toString(): String = "name: $name, constructs: {${languageConstructs.joinToString()}}"
}
