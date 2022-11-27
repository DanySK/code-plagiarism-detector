package org.danilopianini.plagiarismdetector.input

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.danilopianini.plagiarismdetector.utils.BitBucket
import org.danilopianini.plagiarismdetector.utils.GitHub
import java.net.URL

class SupportedOptionsTest : FunSpec({

    test("Searching unsupported service should throw exception") {
        shouldThrow<java.lang.IllegalArgumentException> {
            SupportedOptions.serviceBy("non-existing-service")
            SupportedOptions.serviceBy(URL("https://non-existing-service.org"))
        }
    }

    test("Test get service by its name") {
        SupportedOptions.serviceBy("github") shouldBe GitHub
        SupportedOptions.serviceBy("bitbucket") shouldBe BitBucket
    }

    test("Test get service by url") {
        SupportedOptions.serviceBy(URL("https://github.com/orgs/unibo-oop-projects")) shouldBe GitHub
        SupportedOptions.serviceBy(URL("https://bitbucket.org/danysk/")) shouldBe BitBucket
    }
})
