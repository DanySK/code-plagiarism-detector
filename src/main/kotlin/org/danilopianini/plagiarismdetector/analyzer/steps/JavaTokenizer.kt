package org.danilopianini.plagiarismdetector.analyzer.steps

import com.charleskorn.kaml.Yaml
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.Node
import com.github.javaparser.ast.visitor.TreeVisitor
import kotlinx.serialization.builtins.ListSerializer
import org.danilopianini.plagiarismdetector.analyzer.representation.token.languages.JavaTokenTypes
import org.danilopianini.plagiarismdetector.analyzer.representation.token.Token
import org.danilopianini.plagiarismdetector.analyzer.representation.token.TokenImpl
import org.danilopianini.plagiarismdetector.analyzer.representation.token.TokenTypeImpl

/**
 * A Java source file tokenizer.
 */
class JavaTokenizer : StepHandler<CompilationUnit, Sequence<Token>> {
    override fun process(input: CompilationUnit): Sequence<Token> {
        val tokenizer = TokenizerTreeVisitor()
        tokenizer.visitPreOrder(input)
        return tokenizer.tokens.asSequence()
    }

    /**
     * A token generator.
     */
    class TokenizerTreeVisitor : TreeVisitor() {
        companion object {
            private const val CONFIG_FILE_NAME = "java-token-types.yaml"
        }
        private val javaTokenTypes: JavaTokenTypes

        /**
         * List of tokens of this source tree.
         */
        val tokens = mutableListOf<Token>()

        init {
            val input = ClassLoader.getSystemResourceAsStream(CONFIG_FILE_NAME)!!
            val tokenTypes = Yaml.default.decodeFromStream(ListSerializer(TokenTypeImpl.serializer()), input)
            javaTokenTypes = JavaTokenTypes(tokenTypes.asSequence())
        }

        private fun tokenize(node: Node?) {
            require(node != null)
            val tokenTypeName = node::class.java.simpleName
            if (javaTokenTypes.isValidToken(tokenTypeName)) {
                tokens.add(
                    TokenImpl(
                        node.begin.get().line,
                        node.begin.get().column,
                        javaTokenTypes.getTokenTypeBy(tokenTypeName)
                    )
                )
            }
        }

        override fun process(node: Node?) {
            tokenize(node)
        }
    }
}
