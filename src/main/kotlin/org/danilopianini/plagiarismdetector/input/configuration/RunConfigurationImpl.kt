package org.danilopianini.plagiarismdetector.input.configuration

import org.danilopianini.plagiarismdetector.core.TechniqueFacade
import org.danilopianini.plagiarismdetector.core.detector.Match
import org.danilopianini.plagiarismdetector.output.ReportsExporter
import org.danilopianini.plagiarismdetector.repository.Repository

/**
 * Implementation of a generic [RunConfiguration].
 */
data class RunConfigurationImpl<M : Match>(
    override val technique: TechniqueFacade<M>,
    override val minDuplicatedPercentage: Double,
    override val submission: Set<Repository>,
    override val corpus: Set<Repository>,
    override val filesToExclude: Set<String>,
    override val exporter: ReportsExporter<M>
) : RunConfiguration<M>
