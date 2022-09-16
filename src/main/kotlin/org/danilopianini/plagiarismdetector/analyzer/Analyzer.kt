package org.danilopianini.plagiarismdetector.analyzer

import org.danilopianini.plagiarismdetector.analyzer.representation.SourceRepresentation

/**
 * An interface modeling the analyzer, the component that analyze the source
 * code producing in output a comparable representation of the source file.
 * @param <I> the type of the source file given in input
 * @param <O> the type of output
 * @param <T> the type of the representation
 */
interface Analyzer<I, out O : SourceRepresentation<T>, T> {
    /**
     * Executes the analysis.
     * @return a comparable representation of the source file.
     */
    fun execute(input: I): O
}
