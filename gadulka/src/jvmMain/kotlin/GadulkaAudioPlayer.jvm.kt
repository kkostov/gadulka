package eu.iamkonstantin.kotlin.gadulka

import eu.iamkonstantin.kotlin.gadulka.GadulkaAudioPlayer
import javafx.application.Platform
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import java.net.URI

actual class GadulkaPlayer : GadulkaAudioPlayer {
    private var player: MediaPlayer? = null

    actual override fun play(url: String) {
        Platform.startup {
            val media = Media(URI(url).toString())
            player = MediaPlayer(media).apply {
                play()
            }
        }
    }
}
