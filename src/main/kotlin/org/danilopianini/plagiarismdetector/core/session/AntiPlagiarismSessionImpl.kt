package org.danilopianini.plagiarismdetector.core.session

import org.danilopianini.plagiarismdetector.core.Report
import org.danilopianini.plagiarismdetector.core.ReportImpl
import org.danilopianini.plagiarismdetector.core.detector.Match
import org.danilopianini.plagiarismdetector.input.configuration.RunConfiguration
import org.danilopianini.plagiarismdetector.output.StandardOutput
import org.danilopianini.plagiarismdetector.repository.Repository
import java.util.stream.Collectors

/**
 * A **parallel** implementation of an [AntiPlagiarismSession].
 */
class AntiPlagiarismSessionImpl<out C : RunConfiguration<M>, M : Match>(
    private val configuration: C
) : AntiPlagiarismSession {

    private val output = StandardOutput()
    private val processedResult = mutableSetOf<Report<M>>()

    override operator fun invoke() = with(configuration) {
        submission.forEach { submission ->
            val result = processNotYetProcessed(submission).toMutableSet()
            result.addAll(retrieveAlreadyProcessed(submission))
            processedResult.addAll(result)
            if (result.isNotEmpty()) {
                exporter(result)
            }
        }
    }

    private fun processNotYetProcessed(submission: Repository): Set<Report<M>> = with(configuration) {
        output.startComparison(submission.name, configuration.corpus.count())
        corpus.filter { it.name != submission.name && submission hasNotYetComparedAgainst it }
            .toSet()
            .parallelStream()
            .peek { output.tick() }
            .map { technique.execute(submission, it, filesToExclude, minDuplicationPercentage) }
            .collect(Collectors.toSet())
    }.also { output.endComparison() }

    private fun retrieveAlreadyProcessed(submission: Repository): Set<Report<M>> = with(configuration) {
        corpus.mapNotNull { corpus ->
            processedResult.find { it.refersTo(submission, corpus) }?.run {
                ReportImpl(submission, corpus, comparisonResult, reportedRatio)
            }
        }.toSet()
    }

    private infix fun Repository.hasNotYetComparedAgainst(other: Repository): Boolean =
        processedResult.none { it.refersTo(this, other) }

    private fun Report<M>.refersTo(repo1: Repository, repo2: Repository): Boolean =
        (comparedProject.name == repo1.name && submittedProject.name == repo2.name) ||
            (comparedProject.name == repo2.name && submittedProject.name == repo1.name)
}
