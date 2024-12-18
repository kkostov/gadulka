import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.osdetector)
}

group = "eu.iamkonstantin.kotlin"
version = "1.0.5"

kotlin {
    jvm()
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                //put your multiplatform dependencies here
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
        val jvmMain by getting {
            dependencies {
                val fxSuffix = when (osdetector.classifier) {
                    "linux-x86_64" -> "linux"
                    "linux-aarch_64" -> "linux-aarch64"
                    "windows-x86_64" -> "win"
                    "osx-x86_64" -> "mac"
                    "osx-aarch_64" -> "mac-aarch64"
                    else -> throw IllegalStateException("Unknown OS: ${osdetector.classifier}")
                }
                implementation("org.openjfx:javafx-base:19:${fxSuffix}")
                implementation("org.openjfx:javafx-graphics:19:${fxSuffix}")
                implementation("org.openjfx:javafx-controls:19:${fxSuffix}")
                implementation("org.openjfx:javafx-swing:19:${fxSuffix}")
                implementation("org.openjfx:javafx-web:19:${fxSuffix}")
                implementation("org.openjfx:javafx-media:19:${fxSuffix}")
            }
        }

        androidMain.dependencies {
            implementation(libs.androix.media3.exploplayer)
        }
    }
}

android {
    namespace = "eu.iamkonstantin.kotlin.gadulka"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

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
