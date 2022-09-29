package org.danilopianini.plagiarismdetector.input

import org.danilopianini.plagiarismdetector.commons.BitBucket
import org.danilopianini.plagiarismdetector.commons.GitHub
import org.danilopianini.plagiarismdetector.commons.HostingService
import org.danilopianini.plagiarismdetector.commons.Java
import org.danilopianini.plagiarismdetector.commons.Language

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
}
