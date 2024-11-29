package org.danilopianini.plagiarismdetector.repository.content

import java.io.File

/**
 * An interface modeling the strategy used for retrieve the repository content.
 */
interface RepoContentSupplier {
    /**
     * @return the files match the given pattern.
     */
    fun filesMatching(pattern: Regex): Sequence<File>
}
