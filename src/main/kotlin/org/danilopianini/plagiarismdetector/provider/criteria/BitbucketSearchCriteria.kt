package org.danilopianini.plagiarismdetector.provider.criteria

private const val QUOTATION_MARK = "%22"
private const val QUESTION_MARK = "?"
private const val URL_SEPARATOR = "/"
private const val LIKE_CHAR = "%7E"
private const val QUERY_PREFIX = "q="
private const val AND_OPERATOR = "+AND+"

/**
 * An interface modeling search criteria for searching Bitbucket repositories.
 */
interface BitbucketSearchCriteria : SearchCriteria<String>

/**
 * A search criterion to filter by username.
 * @property username the Bitbucket username.
 */
class ByBitbucketUser(private val username: String) : BitbucketSearchCriteria {
    override fun apply(): String = StringBuilder(URL_SEPARATOR)
        .append(username)
        .append(QUESTION_MARK)
        .toString()
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
        url += if (url.endsWith(QUESTION_MARK)) QUERY_PREFIX else AND_OPERATOR
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
    override fun apply(): String = StringBuilder(super.apply())
        .append("name")
        .append(LIKE_CHAR)
        .append(QUOTATION_MARK)
        .append(repositoryName)
        .append(QUOTATION_MARK).toString()
}
