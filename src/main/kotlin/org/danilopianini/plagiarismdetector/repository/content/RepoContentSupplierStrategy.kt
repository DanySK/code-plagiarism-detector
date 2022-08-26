package org.danilopianini.plagiarismdetector.repository.content

import java.io.File

/**
 * An interface modeling the strategy used for retrieve the repository content.
 */
interface RepoContentSupplierStrategy {
    /**
     * @return the files with the given extensions.
     */
    fun getFilesOf(extensions: Iterable<String>): Iterable<File>
}
