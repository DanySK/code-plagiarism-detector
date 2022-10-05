package org.danilopianini.plagiarismdetector.detector.technique.tokenization

import org.danilopianini.plagiarismdetector.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.detector.ComparisonResult
import org.danilopianini.plagiarismdetector.detector.ComparisonStrategy
import org.danilopianini.plagiarismdetector.detector.PlagiarismDetector

/**
 * A simple token based plagiarism detector.
 */
class TokenBasedPlagiarismDetector(
    private val strategy: ComparisonStrategy<TokenizedSource, Sequence<Token>, TokenMatch> = RKRGreedyStringTiling()
) : PlagiarismDetector<TokenizedSource, Sequence<Token>, TokenMatch> {

    override operator fun invoke(
        input: Pair<TokenizedSource, TokenizedSource>,
    ): ComparisonResult<TokenMatch> = with(strategy(input)) {
        val scoreOfSimilarity = estimateSimilarity(input, this)
        TokenBasedComparisonResult(scoreOfSimilarity, this)
    }

    private fun estimateSimilarity(
        input: Pair<TokenizedSource, TokenizedSource>,
        matches: Set<TokenMatch>,
    ): Double {
        val totalTokens = input.first.representation.count() + input.second.representation.count()
        val matchesCoverage = matches.sumOf { it.length }
        return ((2 * matchesCoverage).toDouble() / totalTokens)
    }
}
