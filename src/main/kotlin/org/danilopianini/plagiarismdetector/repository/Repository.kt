package org.danilopianini.plagiarismdetector.repository

import java.io.File
import java.net.URL

/**
 * An interface modeling a repository, a storage location for software packages.
 */
interface Repository {

    /**
     * The name of the repository.
     */
    val name: String

    /**
     * The owner of the repository.
     */
    val owner: String

    /**
     * The [URL] to clone the repo.
     */
    val cloneUrl: URL

    /**
     * Get all the source files contained in this repository whose name matches the given pattern.
     * @param pattern the [Regex] the pattern that the returned files must meet
     * @return a [Sequence] of source [File]s.
     */
    fun getSources(pattern: Regex): Sequence<File>
}
