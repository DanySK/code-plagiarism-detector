package org.danilopianini.plagiarismdetector.repository

import org.kohsuke.github.GHRepository
import java.net.URL

/**
 * A GitHub repository adapter.
 * @property repository the [GHRepository] to be adapted.
 */
data class GitHubRepository(private val repository: GHRepository) : AbstractRepository() {
    override val name: String
        get() = repository.name

    override val owner: String
        get() = repository.owner.name

    override val cloneUrl: URL
        get() = repository.htmlUrl

    override fun toString() = "git@github.com:$owner/$name"
}
