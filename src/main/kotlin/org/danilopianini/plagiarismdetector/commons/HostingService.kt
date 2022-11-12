package org.danilopianini.plagiarismdetector.commons

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
object GitHub : HostingService {
    override val name: String = "github"
    override val host: String = "$name.com"
}

/**
 * Bitbucket hosting service.
 */
object BitBucket : HostingService {
    override val name: String = "bitbucket"
    override val host: String = "$name.org"
}
