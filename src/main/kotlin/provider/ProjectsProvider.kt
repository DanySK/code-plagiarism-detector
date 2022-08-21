package provider

import provider.repository.Repository

/**
 * A provider of projects.
 */
interface ProjectsProvider {

    /**
     * An [Iterable] of [Repository].
     */
    val repositories: Iterable<Repository>
}
