package org.danilopianini.plagiarismdetector.core.analyzer.technique.tokenization

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.decodeFromStream
import kotlinx.serialization.builtins.SetSerializer
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.LanguageTokenTypes
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.LanguageTokenTypesImpl
import org.danilopianini.plagiarismdetector.core.analyzer.representation.token.TokenTypeImpl

/**
 * A supplier of [LanguageTokenTypes] for Java programming language.
 * @property configurationFileName the name of the configuration file from which load token types.
 */
class FileTokenTypesSupplier private constructor(private val configurationFileName: String) : TokenTypesSupplier {

    private val configurationFile = ClassLoader.getSystemResourceAsStream(configurationFileName) ?: error {
        "Configuration file $configurationFileName not found."
    }

    override val types: LanguageTokenTypes = LanguageTokenTypesImpl(
        Yaml.default.decodeFromStream(SetSerializer(TokenTypeImpl.serializer()), configurationFile),
    )

    companion object {
        private var cache = emptyMap<String, FileTokenTypesSupplier>()

        /**
         * Builds a new token supplier if needed, or fetches an existing one otherwise.
         */
        fun supplierFor(configurationFileName: String) = cache[configurationFileName]
            ?: FileTokenTypesSupplier(configurationFileName).also { cache += configurationFileName to it }
    }
}
