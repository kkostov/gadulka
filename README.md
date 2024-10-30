![Maven Central Version](https://img.shields.io/maven-central/v/eu.iamkonstantin.kotlin/gadulka)

# Gadulka

## What is it?

Gadulka is a minimalistic player library for Kotlin Multiplatform. It targets Android, jvm and iOS, allowing consumers
to play audio files.

To achieve this, Gadulka wraps the native player functionality from each target in "headless" mode. That is, the library
does not provide any UI (this will be up to you).

## Getting Started

Gadulka is available from Maven Central at the following coordinates:

```
implementation("eu.iamkonstantin.kotlin:gadulka:1.1.0")
```

Instantiate the player and call play!

```kotlin
val player = GadulkaPlayer()
player.play(url = "...")
player.stop()
player.release()
```

Example using Jetpack Compose:

```kotlin
@Composable
fun AudioPlayer(player: GadulkaPlayer = GadulkaPlayer()) {
    val url = remember { mutableStateOf("https://download.samplelib.com/wav/sample-12s.wav") }

    Row {
        Button(
            onClick = {
                player.play(
                    url
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

For Android, the player needs a reference to the android context. For example, using Koin (or another DI library):

```kotlin
factory<GadulkaPlayer> {
    GadulkaPlayer(androidContext())
} onClose {
    it?.release()
}
```

**Et voilà, enjoy the library and feel free to open an issue with any questions, thoughts or comments you may have!**

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

This library project was initially created from [JetBrains's Library Template](https://github.com/Kotlin/multiplatform-library-template) under the Apache 2.0 license.
