package eu.iamkonstantin.kotlin.gadulka

import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import java.net.URI

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class GadulkaPlayer {
    var playerState: MediaPlayer? = null

    init {
        // Ensure JavaFX runtime is initialized
        JFXPanel()
    }

    actual fun play(url: String) {
        release()

        Platform.runLater {
            try {
                val media = Media(URI(url).toString())
                playerState = MediaPlayer(media).apply {
                    setOnReady {
                        println("Ready, playing..")
                        play()
                    }
                    setOnEndOfMedia {
                        println("End of media")
                    }
                    setOnError {
                        println("Error occurred: ${this.error?.message}")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    actual fun release() {
        playerState?.stop()
        playerState = null
    }

    actual fun stop() {
        playerState?.stop()
    }

    actual fun getCurrentPosition(): Long? {
        return playerState?.currentTime?.toMillis()?.toLong()
    }
}
