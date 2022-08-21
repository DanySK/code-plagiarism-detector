package provider

import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.DirectoryFileFilter
import org.apache.commons.io.filefilter.SuffixFileFilter
import org.eclipse.jgit.api.Git
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.net.URL
import java.nio.file.Files

private const val LANGUAGES_EXT_FILE_NAME = "Programming_Languages_Extensions.json"
private const val LANGUAGE_NAME_FIELD = "name"
private const val EXTENSIONS_FIELD = "extensions"

/**
 * Abstract base implementation for repositories.
 */
abstract class AbstractRepository : Repository {

    /**
     * @property cloneUrl the [URL] to clone the repo.
     */
    protected abstract val cloneUrl: URL

    override fun getSources(language: String): Iterable<InputStream> {
        val extensions = getExtensionsOfLanguage(language)
        if (!extensions.any()) {
            throw java.lang.IllegalArgumentException("Not recognized language $language.")
        }
        val repoDir = cloneRepo(cloneUrl.toString())
        val sources = listSources(repoDir, extensions).map { FileInputStream(it) }
        repoDir.deleteRecursively()
        return sources
    }

    private fun getExtensionsOfLanguage(language: String): Iterable<String> {
        val fileStream = ClassLoader.getSystemResourceAsStream(LANGUAGES_EXT_FILE_NAME)!!
        JSONArray(JSONTokener(fileStream)).forEach {
            val obj = JSONObject(it.toString())
            if (obj.get(LANGUAGE_NAME_FIELD).toString().equals(language, ignoreCase = true)) {
                return obj.getJSONArray(EXTENSIONS_FIELD).map { e -> e.toString() }
            }
        }
        return emptySet()
    }

    private fun cloneRepo(url: String): File {
        val tmpDir = Files.createTempDirectory(this.name).toFile()
        Git.cloneRepository()
            .setURI(url)
            .setDirectory(tmpDir)
            .call()
        return tmpDir
    }

    private fun listSources(from: File, targetExtensions: Iterable<String>): Collection<File> {
        return FileUtils.listFiles(
            from,
            SuffixFileFilter(targetExtensions.toList()),
            DirectoryFileFilter.DIRECTORY
        )
    }
}
