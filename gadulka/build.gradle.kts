/*
 * Copyright 2026 Konstantin <hi@iamkonstantin.eu>.
 *  Use of this source code is governed by the BSD 3-Clause License that can be found in LICENSE file.
 */

import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import org.gradle.plugins.signing.SigningExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKmpLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.osdetector)
    alias(libs.plugins.dokka)
    alias(libs.plugins.cyclonedx.bom)
}

group = "eu.iamkonstantin.kotlin"
version = "0.0.603"

kotlin {
    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    android {
        namespace = "eu.iamkonstantin.kotlin.gadulka"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.compilations.getByName("main") {
            cinterops {
                create("GadulkaKeyValueObserving")
            }
        }
    }

    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }


    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            val fxSuffix = when (osdetector.classifier) {
                "linux-x86_64" -> "linux"
                "linux-aarch_64" -> "linux-aarch64"
                "windows-x86_64" -> "win"
                "windows-aarch_64" -> "win-aarch64"
                "osx-x86_64" -> "mac"
                "osx-aarch_64" -> "mac-aarch64"
                else -> throw IllegalStateException("Unknown OS: ${osdetector.classifier}")
            }
            compileOnly("org.openjfx:javafx-base:23:${fxSuffix}")
            compileOnly("org.openjfx:javafx-graphics:23:${fxSuffix}")
            compileOnly("org.openjfx:javafx-swing:23:${fxSuffix}")
            compileOnly("org.openjfx:javafx-media:23:${fxSuffix}")
        }

        jvmTest.dependencies {
            implementation(libs.kotlin.test)
        }

        wasmJsMain.dependencies {
            implementation(libs.kotlinx.browser)
        }

        androidMain.dependencies {
            implementation(libs.androix.media3.exploplayer)
            implementation(libs.androidcontextprovider)
        }
    }
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), "gadulka", version.toString())

    pom {
        name = "Gadulka"
        description = "Gadulka is cross-platform player library enabling playback of files for Kotlin apps"
        inceptionYear = "2024"
        url = "https://iamkonstantin.eu/blog/meet-gadulka-a-minimalistic-player-library-for-kotlin-multiplatform/"
        licenses {
            license {
                name = "BSD-3-Clause"
                url = "https://www.gnu.org/licenses/gpl-3.0.html#license-text"
            }
        }
        developers {
            developer {
                id.set("iamkonstantin")
                name.set("Konstantin")
            }
        }
        scm {
            url = "https://github.com/kkostov/gadulka"
        }
    }
}

configure<SigningExtension> {
    setRequired(provider { gradle.taskGraph.allTasks.any { it is PublishToMavenRepository } })
}

val generateDokkaModuleDocs = tasks.register<GenerateDokkaModuleDocs>("generateDokkaModuleDocs") {
    readme.set(layout.projectDirectory.file("../README.md"))
    moduleDocs.set(layout.buildDirectory.file("dokka/Module.md"))
}

dokka {
    moduleName.set("Gadulka")
    dokkaPublications.configureEach {
        offlineMode.set(true)
    }
    dokkaSourceSets.configureEach {
        includes.from(generateDokkaModuleDocs.flatMap { it.moduleDocs })
    }
}

tasks.register("dokkaHtml") {
    dependsOn("dokkaGeneratePublicationHtml")
}

/**
 * Turns the repository README into the Dokka module docs shown on the landing page:
 * drops the badges and title (Dokka supplies its own), rewrites relative repo links
 * to absolute GitHub URLs, and points the image at the copy served alongside the docs.
 */
abstract class GenerateDokkaModuleDocs : DefaultTask() {
    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val readme: RegularFileProperty

    @get:OutputFile
    abstract val moduleDocs: RegularFileProperty

    @TaskAction
    fun generate() {
        val readmeText = readme.get().asFile.readText()
        val heading = "# Gadulka"
        val headingIndex = readmeText.indexOf(heading)
        val body = if (headingIndex >= 0) readmeText.substring(headingIndex + heading.length) else readmeText

        val rewritten = body
            .replace("](CONTRIBUTING.md)", "](https://github.com/kkostov/gadulka/blob/main/CONTRIBUTING.md)")
            .replace("](LICENSE)", "](https://github.com/kkostov/gadulka/blob/main/LICENSE)")
            .replace("./images/kodee.jpg", "images/kodee.jpg")
            .trimStart('\n')

        val outputFile = moduleDocs.get().asFile
        outputFile.parentFile.mkdirs()
        outputFile.writeText("# Module Gadulka\n\n$rewritten")
    }
}
