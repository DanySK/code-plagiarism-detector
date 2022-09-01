package org.danilopianini.plagiarismdetector.provider

import org.danilopianini.plagiarismdetector.provider.criteria.GitHubSearchCriteria
import org.danilopianini.plagiarismdetector.repository.GitHubRepository
import org.danilopianini.plagiarismdetector.repository.Repository
import org.danilopianini.plagiarismdetector.utils.AuthenticationTokenSupplierStrategy
import org.kohsuke.github.GHFileNotFoundException
import org.kohsuke.github.GHRepositorySearchBuilder
import org.kohsuke.github.GitHub
import org.kohsuke.github.HttpException
import java.net.URL

private const val GITHUB_HOST = "github.com"
private const val UNAUTHORIZED_CODE = 401

/**
 * A provider of GitHub repositories.
 */
class GitHubProvider private constructor(
    private var github: GitHub
) : AbstractRepositoryProvider<GitHub, GHRepositorySearchBuilder, GitHubSearchCriteria>() {
    companion object {
        /**
         * Creates a [GitHubProvider] with anonymous authentication: this is **not** recommended due to
         * rate limits. In case limits are reached the computation blocks until new requests can be made.
         * See GitHub API documentation [here](https://docs.github.com/en/rest/rate-limit).
         */
        fun connectAnonymously() = GitHubProvider(GitHub.connectAnonymously())

        /**
         * Creates a [GitHubProvider] with an authenticated connection.
         * @param tokenSupplier the supplier of the token
         */
        fun connectWithToken(tokenSupplier: AuthenticationTokenSupplierStrategy) =
            GitHubProvider(GitHub.connectUsingOAuth(tokenSupplier.token))
    }

    override fun getRepoByUrl(url: URL): Repository {
        val repoName = url.path.replace(Regex("^/|/$"), "")
        try {
            return GitHubRepository(github.getRepository(repoName))
        } catch (e: GHFileNotFoundException) {
            throw IllegalArgumentException("No repo found at the given address. $e")
        } catch (e: HttpException) {
            error(e)
        }
    }

    override fun urlIsValid(url: URL): Boolean = url.host == GITHUB_HOST

    override fun byCriteria(criteria: GitHubSearchCriteria): Iterable<Repository> {
        try {
            return getMatchingReposByCriteria(criteria)
        } catch (e: HttpException) {
            check(e.responseCode != UNAUTHORIZED_CODE) { "Unauthorized: $e" }
            throw IllegalArgumentException("Error while searching repos matching the given criteria. $e")
        }
    }

    private fun getMatchingReposByCriteria(criteria: GitHubSearchCriteria): Iterable<GitHubRepository> {
        return criteria.apply(github).list().toSet().asSequence()
            .map { GitHubRepository(it) }
            .toSet()
    }
}
