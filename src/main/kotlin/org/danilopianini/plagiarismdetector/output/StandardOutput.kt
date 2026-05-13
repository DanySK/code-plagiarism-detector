package org.danilopianini.plagiarismdetector.output

import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.time.ExperimentalTime
import kotlin.time.TimeMark
import kotlin.time.TimeSource
import me.tongfei.progressbar.ProgressBar
import me.tongfei.progressbar.ProgressBarBuilder
import me.tongfei.progressbar.ProgressBarStyle

/**
 * A standard [Output] bound to the command line.
 */
@OptIn(ExperimentalTime::class)
class StandardOutput : Output {
    private val lock = Any()
    private val supportsRewritingCurrentLine = terminalSupportsRewritingCurrentLine()
    private var progressBar: ProgressBar? = null
    private var heartbeat: ScheduledExecutorService? = null
    private var start: TimeMark = TimeSource.Monotonic.markNow()
    private var currentSubmission = ""
    private var totalComparisons = 0
    private var startedComparisons = 0
    private var completedComparisons = 0
    private val activeCorpus = linkedSetOf<String>()

    override fun startComparison(submissionName: String, totalComparisons: Int) {
        synchronized(lock) {
            currentSubmission = submissionName
            this.totalComparisons = totalComparisons
            startedComparisons = 0
            completedComparisons = 0
            activeCorpus.clear()
        }
        if (supportsRewritingCurrentLine) {
            progressBar = ProgressBarBuilder()
                .setTaskName(submissionName)
                .setStyle(ProgressBarStyle.COLORFUL_UNICODE_BLOCK)
                .setInitialMax(totalComparisons.toLong())
                .continuousUpdate()
                .hideEta()
                .clearDisplayOnFinish()
                .build()
        } else {
            logInfo("Analyzing submission '$submissionName' against $totalComparisons corpus projects.")
            heartbeat = Executors.newSingleThreadScheduledExecutor {
                Thread(it, "comparison-progress-heartbeat").apply { isDaemon = true }
            }.also {
                it.scheduleAtFixedRate(::printHeartbeat, 1, 1, TimeUnit.MINUTES)
            }
        }
        start = TimeSource.Monotonic.markNow()
    }

    override fun startCorpusComparison(corpusName: String) {
        val progressMessage = synchronized(lock) {
            startedComparisons++
            activeCorpus.add(corpusName)
            "submission: $currentSubmission | corpus: $corpusName | " +
                "$startedComparisons/$totalComparisons started, $completedComparisons completed"
        }
        if (supportsRewritingCurrentLine) {
            progressBar?.setExtraMessage(" | $progressMessage")
            progressBar?.step()
            progressBar?.refresh()
        } else {
            logInfo("Analyzing $progressMessage.")
        }
    }

    override fun endCorpusComparison(corpusName: String) {
        val progressMessage = synchronized(lock) {
            completedComparisons++
            activeCorpus.remove(corpusName)
            "$startedComparisons/$totalComparisons started, $completedComparisons completed"
        }
        if (supportsRewritingCurrentLine) {
            progressBar?.setExtraMessage(" | $progressMessage")
            progressBar?.refresh()
        }
    }

    override fun endComparison() {
        heartbeat?.shutdownNow()
        heartbeat = null
        progressBar?.close()
        progressBar = null
        logInfo("Total elapsed time: ${start.elapsedNow().inWholeSeconds}s")
    }

    override fun logInfo(message: String) = println(message)

    private fun printHeartbeat() {
        val message = synchronized(lock) {
            val active = activeCorpus.take(MAX_ACTIVE_CORPUS_IN_HEARTBEAT)
            val remainingActive = activeCorpus.count() - active.count()
            val activeDescription = when {
                activeCorpus.isEmpty() -> "none"
                remainingActive > 0 -> active.joinToString(", ") + " and $remainingActive more"
                else -> active.joinToString(", ")
            }
            "Still analyzing submission '$currentSubmission': " +
                "$completedComparisons/$totalComparisons completed; active corpus: $activeDescription."
        }
        logInfo(message)
    }

    /**
     * Terminal output constants and capability checks.
     */
    companion object {
        private const val MAX_ACTIVE_CORPUS_IN_HEARTBEAT = 5

        private fun terminalSupportsRewritingCurrentLine(): Boolean {
            val term = System.getenv("TERM")?.lowercase()
            return System.console() != null && term != null && term != "dumb" && System.getenv("CI").isNullOrBlank()
        }
    }
}
