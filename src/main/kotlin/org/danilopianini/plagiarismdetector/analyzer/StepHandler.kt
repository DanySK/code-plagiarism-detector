package org.danilopianini.plagiarismdetector.analyzer

/**
 * An interface modeling a step handler in the analyzer pipeline.
 * @param I the type of the input.
 * @param O the type of the output.
 */
typealias StepHandler<I, O> = (I) -> (O)
