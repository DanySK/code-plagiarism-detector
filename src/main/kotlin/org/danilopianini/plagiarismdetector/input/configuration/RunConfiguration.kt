package org.danilopianini.plagiarismdetector.input.configuration

import org.danilopianini.plagiarismdetector.core.TechniqueFacade
import org.danilopianini.plagiarismdetector.core.detector.Match
import org.danilopianini.plagiarismdetector.output.ReportsExporter
import org.danilopianini.plagiarismdetector.repository.Repository

/**
 * A run configuration encapsulating all the options to use during a detection session.
 */
interface RunConfiguration<M : Match> {

    /**
     * The [TechniqueFacade] to use.
     */
    val technique: TechniqueFacade<M>

    /**
     * The percentage of duplicated code under which matches are not reported.
     */
    val minDuplicatedPercentage: Double

    /**
     * The submission set to check.
     */
    val submission: Set<Repository>

    /**
     * The corpus set on which test similarities.
     */
    val corpus: Set<Repository>

    /**
     * Set of file names to exclude during the comparison.
     */
    val filesToExclude: Set<String>

    /**
     * The concrete [ReportsExporter] to use to export the results.
     */
    val exporter: ReportsExporter<M>
}
