package repository

import com.jcabi.github.Repo
import com.jcabi.github.User
import java.net.URL

/**
 * A GitHub repository adapter.
 * @property repository the [Repo] to be adapted.
 */
data class GitHubRepository(private val repository: Repo) : AbstractRepository() {
    companion object {
        private const val GITHUB_URL_PREFIX = "https://github.com/"
        private const val URL_SEPARATOR = "/"
    }
    private val ownerUsername = repository.coordinates().user()

    override val name: String = repository.coordinates().repo()

    override val owner: String = User.Smart(repository.github().users().get(ownerUsername)).name()

    override val cloneUrl: URL
        get() {
            val urlRepresentation = StringBuilder().append(GITHUB_URL_PREFIX)
                .append(ownerUsername)
                .append(URL_SEPARATOR)
                .append(name)
                .toString()
            return URL(urlRepresentation)
        }

    override fun toString() = "Repository $name of $owner"
}
