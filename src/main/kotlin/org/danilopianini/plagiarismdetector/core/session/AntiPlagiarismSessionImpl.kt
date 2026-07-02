package org.danilopianini.plagiarismdetector.core.session

import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.stream.Collectors
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.fetchAndIncrement
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.danilopianini.plagiarismdetector.core.Report
import org.danilopianini.plagiarismdetector.core.ReportImpl
import org.danilopianini.plagiarismdetector.core.detector.Match
import org.danilopianini.plagiarismdetector.input.configuration.RunConfiguration
import org.danilopianini.plagiarismdetector.output.Output
import org.danilopianini.plagiarismdetector.repository.Repository

/**
 * A **parallel** implementation of an [AntiPlagiarismSession].
 */
class AntiPlagiarismSessionImpl<out C : RunConfiguration<M>, M : Match>(
    private val configuration: C,
    private val output: Output,
) : AntiPlagiarismSession {
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

    @OptIn(ExperimentalAtomicApi::class)
    private fun processNotYetProcessed(submission: Repository): Set<Report<M>> = with(configuration) {
        val corpusToProcess = corpus
            .filter { it.name != submission.name && submission hasNotYetComparedAgainst it }
            .toSet()
        output.startComparison(submission.name, corpusToProcess.size)
        val threadCounter = AtomicInt(0)
        val executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()) {
            Thread(it, "plagiarism-detector-thread-${threadCounter.fetchAndIncrement()}")
        }
        executor.asCoroutineDispatcher().use { dispatcher ->
            runBlocking {
                corpusToProcess.map { compared ->
                    async(dispatcher) {
                        output.startCorpusComparison(compared.name)
                        try {
                            technique.execute(submission, compared, filesToExclude, minDuplicationPercentage)
                        } finally {
                            output.endCorpusComparison(compared.name)
                        }
                    }
                }.awaitAll().toSet()
            }
        }
    }.also {
        output.endComparison()
    }

    private fun retrieveAlreadyProcessed(submission: Repository): Set<Report<M>> = with(configuration) {
        corpus
            .mapNotNull { corpus ->
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
