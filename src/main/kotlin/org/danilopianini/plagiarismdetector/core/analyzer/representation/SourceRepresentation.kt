package org.danilopianini.plagiarismdetector.core.analyzer.representation

import java.io.File

/**
 * An interface modeling the intermediate representation, which
 * is generated from the source file, prior to comparison.
 * @param <T> the type of the representation.
 */
interface SourceRepresentation<T> {
    /**
     * The source file of this representation.
     */
    val sourceFile: File

    /**
     * The representation of the source file.
     */
    val representation: T
}
