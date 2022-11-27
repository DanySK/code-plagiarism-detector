package org.danilopianini.plagiarismdetector.input

import org.danilopianini.plagiarismdetector.input.configuration.RunConfiguration

/**
 * An interface modeling a configuration manager, which creates and
 * returns a [RunConfiguration] accordingly to the given list of arguments.
 */
interface RunConfigurator : (List<String>) -> (RunConfiguration<*>)
