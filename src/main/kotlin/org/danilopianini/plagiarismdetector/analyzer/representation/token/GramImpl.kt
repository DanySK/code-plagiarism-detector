package org.danilopianini.plagiarismdetector.analyzer.representation.token

/**
 * A simple class implementing a [Gram].
 */
data class GramImpl<T>(override val items: Sequence<T>) : Gram<T> {
    override fun toString(): String {
        return items.toList().toString()
    }
}
