package org.danilopianini.plagiarismdetector.core

import org.apache.commons.math3.stat.descriptive.rank.Percentile
import org.danilopianini.plagiarismdetector.commons.Java
import org.danilopianini.plagiarismdetector.core.detector.ComparisonResult
import org.danilopianini.plagiarismdetector.core.detector.Match
import org.danilopianini.plagiarismdetector.repository.Repository
import kotlin.math.min

/**
 * A simple implementation of the [Report] interface.
 */
class ReportImpl<out M : Match>(
    override val submittedProject: Repository,
    override val comparedProject: Repository,
    override val comparisonResult: Set<ComparisonResult<M>>
) : Report<M> {

    override val similarity: Double = ProjectsSimilarityEstimator {
        with(Percentile()) {
            data = it.map { it.similarity }.toDoubleArray()
            val reported = min(
                it.count().toDouble() / comparedProject.getSources(Java.fileExtensions).count(),
                1.0
            )
            reported * evaluate(75.0)
        }
    }.invoke(comparisonResult)
}
