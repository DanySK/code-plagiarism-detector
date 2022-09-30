package org.danilopianini.plagiarismdetector.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.detector.Match

/**
 * A match between two files.
 */
interface TokenMatch : Match {
    /**
     * The matching [Token] in the pattern.
     */
    val pattern: Pair<TokenizedSource, List<Token>>

    /**
     * The matching [Token] in the text.
     */
    val text: Pair<TokenizedSource, List<Token>>

    /**
     * The length of the match in terms of number of tokens in common.
     */
    val length: Int
}
