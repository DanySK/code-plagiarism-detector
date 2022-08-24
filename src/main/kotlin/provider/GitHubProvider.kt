package provider

import com.jcabi.github.Coordinates
import com.jcabi.github.RtGithub
import com.jcabi.github.Search
import com.jcabi.http.wire.RetryWire
import org.slf4j.LoggerFactory
import provider.criteria.GitHubSearchCriteria
import provider.token.EnvironmentTokenSupplier
import provider.token.TokenSupplierStrategy
import repository.GitHubRepository
import repository.Repository
import java.net.URL

/**
 * A class implementing a search query for GitHub repositories.
 */
class GitHubProvider : AbstractRepositoryProvider<String, GitHubSearchCriteria>() {
    companion object {
        private const val AUTH_TOKEN_NAME = "GH_TOKEN"
        private const val URL_SEPARATOR = "/"
        private const val GITHUB_HOST = "github.com"
        private const val SORT_CRITERIA = "Best Match"
    }
    private val tokenSupplierStrategy: TokenSupplierStrategy = EnvironmentTokenSupplier(AUTH_TOKEN_NAME)
    private val logger = LoggerFactory.getLogger(this.javaClass.name)
    private val github = RtGithub(
        RtGithub(tokenSupplierStrategy.token).entry().through(RetryWire::class.java)
    )

    override fun getRepoByUrl(url: URL): Repository? {
        val tokens = url.path.removePrefix(URL_SEPARATOR).removeSuffix(URL_SEPARATOR).split(URL_SEPARATOR)
        if (!github.repos().exists(Coordinates.Simple(tokens[0], tokens[1]))) {
            logger.error("No repository found at given address ($url)")
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
        return github.search().repos(criteria.apply(), SORT_CRITERIA, Search.Order.ASC)
            .asSequence()
            .map { GitHubRepository(it) }
            .toSet()
    }
}
