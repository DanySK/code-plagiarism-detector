package org.danilopianini.plagiarismdetector.core.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.core.detector.ComparisonResult

/**
 * An implementation of [ComparisonResult].
 */
class TokenBasedComparisonResult(
    override val similarity: Double,
    tokenMatches: Set<TokenMatch>,
) : ComparisonResult<TokenMatch> {

    override val matches: Sequence<TokenMatch> = tokenMatches.asSequence()
}
