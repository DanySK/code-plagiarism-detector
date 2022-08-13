package provider

/**
 * An interface modeling search criteria for searching Bitbucket repositories.
 */
interface BitbucketSearchCriteria : SearchCriteria<String>

/**
 * A criteria matching by username.
 */
class ByBitbucketUser(private val username: String) : BitbucketSearchCriteria {
    override fun apply(): String = "/$username?"
}

/**
 * A decorator of [BitbucketSearchCriteria] for compound criteria.
 */
abstract class BitbucketCompoundCriteria(
    private val criteria: BitbucketSearchCriteria
) : BitbucketSearchCriteria {
    override fun apply(): String = criteria.apply()
}

/**
 * A criteria matching by repository name.
 */
class ByBitbucketName(
    private val repositoryName: String,
    criteria: BitbucketSearchCriteria
) : BitbucketCompoundCriteria(criteria) {
    override fun apply(): String {
        var url = super.apply()
        url += if (url.endsWith("?")) "q=" else "+AND+"
        url += "name+%7E+%22$repositoryName%22"
        return url
    }
}
