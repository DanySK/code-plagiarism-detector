package org.danilopianini.plagiarismdetector.output

import me.tongfei.progressbar.ProgressBar
import me.tongfei.progressbar.ProgressBarBuilder
import me.tongfei.progressbar.ProgressBarStyle

/**
 * A standard [Output] bound to the command line.
 */
class StandardOutput : Output {

    private lateinit var progressBar: ProgressBar

    override fun startComparison(submissionName: String, totalComparisons: Int) {
        progressBar = ProgressBarBuilder()
            .setTaskName(submissionName)
            .setStyle(ProgressBarStyle.COLORFUL_UNICODE_BLOCK)
            .setInitialMax(totalComparisons.toLong())
            .continuousUpdate()
            .hideEta()
            .clearDisplayOnFinish()
            .build()
    }

    override fun tick() {
        progressBar.step()
    }

    override fun endComparison() = progressBar.close()

    override fun logInfo(message: String) = println(message)
}
