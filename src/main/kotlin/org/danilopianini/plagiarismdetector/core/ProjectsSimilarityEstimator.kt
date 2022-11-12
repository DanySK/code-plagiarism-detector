package org.danilopianini.plagiarismdetector.core

import org.apache.commons.math3.stat.descriptive.rank.Percentile
import org.danilopianini.plagiarismdetector.core.detector.ComparisonResult
import kotlin.math.min
import kotlin.math.sin

/**
 * A strategy to estimate the similarity.
 */
fun interface ProjectsSimilarityEstimator : (Double, Set<ComparisonResult<*>>) -> Double

/**
 * An estimator strategy which computes similarity using a fixed weight coefficient.
 */
class SimilarityEstimatorWithConstantWeight : ProjectsSimilarityEstimator {
    override fun invoke(reportedRatio: Double, results: Set<ComparisonResult<*>>): Double =
        if (results.isEmpty()) {
            0.0
        } else {
            with(Percentile()) {
                data = results.map { it.similarity }.toDoubleArray()
                min(WEIGHT_COEFFICIENT * reportedRatio, 1.0) * evaluate(DEFAULT_PERCENTILE_VALUE)
            }
        }

    companion object {
        private const val WEIGHT_COEFFICIENT = 1.5
        private const val DEFAULT_PERCENTILE_VALUE = 75.0
    }
}

/**
 * An estimator strategy which computes similarity using a function-based value.
 */
class SimilarityEstimatorWithLinearWeight : ProjectsSimilarityEstimator {
    override fun invoke(reportedRatio: Double, results: Set<ComparisonResult<*>>): Double =
        if (results.isEmpty()) {
            0.0
        } else {
            with(Percentile()) {
                data = results.map { it.similarity }.toDoubleArray()
                estimateWeightCoefficient(reportedRatio) * evaluate(DEFAULT_PERCENTILE_VALUE)
            }
        }

    private fun estimateWeightCoefficient(x: Double) =
        (sin((PERIOD * x) - (Math.PI / 2)) * AMPLITUDE + LOW_LEVEL).let {
            if (x >= THR_MAX_WEIGHT) MAX_WEIGHT_COEFFICIENT else it
        }

    companion object {
        private const val PERIOD = 4.5
        private const val LOW_LEVEL = 0.5
        private const val AMPLITUDE = 0.5
        private const val THR_MAX_WEIGHT = 0.7
        private const val MAX_WEIGHT_COEFFICIENT = 1.0
        private const val DEFAULT_PERCENTILE_VALUE = 80.0
    }
}
