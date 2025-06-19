package org.danilopianini.plagiarismdetector.core.analyzer

import java.io.File
import org.danilopianini.plagiarismdetector.core.analyzer.representation.SourceRepresentation

/**
 * An interface modeling the analyzer, the component that analyze the source
 * code producing in output a comparable representation of the source file.
 * @param <I> the type of the source file given in input
 * @param <O> the type of output
 * @param <T> the type of the representation
 */
interface Analyzer<out S : SourceRepresentation<T>, T> : (File) -> (S)
