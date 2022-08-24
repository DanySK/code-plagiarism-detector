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

    override val name: String
        get() = repository.coordinates().repo()

    override val owner: String
        get() = User.Smart(repository.github().users().get(repository.coordinates().user())).name()

    override val cloneUrl: URL
        get() {
            val urlRepresentation = StringBuilder().append(GITHUB_URL_PREFIX)
                .append(repository.coordinates().user())
                .append(URL_SEPARATOR)
                .append(repository.coordinates().repo())
                .toString()
            return URL(urlRepresentation)
        }
}
