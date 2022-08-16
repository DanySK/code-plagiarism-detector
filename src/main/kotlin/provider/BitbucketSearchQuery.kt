package provider

import com.mashape.unirest.http.Unirest
import org.json.JSONObject
import java.net.URL

private const val BASE_URL = "https://api.bitbucket.org/2.0/repositories"
private const val SIZE_FIELD = "size"
private const val ERROR_FIELD = "error"
private const val NEXT_PAGE_FIELD = "next"
private const val VALUES_FIELD = "values"
private const val PAGE_LENGTH = 10

/**
 * A class implementing a search query for Bitbucket repositories.
 */
class BitbucketSearchQuery : RepositorySearchQuery<String, BitbucketSearchCriteria> {
    override fun byLink(url: URL): Iterable<Repository> {
        val response = getResponse(BASE_URL.plus(url.path))
        return if (isError(response)) emptySet() else setOf(BitbucketRepository(response))
    }

    override fun byCriteria(criteria: BitbucketSearchCriteria): Iterable<Repository> {
        val result = mutableSetOf<Repository>()
        var response = getResponse(BASE_URL.plus(criteria.apply()))
        result.addAll(extractRepositories(response))
        val numOfMatchingRepos = response.get(SIZE_FIELD).toString().toInt()
        val numOfLeftPages = ((numOfMatchingRepos + PAGE_LENGTH - 1) / PAGE_LENGTH) - 1
        repeat(numOfLeftPages) {
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
            emptySet()
        } else {
            responseObject.getJSONArray(VALUES_FIELD)
                .asSequence()
                .map { BitbucketRepository(JSONObject(it.toString())) }
                .toSet()
        }
    }

    private fun isError(responseObject: JSONObject) = !responseObject.isNull(ERROR_FIELD)
}
