package org.danilopianini.plagiarismdetector.utils

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory

/**
 * The application root logger configurator.
 */
typealias LoggerConfigurator = (List<String>) -> (List<String>)

/**
 * A simple [LoggerConfigurator] implementation.
 */
class LoggerConfiguratorImpl : LoggerConfigurator {
    /**
     * Configure the root logger accordingly to the given list of arguments [args].
     * It returns the list of arguments given in input in which logger configuration
     * parameters have been removed.
     */
    override fun invoke(args: List<String>): List<String> = args.toMutableList().apply {
        if (contains(VERBOSE_OPTION)) {
            val rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
            rootLogger.level = Level.DEBUG
            remove(VERBOSE_OPTION)
        }
    }

    private companion object {
        private const val VERBOSE_OPTION = "--verbose"
    }
}
