package org.danilopianini.plagiarismdetector.logger

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import org.danilopianini.plagiarismdetector.utils.LoggerConfiguratorImpl
import org.slf4j.LoggerFactory

class LoggerTest : FunSpec() {

    init {
        val logger = LoggerFactory.getLogger(this.javaClass) as Logger

        test("Messages should contain the logged message with the correct level") {
            val listAppender = ListAppender<ILoggingEvent>()
            listAppender.start()
            logger.addAppender(listAppender)
            logger.info(LOGGING_MSG)
            val logs = listAppender.list
            logs.shouldNotBeEmpty()
            logs.map { it.message }.first().shouldMatch(LOGGING_MSG)
            logs.map { it.level }.first().shouldBe(Level.INFO)
            listAppender.stop()
        }

        test("Testing LoggerConfigurator") {
            val listAppender = ListAppender<ILoggingEvent>()
            listAppender.start()
            logger.addAppender(listAppender)
            // simulating the default app root logger level is INFO
            val rootLogger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger
            rootLogger.level = Level.INFO
            logger.debug(LOGGING_MSG)
            listAppender.list.shouldBeEmpty()
            val args = LoggerConfiguratorImpl()(listOf("--verbose"))
            logger.debug(LOGGING_MSG)
            listAppender.list.shouldNotBeEmpty()
            args.shouldBeEmpty()
        }
    }

    companion object {
        private const val LOGGING_MSG = "This is a sample logging message"
    }
}
