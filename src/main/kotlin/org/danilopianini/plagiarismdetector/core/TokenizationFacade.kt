package org.danilopianini.plagiarismdetector.core

import org.danilopianini.plagiarismdetector.commons.Java
import org.danilopianini.plagiarismdetector.core.analyzer.representation.SourceRepresentation
import org.danilopianini.plagiarismdetector.core.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.core.analyzer.technique.tokenization.java.JavaTokenizationAnalyzer
import org.danilopianini.plagiarismdetector.core.detector.ComparisonResult
import org.danilopianini.plagiarismdetector.core.detector.technique.tokenization.TokenBasedPlagiarismDetector
import org.danilopianini.plagiarismdetector.core.detector.technique.tokenization.TokenMatch
import org.danilopianini.plagiarismdetector.input.cli.technique.TokenizationConfig
import org.danilopianini.plagiarismdetector.repository.Repository

/**
 * A concrete [TechniqueFacade] which exploits the **Tokenization** technique.
 */
class TokenizationFacade(private val configs: TokenizationConfig) : TechniqueFacade<TokenMatch> {
    private val analyzer = when (configs.language) {
        Java -> JavaTokenizationAnalyzer()
    }
    private val detector = TokenBasedPlagiarismDetector(configs.minimumTokens)

    override fun execute(
        submittedRepository: Repository,
        comparedRepository: Repository,
        filesToExclude: Set<String>,
        minDuplicatedPercentage: Double
    ): Result<TokenMatch> {
        val submittedAnalyzed = analyze(submittedRepository, filesToExclude)
        val corpusAnalyzed = analyze(comparedRepository, filesToExclude)
        return ResultImpl(
            submittedRepository,
            comparedRepository,
            compare(submittedAnalyzed, corpusAnalyzed, minDuplicatedPercentage)
        )
    }

    /**
     * Performs the analysis of the [repository] sources returning a
     * [Sequence] of concrete [SourceRepresentation].
     */
    private fun analyze(repository: Repository, filesToExclude: Set<String>) = repository
        .getSources(configs.language.fileExtensions)
        .filter { it.name !in filesToExclude }
        .map(analyzer)

    /**
     * Performs the comparison between the [analyzedSubmission] and
     * [analyzedCorpus] returning a set of [ComparisonResult].
     */
    private fun compare(
        analyzedSubmission: Sequence<TokenizedSource>,
        analyzedCorpus: Sequence<TokenizedSource>,
        minDuplicatedPercentage: Double
    ): Set<ComparisonResult<TokenMatch>> = analyzedSubmission
        .flatMap { s -> analyzedCorpus.map { c -> detector(Pair(s, c)) } }
        .filter { it.similarity > minDuplicatedPercentage }
        .toSet()
}
