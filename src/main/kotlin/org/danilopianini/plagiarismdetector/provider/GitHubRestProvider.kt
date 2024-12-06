package org.danilopianini.plagiarismdetector.provider

import org.danilopianini.plagiarismdetector.provider.authentication.AuthenticationTokenSupplierStrategy
import org.danilopianini.plagiarismdetector.provider.criteria.GitHubRestSearchCriteria
import org.danilopianini.plagiarismdetector.repository.GitHubRepository
import org.danilopianini.plagiarismdetector.repository.Repository
import org.kohsuke.github.GHRepositorySearchBuilder
import org.kohsuke.github.GitHub
import org.kohsuke.github.HttpException

/**
 * A provider of GitHub repositories.
 */
class GitHubRestProvider private constructor(
    private var github: GitHub,
) : AbstractGitHubProvider<GitHub, GHRepositorySearchBuilder, GitHubRestSearchCriteria>() {
    /**
     * A companion object to create instances of [GitHubRestProvider].
     */
    companion object {
        private const val UNAUTHORIZED_CODE = 401

        /**
         * Creates a [GitHubRestProvider] with anonymous authentication: this is **not** recommended due to
         * rate limits. In case limits are reached the computation blocks until new requests can be made.
         * See GitHub API documentation [here](https://docs.github.com/en/rest/rate-limit).
         */
        fun connectAnonymously() = GitHubRestProvider(GitHub.connectAnonymously())

        /**
         * Creates a [GitHubRestProvider] with an authenticated connection.
         * @param tokenSupplier the supplier of the token
         */
        fun connectWithToken(tokenSupplier: AuthenticationTokenSupplierStrategy) =
            GitHubRestProvider(GitHub.connectUsingOAuth(tokenSupplier.token))
    }

    override fun byCriteria(criteria: GitHubRestSearchCriteria): Sequence<Repository> {
        try {
            return getMatchingReposByCriteria(criteria)
        } catch (e: HttpException) {
            check(e.responseCode != UNAUTHORIZED_CODE) {
                "Unauthorized access to ${e.url} with criteria $criteria: $e"
            }
            error("Error accessing ${e.url} when matching criteria $criteria. $e")
        }
    }

    private fun getMatchingReposByCriteria(criteria: GitHubRestSearchCriteria): Sequence<GitHubRepository> =
        criteria(github)
            .list()
            .toSet()
            .asSequence()
            .map(::GitHubRepository)
}
