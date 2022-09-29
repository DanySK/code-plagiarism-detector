package org.danilopianini.plagiarismdetector.detector

import org.danilopianini.plagiarismdetector.analyzer.representation.SourceRepresentation

/**
 * A comparison strategy, encapsulating the specific algorithm used to check similarities.
 */
interface ComparisonStrategy<in S : SourceRepresentation<T>, T, out M : Match> : (Pair<S, S>) -> (Sequence<Match>)
