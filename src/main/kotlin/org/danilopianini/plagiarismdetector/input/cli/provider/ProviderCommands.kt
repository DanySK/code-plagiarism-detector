package org.danilopianini.plagiarismdetector.input.cli.provider

import org.danilopianini.plagiarismdetector.repository.Repository

/**
 * An interface modeling commands used for configuring repository provider.
 */
interface ProviderCommands {

    /**
     * A [Sequence] of [Repository] taken from the configured provider.
     */
    val repositories: Sequence<Repository>
}
