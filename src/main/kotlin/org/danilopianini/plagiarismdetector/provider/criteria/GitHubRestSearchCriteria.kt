package org.danilopianini.plagiarismdetector.provider.criteria

import org.kohsuke.github.GHFork
import org.kohsuke.github.GHRepositorySearchBuilder
import org.kohsuke.github.GitHub

/**
 * An interface modeling search criteria for searching GitHub repositories.
 */
interface GitHubRestSearchCriteria : SearchCriteria<GitHub, GHRepositorySearchBuilder>

/**
 * A search criterion to filter by username.
 * @property username the GitHub username.
 */
data class ByGitHubUserRest(val username: String) : GitHubRestSearchCriteria {
    override operator fun invoke(subject: GitHub): GHRepositorySearchBuilder =
        subject.searchRepositories().user(username).fork(GHFork.PARENT_AND_FORKS)
}

/**
 * A search criterion to filter by the repository name.
 * @property userCriteria the criteria to search of a user the repository must belong to.
 */
data class ByGitHubNameRest(private val repositoryName: String, val userCriteria: ByGitHubUserRest) :
    GitHubRestSearchCriteria {
    override operator fun invoke(subject: GitHub): GHRepositorySearchBuilder =
        userCriteria.invoke(subject).q(repositoryName).`in`("name")
}
