package org.danilopianini.plagiarismdetector.detector

import org.danilopianini.plagiarismdetector.analyzer.representation.SourceRepresentation

/**
 * An interface modeling a detector of similarities between two [SourceRepresentation].
 */
interface PlagiarismDetector<in S : SourceRepresentation<T>, T, out M : Match> :
    (Pair<S, S>) -> (ComparisonResult<M>)
