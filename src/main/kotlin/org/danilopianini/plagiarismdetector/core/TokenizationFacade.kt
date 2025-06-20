package org.danilopianini.plagiarismdetector.core

import kotlin.math.max
import org.danilopianini.plagiarismdetector.core.analyzer.representation.SourceRepresentation
import org.danilopianini.plagiarismdetector.core.analyzer.representation.TokenizedSource
import org.danilopianini.plagiarismdetector.core.analyzer.technique.tokenization.java.JavaTokenizationAnalyzer
import org.danilopianini.plagiarismdetector.core.detector.ComparisonResult
import org.danilopianini.plagiarismdetector.core.detector.technique.tokenization.TokenBasedPlagiarismDetector
import org.danilopianini.plagiarismdetector.core.detector.technique.tokenization.TokenMatch
import org.danilopianini.plagiarismdetector.core.filter.RepresentationFilter
import org.danilopianini.plagiarismdetector.core.filter.technique.tokenization.TokenizedSourceFilter
import org.danilopianini.plagiarismdetector.input.configuration.TokenizationConfiguration
import org.danilopianini.plagiarismdetector.repository.Repository
import org.danilopianini.plagiarismdetector.utils.Java
import org.slf4j.LoggerFactory

/**
 * A concrete [TechniqueFacade] which exploits the **Tokenization** technique.
 */
class TokenizationFacade(private val configs: TokenizationConfiguration) : TechniqueFacade<TokenMatch> {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val analyzer =
        when (configs.language) {
            Java -> JavaTokenizationAnalyzer()
        }
    private val filter =
        configs.filterThreshold?.let {
            TokenizedSourceFilter(it)
        } ?: RepresentationFilter { _, s -> s }
    private val detector = TokenBasedPlagiarismDetector(configs.minimumTokens)

    override fun execute(
        submittedRepository: Repository,
        comparedRepository: Repository,
        filesToExclude: Set<String>,
        minDuplicatedPercentage: Double,
    ): Report<TokenMatch> {
        logger.debug("Comparing ${submittedRepository.name} with ${comparedRepository.name}")
        val submittedAnalyzed = analyze(submittedRepository, filesToExclude)
        val corpusAnalyzed = analyze(comparedRepository, filesToExclude)
        val results = compare(submittedAnalyzed, corpusAnalyzed, minDuplicatedPercentage)
        val reportedRatio = reportedRatio(results, submittedAnalyzed, corpusAnalyzed)
        return ReportImpl(submittedRepository, comparedRepository, results, reportedRatio)
    }

    /**
     * Performs the analysis of the [repository] sources returning a
     * [Sequence] of concrete [SourceRepresentation].
     */
    private fun analyze(repository: Repository, filesToExclude: Set<String>): Sequence<TokenizedSource> = repository
        .getSources(configs.language.fileExtensions)
        .filter { it.name !in filesToExclude }
        .map(analyzer)
        .filter { it.representation.count() >= configs.minimumTokens }

    /**
     * Performs the comparison between the [analyzedSubmission] and
     * [analyzedCorpus] returning a set of [ComparisonResult].
     */
    private fun compare(
        analyzedSubmission: Sequence<TokenizedSource>,
        analyzedCorpus: Sequence<TokenizedSource>,
        minDuplicatedPercentage: Double,
    ): Set<ComparisonResult<TokenMatch>> = analyzedSubmission
        .flatMap { s -> filter(s, analyzedCorpus).map { c -> detector(Pair(s, c)) } }
        .filter { it.similarity > minDuplicatedPercentage }
        .toSet()

    /**
     * Returns the reported ratio, i.e. the maximum ratio between the number of
     * reported sources on the total of submission set and the number of reported
     * sources on the total of corpus set.
     */
    private fun reportedRatio(
        results: Set<ComparisonResult<TokenMatch>>,
        submittedAnalyzed: Sequence<TokenizedSource>,
        corpusAnalyzed: Sequence<TokenizedSource>,
    ): Double {
        val reportedCorpus = countReportedSourcesOf(results, corpusAnalyzed)
        val reportedSubmission = countReportedSourcesOf(results, submittedAnalyzed)
        return max(
            reportedSubmission.toDouble() / submittedAnalyzed.count().toDouble(),
            reportedCorpus.toDouble() / corpusAnalyzed.count().toDouble(),
        )
    }

    /**
     * Count the number of reported sources in the given [results] of [representations].
     */
    private fun countReportedSourcesOf(
        results: Set<ComparisonResult<TokenMatch>>,
        representations: Sequence<TokenizedSource>,
    ): Int = results
        .flatMap { it.matches }
        .flatMap { sequenceOf(it.pattern.first.sourceFile, it.text.first.sourceFile) }
        .distinctBy { it.path }
        .count { it in representations.map { t -> t.sourceFile } }
}
