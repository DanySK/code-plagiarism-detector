package org.danilopianini.plagiarismdetector.core

import org.danilopianini.plagiarismdetector.core.detector.Match
import org.danilopianini.plagiarismdetector.repository.Repository

/**
 * A technique facade which provides a simplified entry-point
 * interface to execute the detection process in one shot.
 */
interface TechniqueFacade<out M : Match> {

    /**
     * Runs the technique on the [submittedRepository] and the [comparedRepository].
     * During the process the given set of [filesToExclude] are skipped.
     * At the end, only the matches whose duplication percentage is greater
     * than or equals to the given [minDuplicatedPercentage] are reported.
     */
    fun execute(
        submittedRepository: Repository,
        comparedRepository: Repository,
        filesToExclude: Set<String>,
        minDuplicatedPercentage: Double
    ): Result<M>
}
