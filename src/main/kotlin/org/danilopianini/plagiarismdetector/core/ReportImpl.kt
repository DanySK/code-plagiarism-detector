package org.danilopianini.plagiarismdetector.core

import org.danilopianini.plagiarismdetector.core.detector.ComparisonResult
import org.danilopianini.plagiarismdetector.core.detector.Match
import org.danilopianini.plagiarismdetector.repository.Repository
import kotlin.random.Random

/**
 * A simple implementation of the [Report] interface.
 */
class ReportImpl<out M : Match>(
    override val submittedProject: Repository,
    override val comparedProject: Repository,
    override val comparisonResult: Set<ComparisonResult<M>>
) : Report<M> {

    override val similarity: Double
        get() = Random(SEED).nextDouble()

    companion object {
        private const val SEED = 0
    }
}
