package org.danilopianini.plagiarismdetector.input.cli.provider

import org.danilopianini.plagiarismdetector.repository.Repository

/**
 * An interface modeling commands used for configuring repository provider.
 */
interface ProviderCommands {

    /**
     * Returns a [Sequence] of [Repository] containing the ones
     * taken from the configured provider.
     */
    fun getRepositories(): Sequence<Repository>
}
