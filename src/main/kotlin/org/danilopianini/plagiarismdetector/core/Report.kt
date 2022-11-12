package org.danilopianini.plagiarismdetector.core

import org.danilopianini.plagiarismdetector.core.detector.ComparisonResult
import org.danilopianini.plagiarismdetector.core.detector.Match
import org.danilopianini.plagiarismdetector.repository.Repository

/**
 * An interface modeling the set of [ComparisonResult]
 * resulting from the comparison of two [Repository].
 */
interface Report<out M : Match> {

    /**
     * The project on which the analysis has been performed.
     */
    val submittedProject: Repository

    /**
     * The project with which the [submittedProject] has been compared.
     */
    val comparedProject: Repository

    /**
     * The set of [ComparisonResult] resulted from the comparison between
     * [comparisonResult] and [submittedProject].
     */
    val comparisonResult: Set<ComparisonResult<M>>
}

/**
 * A simple implementation of the [Report] interface.
 */
class ReportImpl<out M : Match>(
    override val submittedProject: Repository,
    override val comparedProject: Repository,
    override val comparisonResult: Set<ComparisonResult<M>>
) : Report<M>
