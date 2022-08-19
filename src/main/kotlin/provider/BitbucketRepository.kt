package provider

import org.json.JSONObject

private const val NAME_FIELD = "name"

/**
 * A Bitbucket repository adapter.
 */
data class BitbucketRepository(private val repositoryInfo: JSONObject) : AbstractRepository() {
    override val name: String
        get() = repositoryInfo.get(NAME_FIELD).toString()

    override val contributors: Iterable<String>
        get() = TODO("Not yet implemented")

    override fun getCloneUrl(): String {
        val cloneInfos = repositoryInfo.getJSONObject("links")
            .getJSONArray("clone")
            .get(0) as JSONObject
        return cloneInfos.get("href").toString()
    }
}
