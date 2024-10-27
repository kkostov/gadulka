import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "eu.iamkonstantin.kotlin"
version = "1.0.0"

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
    linuxX64()

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
        url = "https://github.com/kkostov/gadulka/"
        licenses {
            license {
                name = "GPLv3"
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
            url = "https://github.com/kkostov/gadulka/"
        }
    }
}
