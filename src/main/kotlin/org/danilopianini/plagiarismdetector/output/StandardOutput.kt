package org.danilopianini.plagiarismdetector.output

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
    private lateinit var progressBar: ProgressBar
    private var start: TimeMark = TimeSource.Monotonic.markNow()

    override fun startComparison(submissionName: String, totalComparisons: Int) {
        progressBar = ProgressBarBuilder()
            .setTaskName(submissionName)
            .setStyle(ProgressBarStyle.COLORFUL_UNICODE_BLOCK)
            .setInitialMax(totalComparisons.toLong())
            .continuousUpdate()
            .hideEta()
            .clearDisplayOnFinish()
            .build()
        start = TimeSource.Monotonic.markNow()
    }

    override fun tick() {
        progressBar.step()
    }

    override fun endComparison() {
        progressBar.close()
        logInfo("Total elapsed time: ${start.elapsedNow().inWholeSeconds}s")
    }

    override fun logInfo(message: String) = println(message)
}
