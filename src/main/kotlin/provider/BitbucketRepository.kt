package provider

import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.DirectoryFileFilter
import org.apache.commons.io.filefilter.SuffixFileFilter
import org.eclipse.jgit.api.Git
import org.json.JSONObject
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.nio.file.Files

private const val NAME_FIELD = "name"

/**
 * A Bitbucket repository adapter.
 */
data class BitbucketRepository(private val repositoryInfo: JSONObject) : AbstractRepository() {
    override val name: String
        get() = repositoryInfo.get(NAME_FIELD).toString()

    override val contributors: Iterable<String>
        get() = TODO("Not yet implemented")

    override fun extractSources(languageExtensions: Iterable<String>): Iterable<InputStream> {
        val cloneInfos = repositoryInfo.getJSONObject("links")
            .getJSONArray("clone")
            .get(0) as JSONObject
        val cloneUrl = cloneInfos.get("href").toString()
        val repoDir = cloneRepo(cloneUrl)
        val sources = listSources(repoDir, languageExtensions)
            .asSequence()
            .map { FileInputStream(it) }
            .toSet()
        repoDir.deleteRecursively()
        return sources
    }

    private fun cloneRepo(url: String): File {
        val tmpDir = Files.createTempDirectory(this.name).toFile()
        Git.cloneRepository()
            .setURI(url)
            .setDirectory(tmpDir)
            .call()
        return tmpDir
    }

    private fun listSources(from: File, admittedExtensions: Iterable<String>): Collection<File> {
        return FileUtils.listFiles(
            from,
            SuffixFileFilter(admittedExtensions.toList()),
            DirectoryFileFilter.DIRECTORY
        )
    }
}
