package provider

/**
 * A provider of projects.
 */
interface ProjectsProvider {

    /**
     * An [Iterable] of [Repository].
     */
    val repositories: Iterable<Repository>
}
