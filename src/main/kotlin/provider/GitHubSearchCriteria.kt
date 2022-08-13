package provider

import org.kohsuke.github.GHFork
import org.kohsuke.github.GHRepositorySearchBuilder
import org.kohsuke.github.GitHub

/**
 * An interface modeling search criteria for searching GitHub repositories.
 */
interface GitHubSearchCriteria : SearchCriteria<GHRepositorySearchBuilder>

/**
 * A criteria matching by username.
 */
class ByGitHubUser(private val username: String) : GitHubSearchCriteria {
    // TODO Anonymously connection should be replaced with authenticated one
    override fun apply(): GHRepositorySearchBuilder =
        GitHub.connectAnonymously().searchRepositories().user(username).fork(GHFork.PARENT_AND_FORKS)
}

/**
 * A decorator of [GitHubSearchCriteria] for compound criteria.
 */
abstract class GitHubCompoundCriteria(
    private val criteria: GitHubSearchCriteria
) : GitHubSearchCriteria {
    override fun apply(): GHRepositorySearchBuilder = criteria.apply()
}

/**
 * A criteria matching by repository name.
 */
class ByGitHubName(
    private val repositoryName: String,
    criteria: GitHubSearchCriteria
) : GitHubCompoundCriteria(criteria) {
    override fun apply(): GHRepositorySearchBuilder = super.apply().q(repositoryName).`in`("name")
}
