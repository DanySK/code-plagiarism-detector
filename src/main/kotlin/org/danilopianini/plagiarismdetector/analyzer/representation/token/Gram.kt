package org.danilopianini.plagiarismdetector.analyzer.representation.token

/**
 * An interface modeling a [n-gram](https://en.wikipedia.org/wiki/N-gram),
 * a contiguous sequence of items.
 * @param T the type of the items that is composed of.
 */
interface Gram<T> {
    /**
     * The items that compose the gram.
     */
    val items: Sequence<T>
}
