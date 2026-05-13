package org.danilopianini.plagiarismdetector.output

/**
 * An interface modeling a resource on which is possible
 * to present output results.
 */
interface Output {
    /**
     * Notifies the UI that the comparison of [submissionName] has started
     * and will last the given number of [totalComparisons].
     */
    fun startComparison(submissionName: String, totalComparisons: Int)

    /**
     * Notifies the UI that [corpusName] is being compared against the
     * current submission.
     */
    fun startCorpusComparison(corpusName: String)

    /**
     * Notifies the UI that [corpusName] has been compared against the
     * current submission.
     */
    fun endCorpusComparison(corpusName: String)

    /**
     * Notifies the UI the comparison process is ended.
     */
    fun endComparison()

    /**
     * Logs a [message] on the UI.
     */
    fun logInfo(message: String)
}
