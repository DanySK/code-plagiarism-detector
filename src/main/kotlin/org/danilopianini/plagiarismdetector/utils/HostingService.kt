package org.danilopianini.plagiarismdetector.utils

/**
 * An interface modeling a hosting service.
 */
sealed interface HostingService {

    /**
     * The name of the hosting service.
     */
    val name: String

    /**
     * The hostname of the hosting service.
     */
    val host: String
}

/**
 * GitHub hosting service.
 */
data object GitHubRest : HostingService {
    override val name: String = "github-rest"
    override val host: String = "$name.com"
}

/**
 * GitHub hosting service.
 */
data object GitHubGraphQL : HostingService {
    override val name: String = "github"
    override val host: String = "$name.com"
}

/**
 * Bitbucket hosting service.
 */
data object BitBucket : HostingService {
    override val name: String = "bitbucket"
    override val host: String = "$name.org"
}
