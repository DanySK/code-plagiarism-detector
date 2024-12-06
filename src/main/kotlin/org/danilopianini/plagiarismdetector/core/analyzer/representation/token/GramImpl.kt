package org.danilopianini.plagiarismdetector.core.analyzer.representation.token

/**
 * A simple class implementing a [Gram].
 */
data class GramImpl<T>(
    override val items: List<T>,
) : Gram<T> {
    override fun toString(): String = "Gram$items"
}
