package org.danilopianini.plagiarismdetector.provider

import org.danilopianini.plagiarismdetector.provider.criteria.GitHubSearchCriteria
import org.danilopianini.plagiarismdetector.repository.GitHubRepository
import org.danilopianini.plagiarismdetector.repository.Repository
import org.danilopianini.plagiarismdetector.utils.AuthenticationTokenSupplierStrategy
import org.danilopianini.plagiarismdetector.utils.EnvironmentTokenSupplier
import org.kohsuke.github.GHFileNotFoundException
import org.kohsuke.github.GHRepositorySearchBuilder
import org.kohsuke.github.GitHub
import org.kohsuke.github.HttpException
import java.net.URL

/**
 * A provider of GitHub repositories.
 */
class GitHubProvider(
    tokenSupplier: AuthenticationTokenSupplierStrategy = EnvironmentTokenSupplier(AUTH_TOKEN_NAME)
) : AbstractRepositoryProvider<GitHub, GHRepositorySearchBuilder, GitHubSearchCriteria>() {
    companion object {
        private const val AUTH_TOKEN_NAME = "GH_TOKEN"
        private const val GITHUB_HOST = "github.com"
        private const val UNAUTHORIZED_CODE = 401
    }
    private val github = GitHub.connectUsingOAuth(tokenSupplier.token)

    override fun getRepoByUrl(url: URL): Repository {
        val repoName = url.path.replace(Regex("^/|/$"), "")
        try {
            return GitHubRepository(github.getRepository(repoName))
        } catch (e: GHFileNotFoundException) {
            throw IllegalArgumentException(e.message)
        } catch (e: HttpException) {
            throw IllegalStateException(e.message)
        }
    }

    override fun urlIsValid(url: URL): Boolean = url.host == GITHUB_HOST

    override fun byCriteria(criteria: GitHubSearchCriteria): Iterable<Repository> {
        try {
            return getMatchingReposByCriteria(criteria)
        } catch (e: HttpException) {
            check(e.responseCode != UNAUTHORIZED_CODE) { "${e.message}" }
            throw IllegalArgumentException(e.message)
        }
    }

    private fun getMatchingReposByCriteria(criteria: GitHubSearchCriteria): Iterable<GitHubRepository> {
        return criteria.apply(github).list().toSet().asSequence()
            .map { GitHubRepository(it) }
            .toSet()
    }
}
