package org.danilopianini.plagiarismdetector.analyzer.technique.tokenization.java

import com.charleskorn.kaml.Yaml
import kotlinx.serialization.builtins.SetSerializer
import org.danilopianini.plagiarismdetector.analyzer.representation.token.LanguageTokenTypes
import org.danilopianini.plagiarismdetector.analyzer.representation.token.TokenTypeImpl
import org.danilopianini.plagiarismdetector.analyzer.representation.token.LanguageTokenTypesImpl
import org.danilopianini.plagiarismdetector.analyzer.technique.tokenization.LanguageTokenTypesSupplier

/**
 * A supplier of [LanguageTokenTypes] for Java programming language.
 */
class JavaTokenTypesSupplier : LanguageTokenTypesSupplier {
    companion object {
        private const val CONFIG_FILE_NAME = "java-token-types.yml"
    }

    override val types: LanguageTokenTypes
        get() {
            val configFile = ClassLoader.getSystemResourceAsStream(CONFIG_FILE_NAME) ?: error {
                "Configuration file $CONFIG_FILE_NAME not found."
            }
            val tokenTypes = Yaml.default.decodeFromStream(SetSerializer(TokenTypeImpl.serializer()), configFile)
            return LanguageTokenTypesImpl(tokenTypes)
        }
}
