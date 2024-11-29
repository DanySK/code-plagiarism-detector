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
    fun startComparison(
        submissionName: String,
        totalComparisons: Int,
    )

    /**
     * Notifies the UI a new comparison has been completed since the
     * comparison process started.
     */
    fun tick()

    /**
     * Notifies the UI the comparison process is ended.
     */
    fun endComparison()

    /**
     * Logs a [message] on the UI.
     */
    fun logInfo(message: String)
}
