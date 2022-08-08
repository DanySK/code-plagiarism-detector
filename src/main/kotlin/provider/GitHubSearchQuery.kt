package provider

import org.kohsuke.github.GHRepositorySearchBuilder
import org.kohsuke.github.GitHub
import java.net.URL

/**
 * A class implementing a search query for GitHub repositories.
 */
class GitHubSearchQuery : RepositorySearchQuery {

    private val github: GitHub = GitHub.connectAnonymously()

    override fun byLink(url: URL): RepositorySearchQuery.RepositoryQueryResult {
        val repositoryPath = url.path.substringAfter("/")
        return GitHubQueryResult(setOf(GitHubRepository(github.getRepository(repositoryPath))))
    }

    override fun byCriteria(): RepositorySearchQuery.RepositoryQueryCriteria = GitHubQueryCriteria()

    /**
     * A class implementing GitHub search query criteria.
     */
    private inner class GitHubQueryCriteria : RepositorySearchQuery.RepositoryQueryCriteria {

        private val searchCriteria: GHRepositorySearchBuilder = this@GitHubSearchQuery.github.searchRepositories()

        override fun byName(repositoryName: String): RepositorySearchQuery.RepositoryQueryCriteria {
            searchCriteria.q(repositoryName)
            return this
        }

        override fun byUser(user: String): RepositorySearchQuery.RepositoryQueryCriteria {
            searchCriteria.user(user)
            return this
        }

        override fun search(): RepositorySearchQuery.RepositoryQueryResult {
            return GitHubQueryResult(
                searchCriteria.list()
                    .map { GitHubRepository(it) }
            )
        }
    }

    /**
     * A github query result.
     */
    private class GitHubQueryResult(
        override val result: Iterable<Repository>
    ) : RepositorySearchQuery.RepositoryQueryResult
}
