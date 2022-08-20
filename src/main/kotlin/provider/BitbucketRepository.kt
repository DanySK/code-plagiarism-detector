package provider

import org.json.JSONObject

private const val NAME_FIELD = "name"
private const val LINKS_FIELD = "links"
private const val CLONE_FIELD = "clone"
private const val HREF_FIELD = "href"

/**
 * A Bitbucket repository adapter.
 */
data class BitbucketRepository(private val repositoryInfo: JSONObject) : AbstractRepository() {

    override val name: String
        get() = repositoryInfo.get(NAME_FIELD).toString()

    override val contributors: Iterable<String>
        get() = TODO("Not yet implemented")

    override val cloneUrl: String
        get() {
            val cloneInfos = repositoryInfo.getJSONObject(LINKS_FIELD)
                .getJSONArray(CLONE_FIELD)
                .get(0) as JSONObject
            return cloneInfos.get(HREF_FIELD).toString()
        }
}
