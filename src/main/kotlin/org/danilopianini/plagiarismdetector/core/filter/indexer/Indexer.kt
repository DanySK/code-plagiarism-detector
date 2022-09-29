package org.danilopianini.plagiarismdetector.core.filter.indexer

import org.danilopianini.plagiarismdetector.core.analyzer.representation.SourceRepresentation

/**
 * An interface modeling a generator of indexes.
 */
interface Indexer<in S : SourceRepresentation<T>, T, I> : (S) -> (I)
