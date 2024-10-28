package eu.iamkonstantin.kotlin.gadulka

import eu.iamkonstantin.kotlin.gadulka.GadulkaAudioPlayer
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import java.net.URI

actual class GadulkaPlayer : GadulkaAudioPlayer {
    private var player: MediaPlayer? = null

    init {
        // Ensure JavaFX runtime is initialized
        JFXPanel()
    }

    actual override fun play(url: String) {
        player?.dispose()
        player = null
        
        Platform.runLater {
            try {
                val media = Media(URI(url).toString())
                player = MediaPlayer(media).apply {
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
}
