package org.danilopianini.plagiarismdetector.caching

import java.io.File
import org.danilopianini.plagiarismdetector.repository.Repository

/**
 * An interface modeling the manager of the knowledge base,
 * i.e. the component which takes care of caching repos content.
 */
interface KnowledgeBaseManager {
    /**
     * Cache the content of the project which is maintained inside the given [Repository].
     */
    fun save(project: Repository)

    /**
     * Returns if the given [project] content has already been cached.
     */
    fun isCached(project: Repository): Boolean

    /**
     * Loads the cached repository content.
     */
    fun load(project: Repository): File
}
