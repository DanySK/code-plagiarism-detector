package provider.query

import com.jcabi.github.Coordinates
import com.jcabi.github.RtGithub
import com.jcabi.github.Search
import com.jcabi.http.wire.RetryWire
import org.slf4j.LoggerFactory
import provider.query.token.TokenSupplier
import provider.repository.GitHubRepository
import provider.repository.Repository
import java.net.URL

private const val GITHUB_HOST = "github.com"

/**
 * A class implementing a search query for GitHub repositories.
 */
class GitHubSearchQuery(
    private val tokenSupplier: TokenSupplier = TokenSupplier { System.getenv("GH_TOKEN") }
) : BaseRepoSearchQuery<String, GitHubSearchCriteria>() {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    private val github = RtGithub(
        RtGithub(tokenSupplier.getToken()).entry().through(RetryWire::class.java)
    )

    override fun getRepoByUrl(url: URL): Repository? {
        val tokens = url.path.removePrefix("/").removeSuffix("/").split("/")
        if (!github.repos().exists(Coordinates.Simple(tokens[0], tokens[1]))) {
            logger.error("No repo found at given address ($url)")
            return null
        }
        return GitHubRepository(github.repos().get(Coordinates.Simple(tokens[0], tokens[1])))
    }

    override fun urlIsValid(url: URL): Boolean = url.host == GITHUB_HOST

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
