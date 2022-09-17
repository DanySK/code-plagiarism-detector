package org.danilopianini.plagiarismdetector.analyzer.representation.token

/**
 * A simple class implementing a [Gram].
 */
data class GramImpl<T>(override val items: Sequence<T>) : Gram<T> {
    override fun hashCode(): Int {
        return items.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        } else if (javaClass != other?.javaClass) {
            return false
        }
        other as GramImpl<*>
        return other.items.toList() == items.toList()
    }

    override fun toString(): String {
        return items.toList().toString()
    }
}
