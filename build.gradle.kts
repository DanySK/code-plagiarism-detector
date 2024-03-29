import org.gradle.api.tasks.testing.logging.TestLogEvent

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
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

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.stdlib)
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
    target {
        compilations.all {
            kotlinOptions {
                allWarningsAsErrors = true
                freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
            }
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
