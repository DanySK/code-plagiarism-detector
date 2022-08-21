package provider

import java.io.File

/**
 * An interface modeling a repository, a storage location for software packages.
 */
interface Repository {

    /**
     * The name of the repository.
     */
    val name: String

    /**
     * The contributors of the repository.
     */
    val contributors: Iterable<String>

    /**
     * Get all the source files contained in this repository by language.
     * @param language the programming language of the sources to return.
     * @return an [Iterable] of source [File]s of the given language.
     */
    fun getSources(language: String): Iterable<File>
}
