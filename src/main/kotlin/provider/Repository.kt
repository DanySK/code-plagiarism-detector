package provider

import java.io.InputStream

/**
 * A repository.
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
     * Get all the sources contained in this repository by language.
     * @param language the programming language of the sources to return
     * @return an [Iterable] of [InputStream] representing source files.
     */
    fun getSources(language: String): Iterable<InputStream>
}
