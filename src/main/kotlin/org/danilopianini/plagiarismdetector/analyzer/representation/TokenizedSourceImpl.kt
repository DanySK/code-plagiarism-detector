package org.danilopianini.plagiarismdetector.analyzer.representation

import org.danilopianini.plagiarismdetector.analyzer.representation.token.Gram
import org.danilopianini.plagiarismdetector.analyzer.representation.token.GramImpl
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token
import java.io.File

/**
 * A class implementing a [TokenizedSource].
 */
data class TokenizedSourceImpl(
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

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        } else if (javaClass != other?.javaClass) {
            return false
        }
        other as TokenizedSourceImpl
        return sourceFile == other.sourceFile && representation.toList() == other.representation.toList()
    }

    override fun hashCode(): Int {
        var result = sourceFile.hashCode()
        result = 31 * result + representation.hashCode()
        return result
    }
}
