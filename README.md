<div>
 <img src="https://img.shields.io/badge/Platform-Android-brightgreen.svg?logo=android" alt="Platform Android" />

<img src="https://img.shields.io/badge/Platform-iOS%20%2F%20macOS-lightgrey.svg?logo=apple" alt="Platform iOS and macOS" />

<img src="https://img.shields.io/badge/Platform-JVM-8A2BE2.svg?logo=openjdk" alt="Platform JVM" />

<img alt="Platform WASM" src="https://img.shields.io/badge/Platform-WASM-654EF1">


</div>

![Maven Central Version](https://img.shields.io/maven-central/v/eu.iamkonstantin.kotlin/gadulka)

[![Build](https://github.com/kkostov/gadulka/actions/workflows/gradle.yml/badge.svg)](https://github.com/kkostov/gadulka/actions/workflows/gradle.yml)

# Gadulka

![Kodee with a music icon](./images/kodee.jpg)

## What is it?

Gadulka is a minimalistic audio player library for Kotlin Multiplatform.

Gadulka wraps the native player functionality from each target in "headless" mode. That is, the library
does not provide any UI (this will be up to you). You can read more about my motivations [here](https://iamkonstantin.eu/blog/meet-gadulka-a-minimalistic-player-library-for-kotlin-multiplatform/).

## Getting Started

Gadulka is available from Maven Central at the following coordinates:

```
implementation("eu.iamkonstantin.kotlin:gadulka:1.7.0")
```

### Example

Instantiate the player and call play!

```kotlin
val player = GadulkaPlayer()
player.play(url = "...")
player.stop()
player.release()
```

### Jetpack Compose
Example using Jetpack Compose:

```kotlin
@Composable
fun AudioPlayer() {
    val player = rememberGadulkaState()
    Row {
        Button(
            onClick = {
                player.play(
                    "https://download.samplelib.com/wav/sample-12s.wav"
                )
            }) {
            Text("Play")
        }
        Button(
            onClick = {
                player.stop()
            }) {
            Text("Stop")
        }
    }
}
```


**Et voilà, enjoy the library and feel free to open an issue with any questions, thoughts or comments you may have!**

## Notes
If you would like to track the state of the player (e.g. in a composable), Gadulka comes with a simple convenience function which queries the player state every 300ms:

```kotlin
@Composable
fun AudioPlayer() {
    val gadulka = rememberGadulkaLiveState()

    Column {
        Text(gadulka.state.name)

        Text("Volume: ${gadulka.volume}")

        Text("Position: ${gadulka.position / 1000}s / ${gadulka.duration / 1000}s")

        Row {
            Button(
                onClick = {
                    player.play(
                        "https://download.samplelib.com/wav/sample-12s.wav"
                    )
                }) {
                Text("Play")
            }
            Button(
                onClick = {
                    player.stop()
                }) {
                Text("Stop")
            }
        }
    }
}
```

Additional methods to control volume, position and playback rate are [also available](https://gadulka.iamkonstantin.eu).


Gadulka links against but doesn't bundle JavaFX. You may need to adapt your gradle file to add the corresponding dependencies e.g.:

```kotlin
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
        implementation("org.openjfx:javafx-base:23:${fxSuffix}")
        implementation("org.openjfx:javafx-graphics:23:${fxSuffix}")
        implementation("org.openjfx:javafx-controls:23:${fxSuffix}")
        implementation("org.openjfx:javafx-swing:23:${fxSuffix}")
        implementation("org.openjfx:javafx-web:23:${fxSuffix}")
        implementation("org.openjfx:javafx-media:23:${fxSuffix}")
    }
}
```

📖 [Docs](https://gadulka.iamkonstantin.eu)

🍿 [Demo (WASM)](https://gadulka.iamkonstantin.eu/wasm)

## About the name

Gadulka [is a traditional Bulgarian](https://en.wikipedia.org/wiki/Gadulka) bowed string instrument. It's a tiny music
instrument which aligned well with the mindset of the library and why the name was chosen.

## Why on GitHub

While my personal projects use a self-hosted Forgejo or take advantage of the fantastic offering of Codeberg, the CI
pipeline for Gadulka requires a macOS build in order to test iOS compatibility. This is a very intensive (and expensive)
run for which GitHub makes more sense. Of course, this is something that can change in the future.

## License 📃

The library is licensed with the BSD-3-Clause license, more information in [LICENSE](LICENSE).

This is a permissive license which allows for any type of use, provided the copyright notice is included.

This library project was initially created
from [JetBrains's Library Template](https://github.com/Kotlin/multiplatform-library-template) under the Apache 2.0
license.


### Dependencies
To achieve its purpose, Gadulka relies on a number of libraries. A SBOM report is available in [JSON](https://gadulka.iamkonstantin.eu/sbom/bom.json) and [XML](https://gadulka.iamkonstantin.eu/sbom/bom.xml) for a complete list.   

Specific license terms:

* [Kotlin Native by JetBrains](https://kotlinlang.org/docs/native-binary-licenses.html)
* [JavaFX](https://github.com/openjdk/jfx/blob/master/LICENSE) linked under the class path exception allowing distribution as commercial and closed-source applications without additional copyleft obligations.