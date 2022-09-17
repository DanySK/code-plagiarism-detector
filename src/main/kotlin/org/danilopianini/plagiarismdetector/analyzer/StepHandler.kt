package org.danilopianini.plagiarismdetector.analyzer

/**
 * An interface modeling a step handler in the analyzer pipeline.
 * @param I the type of the input.
 * @param O the type of the output.
 */
fun interface StepHandler<I, O> {
    /**
     * Process the given input.
     * @return the input transformation result.
     */
    fun process(input: I): O
}
