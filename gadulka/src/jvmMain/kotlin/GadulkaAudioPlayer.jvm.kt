/*
 * Copyright 2025 Konstantin <hi@iamkonstantin.eu>.
 *  Use of this source code is governed by the BSD 3-Clause License that can be found in LICENSE file.
 */

package eu.iamkonstantin.kotlin.gadulka

import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.util.Duration
import java.net.URI

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class GadulkaPlayer actual constructor() {
    var playerState: MediaPlayer? = null
    private var lastVolume: Double? = null
    private var lastRate: Double? = null

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
                        println("Gadulka JVM: Player is ready")
                        play()
                    }
                    setOnEndOfMedia {
                        println("Gadulka JVM: End of media event")
                        Platform.runLater {
                            try {
                                this@GadulkaPlayer.playerState?.stop()
                                this@GadulkaPlayer.playerState?.seek(Duration.ZERO)
                            } catch (_: Exception) { }
                        }
                    }
                    setOnError {
                        println("Gadulka JVM: Error occurred: ${this.error?.message}")
                    }
                }
            } catch (e: Exception) {
                println("Gadulka JVM: Failed to play audio.")
                e.printStackTrace()
            }
        }
    }

    actual fun play() {
        Platform.runLater {
            // Fix seeking issues (when currentTime exceeds duration)
            val atEnd = try {
                val ct = playerState?.currentTime?.toMillis() ?: -1.0
                val dt = playerState?.media?.duration?.toMillis() ?: -1.0
                ct >= 0 && dt >= 0 && ct >= dt
            } catch (_: Exception) { false }

            if (currentPlayerState() == GadulkaPlayerState.IDLE || atEnd)
                playerState?.seek(Duration.ZERO)

            playerState?.play()
            lastVolume?.let { playerState?.volume = it }
            lastRate?.let {
                playerState?.rate = 1.0     // Workaround, see: https://stackoverflow.com/a/79324478
                playerState?.rate = it
            }
        }
    }

    actual fun release() {
        playerState?.pause()
        playerState?.stop()
        playerState = null
    }

    actual fun stop() {
        playerState?.stop()
    }

    actual fun pause() {
        playerState?.pause()
    }

    actual fun currentPosition(): Long? {
        return playerState?.currentTime?.toMillis()?.toLong()
    }

    actual fun currentDuration(): Long? {
        return playerState?.media?.duration?.toMillis()?.toLong()
    }

    actual fun currentPlayerState(): GadulkaPlayerState? {
        val status = playerState?.status
        if (status == null) {
            return null
        }
        return when (status) {
            MediaPlayer.Status.UNKNOWN -> null
            MediaPlayer.Status.READY -> GadulkaPlayerState.IDLE
            MediaPlayer.Status.PAUSED -> GadulkaPlayerState.PAUSED
            MediaPlayer.Status.PLAYING -> GadulkaPlayerState.PLAYING
            MediaPlayer.Status.STOPPED -> GadulkaPlayerState.IDLE
            MediaPlayer.Status.STALLED -> GadulkaPlayerState.BUFFERING
            MediaPlayer.Status.HALTED -> GadulkaPlayerState.IDLE
            MediaPlayer.Status.DISPOSED -> null
        }
    }

    actual fun currentVolume(): Float? {
        return playerState?.volume?.toFloat()
    }

    actual fun setVolume(volume: Float) {
        lastVolume = volume.toDouble()
        playerState?.volume = lastVolume!!
    }

    actual fun setRate(rate: Float) {
        lastRate = rate.toDouble()
        playerState?.rate = lastRate!!
    }

    actual fun seekTo(time: Long) {
        Platform.runLater {
            playerState?.seek(Duration.millis(time.toDouble()))
        }
    }
}
