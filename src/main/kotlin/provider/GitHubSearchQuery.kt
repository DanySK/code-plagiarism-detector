package provider

import com.jcabi.github.Coordinates
import com.jcabi.github.RtGithub
import com.jcabi.github.Search
import org.slf4j.LoggerFactory
import java.net.URL

/**
 * A class implementing a search query for GitHub repositories.
 */
class GitHubSearchQuery : RepositorySearchQuery<String, GitHubSearchCriteria> {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    // TODO authentication
    private val github = RtGithub()

    override fun byLink(url: URL): Iterable<Repository> {
        val tokens = url.path.removePrefix("/").removeSuffix("/").split("/")
        if (tokens.count() != 2) {
            throw java.lang.IllegalArgumentException("The url must match in owner/repo-name format")
        }
        return if (github.repos().exists(Coordinates.Simple(tokens[0], tokens[1]))) {
            setOf(GitHubRepository(github.repos().get(Coordinates.Simple(tokens[0], tokens[1]))))
        } else {
            logger.error("no repo found")
            emptySet()
        }
    }

    override fun byCriteria(criteria: GitHubSearchCriteria): Iterable<Repository> {
        try {
            return getMatchingReposByCriteria(criteria)
        } catch (e: AssertionError) {
            logger.error(e.message)
        }
        return emptySet()
    }

    private fun getMatchingReposByCriteria(criteria: GitHubSearchCriteria): Iterable<GitHubRepository> {
        return github.search().repos(criteria.apply(), "Best Match", Search.Order.ASC)
            .asSequence()
            .map { GitHubRepository(it) }
            .toSet()
    }
}
