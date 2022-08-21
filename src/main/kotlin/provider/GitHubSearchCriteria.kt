package provider

/**
 * An interface modeling search criteria for searching GitHub repositories.
 */
interface GitHubSearchCriteria : SearchCriteria<String>

/**
 * A search criterion to filter by username.
 * @property username the GitHub username.
 */
class ByGitHubUser(private val username: String) : GitHubSearchCriteria {
    override fun apply(): String = "user:$username fork:true"
}

/**
 * A decorator of [GitHubSearchCriteria] for compound criteria.
 * @property criteria the base criteria to decorate.
 */
abstract class GitHubCompoundCriteria(
    private val criteria: GitHubSearchCriteria
) : GitHubSearchCriteria {
    override fun apply(): String = criteria.apply()
}

/**
 * A search criterion to filter by the repository name.
 * @property criteria the criteria to decorate.
 */
class ByGitHubName(
    private val repositoryName: String,
    criteria: GitHubSearchCriteria
) : GitHubCompoundCriteria(criteria) {
    override fun apply(): String = "${super.apply()} $repositoryName"
}
