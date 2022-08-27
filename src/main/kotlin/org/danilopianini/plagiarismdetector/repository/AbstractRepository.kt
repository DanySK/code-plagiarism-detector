package org.danilopianini.plagiarismdetector.repository

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import org.danilopianini.plagiarismdetector.repository.content.RepoContentSupplierCloneStrategy
import java.io.File
import java.net.URL

/**
 * Abstract base implementation for repositories.
 */
abstract class AbstractRepository : Repository {
    companion object {
        private const val LANGUAGES_EXT_FILE_NAME = "Programming_Languages_Extensions.json"
        private const val LANGUAGE_NAME_FIELD = "name"
        private const val EXTENSIONS_FIELD = "extensions"
    }

    /**
     * The [URL] to clone the repo.
     */
    protected abstract val cloneUrl: URL

    override fun getSources(language: String): Iterable<File> {
        val extensions = getExtensionsOfLanguage(language)
        require(extensions.any()) { "Not recognized language $language." }
        return RepoContentSupplierCloneStrategy(cloneUrl).getFilesOf(extensions)
    }

    private fun getExtensionsOfLanguage(language: String): Iterable<String> {
        val fileStream = ClassLoader.getSystemResourceAsStream(LANGUAGES_EXT_FILE_NAME)!!
        JSONArray(JSONTokener(fileStream)).forEach {
            val obj = JSONObject("$it")
            if (obj.get(LANGUAGE_NAME_FIELD).toString().equals(language, ignoreCase = true)) {
                return obj.getJSONArray(EXTENSIONS_FIELD).map { e -> "$e" }
            }
        }
        return emptySet()
    }
}
