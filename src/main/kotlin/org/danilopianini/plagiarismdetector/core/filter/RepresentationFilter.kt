package org.danilopianini.plagiarismdetector.core.filter

import org.danilopianini.plagiarismdetector.core.analyzer.representation.SourceRepresentation

/**
 * An interface modeling a filter of [SourceRepresentation].
 */
fun interface RepresentationFilter<S : SourceRepresentation<T>, T> : (S, Sequence<S>) -> (Sequence<S>)
