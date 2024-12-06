package org.danilopianini.plagiarismdetector.provider.criteria

private const val QUOTATION_MARK = "%22"
private const val QUESTION_MARK = "?"
private const val URL_SEPARATOR = "/"
private const val EMPTY_CHAR = " "
private const val PLUS_CHAR = "+"
private const val LIKE_CHAR = "%7E"
private const val QUERY_PREFIX = "q="
private const val AND_OPERATOR = "+AND+"

/**
 * An interface modeling search criteria for searching Bitbucket repositories.
 */
interface BitbucketSearchCriteria : SearchCriteria<String, String>

/**
 * A search criterion to filter by username.
 * @property username the Bitbucket username.
 */
class ByBitbucketUser(
    private val username: String,
) : BitbucketSearchCriteria {
    override operator fun invoke(subject: String): String =
        StringBuilder(subject)
            .append(URL_SEPARATOR)
            .append(username)
            .append(QUESTION_MARK)
            .toString()
}

/**
 * A decorator of [BitbucketSearchCriteria] for compound criteria.
 * @property criteria the base criteria to decorate.
 */
open class BitbucketCompoundCriteria(
    private val criteria: BitbucketSearchCriteria,
) : BitbucketSearchCriteria {
    override operator fun invoke(subject: String): String {
        var url = criteria(subject)
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
    criteria: BitbucketSearchCriteria,
) : BitbucketCompoundCriteria(criteria) {
    override operator fun invoke(subject: String): String =
        StringBuilder(super.invoke(subject))
            .append("name")
            .append(LIKE_CHAR)
            .append(QUOTATION_MARK)
            .append(repositoryName.replace(EMPTY_CHAR, PLUS_CHAR))
            .append(QUOTATION_MARK)
            .toString()
}
