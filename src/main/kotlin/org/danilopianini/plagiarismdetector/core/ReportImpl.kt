package org.danilopianini.plagiarismdetector.core

import org.apache.commons.math3.stat.descriptive.rank.Percentile
import org.danilopianini.plagiarismdetector.core.detector.ComparisonResult
import org.danilopianini.plagiarismdetector.core.detector.Match
import org.danilopianini.plagiarismdetector.repository.Repository

/**
 * A simple implementation of the [Report] interface.
 */
class ReportImpl<out M : Match>(
    override val submittedProject: Repository,
    override val comparedProject: Repository,
    override val comparisonResult: Set<ComparisonResult<M>>,
    private val reportedRatio: Double,
) : Report<M> {

    override val similarity: Double = ProjectsSimilarityEstimator {
        with(Percentile()) {
            data = it.map { it.similarity }.toDoubleArray()
            reportedRatio * evaluate(DEFAULT_PERCENTILE_VALUE)
        }
    }.invoke(comparisonResult)

    companion object {
        private const val DEFAULT_PERCENTILE_VALUE = 75.0
    }
}
