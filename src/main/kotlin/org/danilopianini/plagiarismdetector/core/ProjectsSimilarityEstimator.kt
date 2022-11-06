package org.danilopianini.plagiarismdetector.core

import org.danilopianini.plagiarismdetector.core.detector.ComparisonResult

/**
 * A strategy to estimate the similarity.
 */
fun interface ProjectsSimilarityEstimator : (Set<ComparisonResult<*>>) -> Double
