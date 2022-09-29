package org.danilopianini.plagiarismdetector.input

import org.danilopianini.plagiarismdetector.core.session.AntiPlagiarismSession

/**
 * An interface modeling a configuration manager.
 */
interface RunConfigurator {

    /**
     * Returns a new [AntiPlagiarismSession] configured accordingly
     * to the given list of [arguments].
     */
    fun sessionFrom(arguments: List<String>): AntiPlagiarismSession
}
