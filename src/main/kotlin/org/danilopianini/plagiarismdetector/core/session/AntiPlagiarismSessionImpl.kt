package org.danilopianini.plagiarismdetector.core.session

import org.danilopianini.plagiarismdetector.core.Report
import org.danilopianini.plagiarismdetector.core.ReportImpl
import org.danilopianini.plagiarismdetector.core.detector.Match
import org.danilopianini.plagiarismdetector.input.configuration.RunConfiguration
import org.danilopianini.plagiarismdetector.repository.Repository
import org.slf4j.LoggerFactory
import java.util.stream.Collectors
import kotlin.system.measureTimeMillis

/**
 * A **parallel** implementation of an [AntiPlagiarismSession].
 */
class AntiPlagiarismSessionImpl<out C : RunConfiguration<M>, M : Match>(
    private val configuration: C
) : AntiPlagiarismSession {

    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val processedResult = mutableSetOf<Report<M>>()

    override operator fun invoke() = logExecutionTime {
        with(configuration) {
            submission.forEach { submission ->
                val result = corpus
                    .filter { it.name != submission.name && hasNotYetProcessed(submission, it) }
                    .toSet()
                    .parallelStream()
                    .map { technique.execute(submission, it, filesToExclude, minDuplicatedPercentage) }
                    .collect(Collectors.toSet())
                corpus.filter { hasAlreadyBeenProcessed(submission, it) }
                    .forEach {
                        alreadyProcessed(submission, it)?.run {
                            result.add(ReportImpl(submission, it, comparisonResult, reportedRatio))
                        }
                    }
                processedResult.addAll(result)
                if (result.isNotEmpty()) {
                    configuration.exporter(result)
                }
            }
        }
    }

    private fun hasAlreadyBeenProcessed(repo1: Repository, repo2: Repository): Boolean =
        !hasNotYetProcessed(repo1, repo2)

    private fun hasNotYetProcessed(repo1: Repository, repo2: Repository): Boolean =
        processedResult.none {
            (it.comparedProject.name == repo1.name && it.submittedProject.name == repo2.name) ||
                (it.comparedProject.name == repo2.name && it.submittedProject.name == repo1.name)
        }

    private fun alreadyProcessed(repo1: Repository, repo2: Repository): Report<M>? =
        processedResult.find {
            (it.comparedProject.name == repo1.name && it.submittedProject.name == repo2.name) ||
                (it.comparedProject.name == repo2.name && it.submittedProject.name == repo1.name)
        }

    /**
     * Logs the elapsed time used to execute the given [block].
     */
    private fun logExecutionTime(block: () -> Unit) {
        val elapsedTime = measureTimeMillis(block)
        logger.info("> Elapsed time: ${elapsedTime.toDouble() / MILLISECONDS_IN_ONE_MINUTE} min.")
    }

    companion object {
        private const val MILLISECONDS_IN_ONE_MINUTE = 60_000
    }
}
