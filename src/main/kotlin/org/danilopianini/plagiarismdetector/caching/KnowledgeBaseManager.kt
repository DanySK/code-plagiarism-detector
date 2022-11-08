package org.danilopianini.plagiarismdetector.caching

import java.io.File

/**
 * An interface modeling the manager of the knowledge base,
 * i.e. the component which takes care of caching repos content.
 */
interface KnowledgeBaseManager {

    /**
     * Cache the content of the repo which is inside of [projectDirectory].
     */
    fun save(projectName: String, projectDirectory: File)

    /**
     * Returns if the given [repository] content is already been cached.
     */
    fun isCached(projectName: String): Boolean

    /**
     * Loads the cached repository content.
     */
    fun load(projectName: String): File
}
