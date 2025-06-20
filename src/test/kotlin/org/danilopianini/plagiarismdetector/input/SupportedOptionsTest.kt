package org.danilopianini.plagiarismdetector.input

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.net.URI
import org.danilopianini.plagiarismdetector.utils.BitBucket
import org.danilopianini.plagiarismdetector.utils.GitHubGraphQL

class SupportedOptionsTest :
    FunSpec({

        test("Searching unsupported service should throw exception") {
            shouldThrow<java.lang.IllegalArgumentException> {
                SupportedOptions.serviceBy("non-existing-service")
                SupportedOptions.serviceBy(URI("https://non-existing-service.org").toURL())
            }
        }

        test("Test get service by its name") {
            SupportedOptions.serviceBy("github") shouldBe GitHubGraphQL
            SupportedOptions.serviceBy("bitbucket") shouldBe BitBucket
        }

        test("Test get service by url") {
            SupportedOptions.serviceBy(URI("https://github.com/orgs/unibo-oop-projects").toURL()) shouldBe GitHubGraphQL
            SupportedOptions.serviceBy(URI("https://bitbucket.org/danysk/").toURL()) shouldBe BitBucket
        }
    })
