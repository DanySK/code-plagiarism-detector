package org.danilopianini.plagiarismdetector.input

import org.danilopianini.plagiarismdetector.utils.BitBucket
import org.danilopianini.plagiarismdetector.utils.GitHubGraphQL
import org.danilopianini.plagiarismdetector.utils.GitHubRest
import org.danilopianini.plagiarismdetector.utils.HostingService
import org.danilopianini.plagiarismdetector.utils.Java
import org.danilopianini.plagiarismdetector.utils.Language
import java.net.URL

/**
 * An object containing all supported options.
 */
internal object SupportedOptions {
    /**
     * Supported programming [Language]s.
     */
    val languages: Set<Language> = setOf(Java)

    /**
     * Supported [HostingService]s.
     */
    val services: List<HostingService> = listOf(GitHubGraphQL, GitHubRest, BitBucket)

    /**
     * Returns the [HostingService] with the given [name].
     */
    fun serviceBy(name: String): HostingService =
        requireNotNull(services.find { it.name == name }) { "$name not supported!" }

    /**
     * Returns the [HostingService] according to the given [url].
     */
    fun serviceBy(url: URL): HostingService =
        requireNotNull(services.find { it.host == url.host }) { "${url.host} not supported!" }
}
