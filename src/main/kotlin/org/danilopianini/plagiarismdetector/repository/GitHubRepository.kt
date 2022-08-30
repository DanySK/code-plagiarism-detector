package org.danilopianini.plagiarismdetector.repository

import com.jcabi.github.Repo
import org.kohsuke.github.GHRepository
import java.net.URL

/**
 * A GitHub repository adapter.
 * @property repository the [Repo] to be adapted.
 */
data class GitHubRepository(private val repository: GHRepository) : AbstractRepository() {
    override val name: String = repository.name

    override val owner: String = repository.owner.name

    override val cloneUrl: URL = repository.htmlUrl

    override fun toString() = "Repository $name of $owner"
}
