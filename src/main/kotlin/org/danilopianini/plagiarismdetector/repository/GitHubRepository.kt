package org.danilopianini.plagiarismdetector.repository

import org.kohsuke.github.GHRepository
import java.net.URL

/**
 * A GitHub repository adapter.
 * @property repository the [GHRepository] to be adapted.
 */
data class GitHubRepository(private val repository: GHRepository) : AbstractRepository() {
    override val name: String by lazy { repository.name }

    override val owner: String by lazy { repository.owner.name }

    override val cloneUrl: URL by lazy { repository.htmlUrl }

    override fun toString() = "git@github.com:$owner/$name"
}
