package org.danilopianini.plagiarismdetector.analyzer.representation.token

import kotlinx.serialization.Serializable

/**
 * A simple implementation of a token type.
 */
@Serializable
data class TokenTypeImpl(
    override val identifier: String,
    override val types: Collection<String>
) : TokenType {
    override fun toString(): String {
        return "$identifier: ${types.joinToString()}"
    }
}
