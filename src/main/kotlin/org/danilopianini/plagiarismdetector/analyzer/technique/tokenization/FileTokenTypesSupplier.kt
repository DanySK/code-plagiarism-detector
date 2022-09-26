package org.danilopianini.plagiarismdetector.analyzer.technique.tokenization

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.builtins.SetSerializer
import org.danilopianini.plagiarismdetector.analyzer.representation.token.LanguageTokenTypes
import org.danilopianini.plagiarismdetector.analyzer.representation.token.TokenTypeImpl
import org.danilopianini.plagiarismdetector.analyzer.representation.token.LanguageTokenTypesImpl

/**
 * A supplier of [LanguageTokenTypes] for Java programming language.
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
