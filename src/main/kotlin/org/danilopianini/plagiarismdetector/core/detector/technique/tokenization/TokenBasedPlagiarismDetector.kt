package org.danilopianini.plagiarismdetector.core.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.core.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.core.detector.ComparisonResult
import org.danilopianini.plagiarismdetector.core.detector.ComparisonStrategy
import org.danilopianini.plagiarismdetector.core.detector.PlagiarismDetector

/**
 * A simple token based plagiarism detector.
 * @constructor Creates a detector using the detection [comparisonStrategy] given in input.
 */
class TokenBasedPlagiarismDetector(
    private val comparisonStrategy: ComparisonStrategy<TokenizedSource, Sequence<Token>, TokenMatch>,
    private val similarityEstimationStrategy: TokenBasedSimilarityStrategy = NormalizedMaxSimilarityStrategy(),
) : PlagiarismDetector<TokenizedSource, Sequence<Token>, TokenMatch> {

    /**
     * Creates a detector using, as detection strategy, [RKRGreedyStringTiling]
     * with minimum match length searched set to [minimumMatchLength].
     */
    constructor(minimumMatchLength: Int) : this(RKRGreedyStringTiling(minimumMatchLength))

    override operator fun invoke(
        input: Pair<TokenizedSource, TokenizedSource>,
    ): ComparisonResult<TokenMatch> = with(comparisonStrategy(input)) {
        val scoreOfSimilarity = similarityEstimationStrategy.similarityOf(input, this)
        TokenBasedComparisonResult(scoreOfSimilarity, this)
    }
}
