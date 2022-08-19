package provider

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.InputStream

private const val LANGUAGES_EXT_FILE_NAME = "Programming_Languages_Extensions.json"
private const val LANGUAGE_NAME_FIELD = "name"
private const val EXTENSIONS_FIELD = "extensions"

/**
 * Abstract repo impl.
 */
abstract class AbstractRepository : Repository {

    override fun getSources(language: String): Iterable<InputStream> {
        val extensions = getExtensionsOfLanguage(language)
        if (extensions.any()) {
            return extractSources(extensions)
        } else {
            throw java.lang.IllegalArgumentException("Not recognized language $language.")
        }
    }

    private fun getExtensionsOfLanguage(language: String): Iterable<String> {
        val fileStream = ClassLoader.getSystemResourceAsStream(LANGUAGES_EXT_FILE_NAME)!!
        JSONArray(JSONTokener(fileStream)).forEach {
            val obj = JSONObject(it.toString())
            if (obj.get(LANGUAGE_NAME_FIELD).toString().equals(language, ignoreCase = false)) {
                return obj.getJSONArray(EXTENSIONS_FIELD)
                    .map { e -> e.toString() }
                    .toSet()
            }
        }
        return emptySet()
    }

    /**
     * Extract sources by language extensions.
     * @param languageExtensions an [Iterable] of string representing file extensions of sources to extract.
     */
    abstract fun extractSources(languageExtensions: Iterable<String>): Iterable<InputStream>
}
