package org.danilopianini.plagiarismdetector.provider.criteria

import org.danilopianini.plagiarismdetector.utils.EnvironmentTokenSupplier
import org.kohsuke.github.GHFork
import org.kohsuke.github.GHRepositorySearchBuilder
import org.kohsuke.github.GitHub

private const val AUTH_TOKEN_NAME = "GH_TOKEN"

/**
 * An interface modeling search criteria for searching GitHub repositories.
 */
interface GitHubSearchCriteria : SearchCriteria<GHRepositorySearchBuilder>

/**
 * A search criterion to filter by username.
 * @property username the GitHub username.
 */
class ByGitHubUser(private val username: String) : GitHubSearchCriteria {
    private val tokenSupplier = EnvironmentTokenSupplier(AUTH_TOKEN_NAME)
    private val github = GitHub.connectUsingOAuth(tokenSupplier.token)
    override fun apply(): GHRepositorySearchBuilder =
        github.searchRepositories().user(username).fork(GHFork.PARENT_AND_FORKS)
}

/**
 * A decorator of [GitHubSearchCriteria] for compound criteria.
 * @property criteria the base criteria to decorate.
 */
abstract class GitHubCompoundCriteria(
    private val criteria: GitHubSearchCriteria
) : GitHubSearchCriteria {
    override fun apply(): GHRepositorySearchBuilder = criteria.apply()
}

/**
 * A search criterion to filter by the repository name.
 * @property criteria the criteria to decorate.
 */
class ByGitHubName(
    private val repositoryName: String,
    criteria: GitHubSearchCriteria
) : GitHubCompoundCriteria(criteria) {
    override fun apply(): GHRepositorySearchBuilder = super.apply().q(repositoryName).`in`("name")
}
