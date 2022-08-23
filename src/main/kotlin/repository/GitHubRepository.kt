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
    }

    override val name: String
        get() = repository.coordinates().repo()

    override val owner: String
        get() = User.Smart(repository.github().users().get(repository.coordinates().user())).name()

    override val cloneUrl: URL
        get() = URL("$GITHUB_URL_PREFIX${repository.coordinates().user()}/${repository.coordinates().repo()}")
}
