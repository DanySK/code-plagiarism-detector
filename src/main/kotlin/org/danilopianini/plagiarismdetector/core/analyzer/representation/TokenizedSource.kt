package org.danilopianini.plagiarismdetector.core.analyzer.representation

import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.Gram
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.Token

/**
 * A token based representation of a source file, which is a sequence
 * of structure-preserving lexical [Token] found in the code files.
 */
interface TokenizedSource : SourceRepresentation<Sequence<Token>> {
    /**
     * Splits the sequence of tokens of this file representation
     * in [grams](https://en.wikipedia.org/wiki/N-gram) of the given dimension.
     * @param size an [Int] describing the size of each gram.
     * @return a list of [Gram].
     */
    fun splitInGramsOf(size: Int): Sequence<Gram<Token>>
}
