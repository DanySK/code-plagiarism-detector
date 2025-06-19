package org.danilopianini.plagiarismdetector.repository

import java.net.URI
import java.net.URL
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.CredentialsProvider
import org.kohsuke.github.GHRepository

/**
 * A GitHub repository.
 */
data class GitHubRepository(override val owner: String, override val name: String) : AbstractRepository() {
    constructor(queryResult: GHRepository) : this(queryResult.ownerName, queryResult.name)

    override val cloneUrl: URL = URI("https://github.com/$owner/$name.git").toURL()

    /**
     * Validate the existence of the repository.
     */
    fun validate(credentials: CredentialsProvider? = null) {
        val validation =
            runCatching {
                Git
                    .lsRemoteRepository()
                    .setRemote(cloneUrl.toString())
                    .apply { credentials?.let { setCredentialsProvider(it) } }
                    .call()
            }
        if (validation.isFailure || validation.getOrThrow().isEmpty()) {
            throw IllegalStateException(
                "Repository $owner/$name does not exist or is not accessible.",
                validation.exceptionOrNull(),
            )
        }
    }
}
