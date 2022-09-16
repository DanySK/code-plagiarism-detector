package org.danilopianini.plagiarismdetector.analyzer.representation.token

import kotlinx.serialization.Serializable

/**
 * A simple class implementing a [TokenType].
 */
@Serializable
data class TokenTypeImpl(
    override val name: String,
    override val languageConstructs: Collection<String>
) : TokenType {
    override fun toString(): String {
        return "name: $name, constructs: {${languageConstructs.joinToString()}}"
    }
}
