package org.danilopianini.plagiarismdetector.provider

import org.danilopianini.plagiarismdetector.provider.criteria.GitHubSearchCriteria
import org.danilopianini.plagiarismdetector.repository.GitHubRepository
import org.danilopianini.plagiarismdetector.repository.Repository
import org.danilopianini.plagiarismdetector.utils.EnvironmentTokenSupplier
import org.kohsuke.github.GHException
import org.kohsuke.github.GHFileNotFoundException
import org.kohsuke.github.GHRepositorySearchBuilder
import org.kohsuke.github.GitHub
import org.kohsuke.github.HttpException
import java.net.URL

/**
 * A provider of GitHub repositories.
 */
class GitHubProvider : AbstractRepositoryProvider<GHRepositorySearchBuilder, GitHubSearchCriteria>() {
    companion object {
        private const val AUTH_TOKEN_NAME = "GH_TOKEN"
        private const val GITHUB_HOST = "github.com"
    }
    private val tokenSupplier = EnvironmentTokenSupplier(AUTH_TOKEN_NAME)
    private val github = GitHub.connectUsingOAuth(tokenSupplier.token)

    override fun getRepoByUrl(url: URL): Repository {
        val repoName = url.path.replace(Regex("^/|/$"), "")
        try {
            return GitHubRepository(github.getRepository(repoName))
        } catch (e: GHFileNotFoundException) {
            throw IllegalArgumentException("FILE NOT FOUND: $e")
        }
    }

    override fun urlIsValid(url: URL): Boolean = url.host == GITHUB_HOST

    override fun byCriteria(criteria: GitHubSearchCriteria): Iterable<Repository> {
        try {
            return getMatchingReposByCriteria(criteria)
        } catch (e: GHException) {
            throw IllegalArgumentException(e)
        } catch (e: HttpException) {
            error("Validation failed $e")
        }
    }

    private fun getMatchingReposByCriteria(criteria: GitHubSearchCriteria): Iterable<GitHubRepository> {
        return criteria.apply().list().toSet().asSequence()
            .map { GitHubRepository(it) }
            .toSet()
    }
}
