package org.danilopianini.plagiarismdetector.core.analyzer.representation

import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.Gram
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.GramImpl
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.Token
import java.io.File

/**
 * A class implementing a [TokenizedSource].
 * @param sourceFile the file associated to this representation
 * @param tokens the list of tokens that represents the source file
 */
data class TokenizedSourceImpl(
    override val sourceFile: File,
    val tokens: List<Token>,
) : TokenizedSource {
    private companion object {
        private const val SLIDING_STEP = 1
    }

    override val representation: Sequence<Token> = tokens.asSequence()

    override fun splitInGramsOf(size: Int): Sequence<Gram<Token>> =
        representation
            .windowed(size, SLIDING_STEP)
            .map(::GramImpl)
}
