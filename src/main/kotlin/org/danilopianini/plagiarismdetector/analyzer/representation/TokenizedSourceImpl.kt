package org.danilopianini.plagiarismdetector.analyzer.representation

import org.danilopianini.plagiarismdetector.analyzer.representation.token.Gram
import org.danilopianini.plagiarismdetector.analyzer.representation.token.GramImpl
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token
import java.io.File

/**
 * Tokenized source impl.
 */
class TokenizedSourceImpl(
    override val sourceFile: File,
    override val representation: Sequence<Token>
) : TokenizedSource {
    companion object {
        private const val SLIDING_STEP = 1
    }

    override fun splitInGramsOf(size: Int): Sequence<Gram<Token>> {
        return representation.windowed(size, SLIDING_STEP)
            .map { GramImpl(it.asSequence()) }
    }
}
