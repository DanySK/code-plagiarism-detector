package provider

import com.jcabi.github.Repo
import java.net.URL

private const val GH_URL_PREFIX = "https://github.com/"

/**
 * A GitHub repository adapter.
 * @property repository the [Repo] to be adapted
 */
data class GitHubRepository(private val repository: Repo) : AbstractRepository() {
    override val name: String
        get() = repository.coordinates().repo()

    override val contributors: Iterable<String>
        get() = repository.collaborators().iterate().map { it.login() }

    override val cloneUrl: URL
        get() = URL("$GH_URL_PREFIX${repository.coordinates().user()}/${repository.coordinates().repo()}")
}
