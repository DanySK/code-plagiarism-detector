package org.danilopianini.plagiarismdetector.output

import org.danilopianini.plagiarismdetector.core.Result
import org.danilopianini.plagiarismdetector.core.detector.Match

/**
 * An interface modeling the component responsible for
 * exporting the comparison process results.
 */
interface ResultsExporter<in M : Match> : (Set<Result<M>>) -> (Unit)
