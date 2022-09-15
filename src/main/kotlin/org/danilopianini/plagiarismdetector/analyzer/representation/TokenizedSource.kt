package org.danilopianini.plagiarismdetector.analyzer.representation

import org.danilopianini.plagiarismdetector.analyzer.representation.token.Gram
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token

/**
 * A token based representation of a source file, which is a sequence
 * of structure-preserving terms found in the code files.
 */
interface TokenizedSource : SourceRepresentation<Sequence<Token>> {
    /**
     * Splits the sequence of tokens owned by this file representation
     * in [grams](https://en.wikipedia.org/wiki/N-gram) of the given dimension.
     * @param dimension an [Int] describing the dimension of each gram.
     * @return a sequence of [Gram].
     */
    fun splitInGramsOf(dimension: Int): Sequence<Gram<Token>>
}
