package org.danilopianini.plagiarismdetector.analyzer.technique.tokenization

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.builtins.SetSerializer
import org.danilopianini.plagiarismdetector.analyzer.representation.token.LanguageTokenTypes
import org.danilopianini.plagiarismdetector.analyzer.representation.token.LanguageTokenTypesImpl
import org.danilopianini.plagiarismdetector.analyzer.representation.token.TokenTypeImpl

/**
 * A supplier of [LanguageTokenTypes] for Java programming language.
 * @property configurationFileName the name of the configuration file from which load token types.
 */
class FileTokenTypesSupplier(private val configurationFileName: String) : TokenTypesSupplier {
    private val configurationFile = ClassLoader.getSystemResourceAsStream(configurationFileName) ?: error {
        "Configuration file $configurationFileName not found."
    }

    override val types: LanguageTokenTypes
        get() {
            val tokenTypes = Yaml.default.decodeFromStream(SetSerializer(TokenTypeImpl.serializer()), configurationFile)
            return LanguageTokenTypesImpl(tokenTypes)
        }
}
