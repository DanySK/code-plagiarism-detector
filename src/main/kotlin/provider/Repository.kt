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
     * An input stream iterable of each repository raw source code.
     */
    val sources: Iterable<InputStream>
}
