package org.danilopianini.plagiarismdetector.provider

import com.jcabi.github.Coordinates
import com.jcabi.github.RtGithub
import com.jcabi.github.Search
import com.jcabi.http.wire.RetryWire
import org.danilopianini.plagiarismdetector.provider.criteria.GitHubSearchCriteria
import org.danilopianini.plagiarismdetector.repository.GitHubRepository
import org.danilopianini.plagiarismdetector.repository.Repository
import org.danilopianini.plagiarismdetector.utils.EnvironmentTokenSupplier
import java.net.URL

/**
 * A provider of GitHub repositories.
 */
class GitHubProvider : AbstractRepositoryProvider<String, GitHubSearchCriteria>() {
    companion object {
        private const val AUTH_TOKEN_NAME = "GH_TOKEN"
        private const val URL_SEPARATOR = "/"
        private const val GITHUB_HOST = "github.com"
        private const val SORT_CRITERIA = "Best Match"
        private const val ERROR_MSG = "The listed repositories cannot be searched either because" +
            " the resources do not exist or you do not have permission to view them"
    }
    private val tokenSupplier = EnvironmentTokenSupplier(AUTH_TOKEN_NAME)
    private val github = RtGithub(
        RtGithub(tokenSupplier.token).entry().through(RetryWire::class.java)
    )

    override fun getRepoByUrl(url: URL): Repository {
        val (user, name) = url.path.removePrefix(URL_SEPARATOR).removeSuffix(URL_SEPARATOR).split(URL_SEPARATOR)
        require(github.repos().exists(Coordinates.Simple(user, name))) { ERROR_MSG }
        return GitHubRepository(github.repos().get(Coordinates.Simple(user, name)))
    }

    override fun urlIsValid(url: URL): Boolean = url.host == GITHUB_HOST

    override fun byCriteria(criteria: GitHubSearchCriteria): Iterable<Repository> {
        try {
            return getMatchingReposByCriteria(criteria)
        } catch (e: AssertionError) {
            throw IllegalArgumentException("$ERROR_MSG: $e")
        }
    }

    private fun getMatchingReposByCriteria(criteria: GitHubSearchCriteria): Iterable<GitHubRepository> {
        return github.search().repos(criteria.apply(), SORT_CRITERIA, Search.Order.ASC)
            .asSequence()
            .map { GitHubRepository(it) }
            .toSet()
    }
}
