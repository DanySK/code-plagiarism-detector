package provider

import com.mashape.unirest.http.Unirest
import org.json.JSONObject
import org.slf4j.LoggerFactory
import java.net.URL

private const val BASE_URL = "https://api.bitbucket.org/2.0/repositories"
private const val ERROR_FIELD = "error"
private const val NEXT_PAGE_FIELD = "next"
private const val VALUES_FIELD = "values"
private const val MESSAGE_FIELD = "message"

/**
 * A class implementing a search query for Bitbucket repositories.
 */
class BitbucketSearchQuery : RepositorySearchQuery<String, BitbucketSearchCriteria> {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    override fun byLink(url: URL): Iterable<Repository> {
        val path = url.path.removePrefix("/").removeSuffix("/")
        if (path.split("/").count() != 2) {
            logger.error("The link must be in owner/repo-name format")
            return emptySet()
        }
        val response = getResponse(BASE_URL.plus(url.path))
        return if (isError(response)) {
            logger.error(response.getJSONObject(ERROR_FIELD).get(MESSAGE_FIELD).toString())
            emptySet()
        } else {
            setOf(BitbucketRepository(response))
        }
    }

    override fun byCriteria(criteria: BitbucketSearchCriteria): Iterable<Repository> {
        val result = mutableSetOf<Repository>()
        var response = getResponse(BASE_URL.plus(criteria.apply()))
        result.addAll(extractRepositories(response))
        while (hasNext(response)) {
            response = getResponse(response.get(NEXT_PAGE_FIELD).toString())
            result.addAll(extractRepositories(response))
        }
        return result
    }

    private fun getResponse(url: String): JSONObject {
        // TODO authentication
        return Unirest.get(url)
            .header("Accept", "application/json")
            .asJson().body.`object`
    }

    private fun extractRepositories(responseObject: JSONObject): Iterable<Repository> {
        return if (isError(responseObject)) {
            logger.error(responseObject.getJSONObject(ERROR_FIELD).get(MESSAGE_FIELD).toString())
            emptySet()
        } else {
            responseObject.getJSONArray(VALUES_FIELD)
                .asSequence()
                .map { BitbucketRepository(JSONObject(it.toString())) }
                .toSet()
        }
    }

    private fun isError(responseObject: JSONObject) = !responseObject.isNull(ERROR_FIELD)

    private fun hasNext(responseObject: JSONObject) = !responseObject.isNull(NEXT_PAGE_FIELD)
}
