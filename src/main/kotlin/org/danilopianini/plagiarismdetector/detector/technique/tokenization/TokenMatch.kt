package org.danilopianini.plagiarismdetector.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.detector.Match

/**
 * A match between two [TokenizedSource].
 */
interface TokenMatch : Match {
    /**
     * The matching [Token]s in the pattern, i.e. the shorter [TokenizedSource].
     */
    val pattern: Pair<TokenizedSource, List<Token>>

    /**
     * The matching [Token]s in the text, i.e. the longer [TokenizedSource].
     */
    val text: Pair<TokenizedSource, List<Token>>

    /**
     * The length of the match in terms of number of tokens in common.
     */
    val length: Int
}
