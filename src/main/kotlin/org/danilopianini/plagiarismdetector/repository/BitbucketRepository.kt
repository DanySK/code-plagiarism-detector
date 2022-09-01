package org.danilopianini.plagiarismdetector.repository

import org.json.JSONObject
import java.net.URL

private const val REPOSITORY_NAME_FIELD = "name"
private const val LINKS_FIELD = "links"
private const val CLONE_FIELD = "clone"
private const val HREF_FIELD = "href"
private const val OWNER_FIELD = "owner"
private const val OWNER_NAME_FIELD = "display_name"

/**
 * A Bitbucket repository.
 * @property repositoryInfo the [JSONObject] containing all repo's infos.
 */
data class BitbucketRepository(private val repositoryInfo: JSONObject) : AbstractRepository() {
    override val name: String
        get() = repositoryInfo.get(REPOSITORY_NAME_FIELD).toString()

    override val owner: String
        get() = repositoryInfo.getJSONObject(OWNER_FIELD).get(OWNER_NAME_FIELD).toString()

    override val cloneUrl: URL
        get() {
            val cloneInfos = repositoryInfo.getJSONObject(LINKS_FIELD)
                .getJSONArray(CLONE_FIELD)
                .get(0) as JSONObject
            return URL(cloneInfos.get(HREF_FIELD).toString())
        }

    override fun toString() = "Repository $name of $owner"
}
