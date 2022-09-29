package org.danilopianini.plagiarismdetector.repository

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeExactly
import org.danilopianini.plagiarismdetector.repository.content.RepoContentSupplierCloneStrategy
import java.net.URL

class RepositoryContentSupplierTest : FunSpec() {

    private val githubContentRepoSupplier = RepoContentSupplierCloneStrategy(URL(GH_SAMPLE_REPO_URL))
    private val bitbucketContentRepoSupplier = RepoContentSupplierCloneStrategy(URL(BB_SAMPLE_REPO_URL))

    init {
        test("Check if returns all sources") {
            val suffixFileJavaPattern = Regex(".*.java$")
            githubContentRepoSupplier.filesMatching(suffixFileJavaPattern).count() shouldBeExactly EXPECTED_SOURCES
            bitbucketContentRepoSupplier.filesMatching(suffixFileJavaPattern).count() shouldBeExactly EXPECTED_SOURCES
        }

        test("No sources meets the given pattern") {
            val suffixFileJavaPattern = Regex(".*.cs")
            githubContentRepoSupplier.filesMatching(suffixFileJavaPattern).count() shouldBeExactly 0
            bitbucketContentRepoSupplier.filesMatching(suffixFileJavaPattern).count() shouldBeExactly 0
        }
    }

    companion object {
        private const val GH_SAMPLE_REPO_URL = "https://github.com/tassiLuca/test-app-for-code-plagiarism-detector"
        private const val BB_SAMPLE_REPO_URL = "https://bitbucket.org/tassiLuca/test-app-for-code-plagiarism-detector"
        private const val EXPECTED_SOURCES = 8
    }
}
