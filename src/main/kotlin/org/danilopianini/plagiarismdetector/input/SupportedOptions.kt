package org.danilopianini.plagiarismdetector.input

import org.danilopianini.plagiarismdetector.commons.BitBucket
import org.danilopianini.plagiarismdetector.commons.GitHub
import org.danilopianini.plagiarismdetector.commons.HostingService
import org.danilopianini.plagiarismdetector.commons.Java
import org.danilopianini.plagiarismdetector.commons.Language
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
    val services: Set<HostingService> = setOf(GitHub, BitBucket)

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
