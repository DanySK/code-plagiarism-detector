package org.danilopianini.plagiarismdetector.logger

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldMatch
import org.slf4j.LoggerFactory

class LoggerTest : FunSpec() {
    companion object {
        private const val LOGGING_MSG = "This is a sample logging message"
    }

    init {
        val logger = LoggerFactory.getLogger(this.javaClass) as Logger
        val listAppender = ListAppender<ILoggingEvent>()
        listAppender.start()
        logger.addAppender(listAppender)
        beforeSpec {
            logger.debug(LOGGING_MSG)
        }

        test("Messages should contain the logged message with the correct level") {
            val logs = listAppender.list
            logs.shouldNotBeEmpty()
            logs.map { it.message }.first().shouldMatch(LOGGING_MSG)
            logs.map { it.level }.first().shouldBe(Level.DEBUG)
        }
    }
}
