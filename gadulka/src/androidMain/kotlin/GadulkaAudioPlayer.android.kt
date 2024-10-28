package eu.iamkonstantin.kotlin.gadulka

import android.media.MediaPlayer
import eu.iamkonstantin.kotlin.gadulka.GadulkaAudioPlayer

actual class GadulkaPlayer : GadulkaAudioPlayer {
    private val mediaPlayer = MediaPlayer()

    actual override fun play(url: String) {
        if (mediaPlayer.isPlaying) mediaPlayer.stop()
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            it.start()
        }
    }
}