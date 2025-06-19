package org.danilopianini.plagiarismdetector.provider

import java.net.URL
import org.danilopianini.plagiarismdetector.provider.criteria.SearchCriteria
import org.danilopianini.plagiarismdetector.repository.GitHubRepository
import org.danilopianini.plagiarismdetector.repository.Repository

/**
 * A provider of GitHub repositories.
 */
abstract class AbstractGitHubProvider<I, O, in C : SearchCriteria<I, O>> : AbstractRepositoryProvider<I, O, C>() {
    final override fun getRepoByUrl(url: URL): Repository {
        // Remove heading and trailing slashes
        val (owner, repo) = url.path.replace(Regex("^/|/$"), "").split("/")
        return GitHubRepository(owner, repo).apply { validate() }
    }

    final override fun urlIsValid(url: URL): Boolean = url.host == "github.com"
}
