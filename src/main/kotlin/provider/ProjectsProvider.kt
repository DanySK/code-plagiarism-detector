package provider

/**
 * A provider of projects.
 */
interface ProjectsProvider {

    /**
     * An iterable of repositories.
     */
    val repositories: Iterable<Repository>
}
