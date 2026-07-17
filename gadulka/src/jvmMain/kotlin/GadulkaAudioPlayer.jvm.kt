/*
 * Copyright 2025 Konstantin <hi@iamkonstantin.eu>.
 *  Use of this source code is governed by the BSD 3-Clause License that can be found in LICENSE file.
 */

package eu.iamkonstantin.kotlin.gadulka

import co.touchlab.kermit.Logger
import javafx.application.Platform
import javafx.embed.swing.JFXPanel
import javafx.scene.media.Media
import javafx.scene.media.MediaException
import javafx.scene.media.MediaPlayer
import javafx.util.Duration
import java.net.URI

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class GadulkaPlayer actual constructor() {
    var playerState: MediaPlayer? = null
    private var lastVolume: Double? = null
    private var lastRate: Double? = null
    private var errorListener: ErrorListener? = null

    init {
        // Ensure JavaFX runtime is initialized
        JFXPanel()
    }

    actual fun play(url: String) {
        release()
        Platform.runLater {
            val resolved = materialiseToLocalUrl(url)
            val uriScheme = runCatching { URI(resolved).scheme }.getOrElse { "unparseable" }
            Logger.d("Gadulka JVM: play() scheme=$uriScheme")
            if (uriScheme == "file") {
                val exists = runCatching { java.io.File(URI(resolved)).exists() }.getOrElse { false }
                Logger.d("Gadulka JVM: file exists=$exists")
            }
            try {
                val media = Media(URI(resolved).toString()).apply {
                    setOnError {
                        Logger.e("Gadulka JVM: Media.onError", error)
                    }
                }
                playerState = MediaPlayer(media).apply {
                    setOnReady {
                        Logger.d("Gadulka JVM: Player is ready")
                        play()
                    }
                    setOnEndOfMedia {
                        Logger.d("Gadulka JVM: End of media event")
                        Platform.runLater {
                            try {
                                this@GadulkaPlayer.playerState?.stop()
                                this@GadulkaPlayer.playerState?.seek(Duration.ZERO)
                            } catch (_: Exception) { }
                        }
                    }
                    setOnError {
                        Logger.e("Gadulka JVM", error)
                        errorListener?.onError(error.stackTraceToString())
                    }
                }
            } catch (e: Exception) {
                Logger.e("Gadulka JVM: Failed to play audio.", e)
                errorListener?.onError(e.stackTraceToString())
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

    actual fun setOnErrorListener(listener: ErrorListener) {
        errorListener = listener
    }
}
