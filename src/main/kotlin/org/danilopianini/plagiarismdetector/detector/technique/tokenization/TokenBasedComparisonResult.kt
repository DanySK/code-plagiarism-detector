package org.danilopianini.plagiarismdetector.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.detector.ComparisonResult

/**
 * An implementation of [ComparisonResult].
 */
class TokenBasedComparisonResult(
    override val similarity: Double,
    tokenMatches: Set<TokenMatch>,
) : ComparisonResult<TokenMatch> {

    override val matches: Sequence<TokenMatch> = tokenMatches.asSequence()
}
