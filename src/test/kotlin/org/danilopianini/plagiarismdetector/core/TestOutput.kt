package org.danilopianini.plagiarismdetector.core

import org.danilopianini.plagiarismdetector.output.Output

/**
 * A test output implementation for the [Output] interface.
 */
object TestOutput : Output {
    override fun startComparison(submissionName: String, totalComparisons: Int) =
        println("Start comparison $submissionName ($totalComparisons)")

    override fun startCorpusComparison(corpusName: String) = Unit

    override fun endCorpusComparison(corpusName: String) = Unit

    override fun endComparison() = println("Ended")

    override fun logInfo(message: String) = println(message)
}
