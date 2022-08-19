package provider

import org.kohsuke.github.GHException
import org.kohsuke.github.GHRepositorySearchBuilder
import org.kohsuke.github.GitHub
import org.slf4j.LoggerFactory
import java.io.FileNotFoundException
import java.net.URL

/**
 * A class implementing a search query for GitHub repositories.
 */
class GitHubSearchQuery : RepositorySearchQuery<GHRepositorySearchBuilder, GitHubSearchCriteria> {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    // TODO authentication
    private val github: GitHub = GitHub.connectAnonymously()

    override fun byLink(url: URL): Iterable<Repository> {
        try {
            return getMatchingReposByPath(url.path.substringAfter("/"))
        } catch (e: FileNotFoundException) {
            logger.error("No matching repos at url $url: ${e.message}")
        }
        return emptySet()
    }

    private fun getMatchingReposByPath(path: String): Iterable<GitHubRepository> {
        return setOf(GitHubRepository(github.getRepository(path)))
    }

    override fun byCriteria(criteria: GitHubSearchCriteria): Iterable<Repository> {
        try {
            return getMatchingReposByCriteria(criteria)
        } catch (e: GHException) {
            logger.error("No user: ${e.message}")
        }
        return emptySet()
    }

    private fun getMatchingReposByCriteria(criteria: GitHubSearchCriteria): Iterable<GitHubRepository> {
        return criteria.apply().list().map { GitHubRepository(it) }
    }
}
