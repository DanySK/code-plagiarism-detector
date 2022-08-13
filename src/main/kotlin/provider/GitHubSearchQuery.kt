package provider

import org.kohsuke.github.GHRepositorySearchBuilder
import org.kohsuke.github.GitHub
import java.net.URL

/**
 * A class implementing a search query for GitHub repositories.
 */
class GitHubSearchQuery : RepositorySearchQuery<GHRepositorySearchBuilder, GitHubSearchCriteria> {

    private val github: GitHub = GitHub.connectAnonymously()

    override fun byLink(url: URL): Iterable<Repository> {
        val repositoryPath = url.path.substringAfter("/")
        return setOf(GitHubRepository(github.getRepository(repositoryPath)))
    }

    override fun byCriteria(criteria: GitHubSearchCriteria): Iterable<Repository> {
        return criteria.apply().list().map { GitHubRepository(it) }
    }
}
