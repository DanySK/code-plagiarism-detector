package provider.query

/**
 * An interface modeling search criteria for searching Bitbucket repositories.
 */
interface BitbucketSearchCriteria : SearchCriteria<String>

/**
 * A search criterion to filter by username.
 * @property username the Bitbucket username.
 */
class ByBitbucketUser(private val username: String) : BitbucketSearchCriteria {
    override fun apply(): String = "/$username?"
}

/**
 * A decorator of [BitbucketSearchCriteria] for compound criteria.
 * @property criteria the base criteria to decorate.
 */
abstract class BitbucketCompoundCriteria(
    private val criteria: BitbucketSearchCriteria
) : BitbucketSearchCriteria {
    override fun apply(): String {
        var url = criteria.apply()
        url += if (url.endsWith("?")) "q=" else "+AND+"
        return url
    }
}

/**
 * A search criterion to filter by the repository name.
 * @property criteria the criteria to decorate.
 */
class ByBitbucketName(
    private val repositoryName: String,
    criteria: BitbucketSearchCriteria
) : BitbucketCompoundCriteria(criteria) {
    override fun apply(): String = super.apply().plus("name+%7E+%22$repositoryName%22")
}
