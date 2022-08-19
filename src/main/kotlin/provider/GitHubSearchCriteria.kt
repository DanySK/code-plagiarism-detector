package provider

/**
 * An interface modeling search criteria for searching GitHub repositories.
 */
interface GitHubSearchCriteria : SearchCriteria<String>

/**
 * A criteria matching by username.
 */
class ByGitHubUser(private val username: String) : GitHubSearchCriteria {
    override fun apply(): String = "user:$username fork:true"
}

/**
 * A decorator of [GitHubSearchCriteria] for compound criteria.
 */
abstract class GitHubCompoundCriteria(
    private val criteria: GitHubSearchCriteria
) : GitHubSearchCriteria {
    override fun apply(): String = criteria.apply()
}

/**
 * A criteria matching by repository name.
 */
class ByGitHubName(
    private val repositoryName: String,
    criteria: GitHubSearchCriteria
) : GitHubCompoundCriteria(criteria) {
    override fun apply(): String = "${super.apply()} $repositoryName"
}
