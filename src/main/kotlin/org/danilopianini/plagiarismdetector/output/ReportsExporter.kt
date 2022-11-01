package org.danilopianini.plagiarismdetector.output

import org.danilopianini.plagiarismdetector.core.Report
import org.danilopianini.plagiarismdetector.core.detector.Match

/**
 * An interface modeling the component responsible for
 * exporting the comparison process results.
 */
interface ReportsExporter<in M : Match> : (Set<Report<M>>) -> (Unit)
