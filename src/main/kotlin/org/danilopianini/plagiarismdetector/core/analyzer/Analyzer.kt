package org.danilopianini.plagiarismdetector.core.analyzer

import org.danilopianini.plagiarismdetector.core.analyzer.representation.SourceRepresentation
import java.io.File

/**
 * An interface modeling the analyzer, the component that analyze the source
 * code producing in output a comparable representation of the source file.
 * @param <I> the type of the source file given in input
 * @param <O> the type of output
 * @param <T> the type of the representation
 */
interface Analyzer<out O : SourceRepresentation<T>, T> : (File) -> (O)
