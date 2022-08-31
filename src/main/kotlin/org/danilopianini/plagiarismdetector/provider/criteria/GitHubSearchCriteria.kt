package org.danilopianini.plagiarismdetector.provider.criteria

import org.kohsuke.github.GHFork
import org.kohsuke.github.GHRepositorySearchBuilder
import org.kohsuke.github.GitHub

/**
 * An interface modeling search criteria for searching GitHub repositories.
 */
interface GitHubSearchCriteria : SearchCriteria<GitHub, GHRepositorySearchBuilder>

/**
 * A search criterion to filter by username.
 * @property username the GitHub username.
 */
class ByGitHubUser(private val username: String) : GitHubSearchCriteria {
    override fun apply(input: GitHub): GHRepositorySearchBuilder =
        input.searchRepositories().user(username).fork(GHFork.PARENT_AND_FORKS)
}

/**
 * A decorator of [GitHubSearchCriteria] for compound criteria.
 * @property criteria the base criteria to decorate.
 */
abstract class GitHubCompoundCriteria(
    private val criteria: GitHubSearchCriteria
) : GitHubSearchCriteria {
    override fun apply(input: GitHub): GHRepositorySearchBuilder = criteria.apply(input)
}

/**
 * A search criterion to filter by the repository name.
 * @property criteria the criteria to decorate.
 */
class ByGitHubName(
    private val repositoryName: String,
    criteria: GitHubSearchCriteria
) : GitHubCompoundCriteria(criteria) {
    override fun apply(input: GitHub): GHRepositorySearchBuilder =
        super.apply(input).q(repositoryName).`in`("name")
}
