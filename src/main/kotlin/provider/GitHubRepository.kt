package provider

import com.jcabi.github.Repo

/**
 * A GitHub repository adapter.
 * @property repository the [Repo] to be adapted
 */
data class GitHubRepository(private val repository: Repo) : AbstractRepository() {

    override val name: String
        get() = repository.coordinates().repo()

    override val contributors: Iterable<String>
        get() = repository.collaborators().iterate().map { it.login() }

    override fun getCloneUrl(): String {
        return "https://github.com/${repository.coordinates().user()}/${repository.coordinates().repo()}"
    }
}
