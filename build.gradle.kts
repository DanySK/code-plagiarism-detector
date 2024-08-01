import com.apollographql.apollo3.gradle.internal.ApolloDownloadSchemaTask
import com.apollographql.apollo3.gradle.internal.ApolloGenerateSourcesTask
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.apollo)
    alias(libs.plugins.dokka)
    alias(libs.plugins.gitSemVer)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.qa)
    alias(libs.plugins.publishOnCentral)
    alias(libs.plugins.multiJvmTesting)
    alias(libs.plugins.shadowJar)
    alias(libs.plugins.taskTree)
    alias(libs.plugins.serialization)
    application
}

group = "org.danilopianini"

application {
    mainClass.set("$group.plagiarismdetector.MainKt")
}

apollo {
    fun disableDownload() {
        tasks.withType<ApolloDownloadSchemaTask>().configureEach {
            enabled = false
        }
    }
    service("github") {
        val schema = file("src/main/resources/github.graphql")
        packageName.set("org.danilopianini.plagiarismdetector.graphql.github")
        schemaFiles.from(schema)
        if (schema.exists()) {
            disableDownload()
        }
        introspection {
            endpointUrl.set("https://api.github.com/graphql")
            schemaFile.set(schema)
            val token = System.getenv("GH_TOKEN") ?: System.getenv("GITHUB_TOKEN")
            if (token == null) {
                project.logger.warn(
                    "No token provided. Please set the GH_TOKEN or the GITHUB_TOKEN environment variable." +
                        "Schema download is disabled.",
                )
                disableDownload()
            }
            headers.set(
                mapOf(
                    "Authorization" to "Bearer $token",
                ),
            )
        }
    }
}

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    dependsOn(tasks.withType<ApolloDownloadSchemaTask>().filter { it.name.contains("FromIntrospection") })
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.stdlib)
    implementation(libs.apollo.runtime)
    implementation(libs.clikt)
    implementation(libs.github.api)
    implementation(libs.unirest)
    implementation(libs.json)
    implementation(libs.kaml)
    implementation(libs.org.eclipse.jgit)
    implementation(libs.commons.io)
    implementation(libs.commons.math)
    implementation(libs.javaparser.core)
    implementation(libs.slf4j.api)
    implementation(libs.logback.classic)
    implementation(libs.logback.core)
    implementation(libs.progressbar)
    testImplementation(libs.bundles.kotlin.testing)
    testImplementation(libs.mockk)
}

multiJvm {
    jvmVersionForCompilation.set(11)
}

kotlin {
    compilerOptions {
        allWarningsAsErrors = true
        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
    }
}

/*
 * Disable verification tasks for generated sources
 */
tasks.withType<SourceTask>().configureEach {
    if (this is VerificationTask) {
        dependsOn(tasks.withType<ApolloGenerateSourcesTask>())
        exclude {
            it.file.absolutePath.contains("generated", ignoreCase = true)
        }
    }
}

ktlint {
    filter {
        exclude {
            it.file.absolutePath.contains("generated")
        }
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        showCauses = true
        showStackTraces = true
        events(*TestLogEvent.values())
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    }
    jvmArgs(project.property("jvmTestArgs"))
}

signing {
    val signingKey: String? by project
    val signingPassword: String? by project
    useInMemoryPgpKeys(signingKey, signingPassword)
}

publishOnCentral {
    projectLongName.set("Code Plagiarism Detector")
    projectDescription.set("A tool for scanning existing projects in search of potential signs of plagiarism")
    repository("https://maven.pkg.github.com/danysk/${rootProject.name}".lowercase()) {
        user.set("DanySK")
        password.set(System.getenv("GITHUB_TOKEN"))
    }
    publishing {
        publications {
            withType<MavenPublication> {
                pom {
                    developers {
                        developer {
                            name.set("Danilo Pianini")
                            email.set("danilo.pianini@gmail.com")
                            url.set("http://www.danilopianini.org/")
                        }
                        developer {
                            name.set("Luca Tassinari")
                            email.set("luca.tassinari.2000@gmail.com")
                        }
                    }
                }
            }
        }
    }
}
