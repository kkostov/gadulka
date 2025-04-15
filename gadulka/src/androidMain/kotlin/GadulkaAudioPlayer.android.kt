/*
 * Copyright 2025 Konstantin <hi@iamkonstantin.eu>.
 *  Use of this source code is governed by the BSD 3-Clause License that can be found in LICENSE file.
 */

package eu.iamkonstantin.kotlin.gadulka

import android.content.ContentResolver
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.kdroid.androidcontextprovider.ContextProvider

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class GadulkaPlayer actual constructor() {

    private var mediaPlayer = ExoPlayer.Builder(ContextProvider.getContext()).build()

    actual fun play(url: String) {
        if (mediaPlayer.isPlaying) {
            stop()
        }

        if (mediaPlayer.isCommandAvailable(Player.COMMAND_PREPARE)) mediaPlayer.prepare()

        val mediaItem = MediaItem.fromUri(url)

        if (mediaPlayer.isCommandAvailable(Player.COMMAND_SET_MEDIA_ITEM)) mediaPlayer.setMediaItem(mediaItem)

        if(mediaPlayer.isCommandAvailable(Player.COMMAND_PLAY_PAUSE)) mediaPlayer.play()
    }

    actual fun play() {
        if(mediaPlayer.isCommandAvailable(Player.COMMAND_PLAY_PAUSE)) mediaPlayer.play()
    }


    /**
     * Android-specific implementation of the [play] method which uses a ContentResolver to calculate the Uri of a raw file resource bundled with the app.
     */
    fun play(rawResourceId: Int) {
        val uri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .appendPath("$rawResourceId")
            .build().toString()
        play(uri)
    }

    actual fun currentPosition(): Long? {
        if (mediaPlayer.isCommandAvailable(Player.COMMAND_GET_CURRENT_MEDIA_ITEM)) {
            return mediaPlayer.currentPosition
        }
        return null
    }

    actual fun currentDuration(): Long? {
        if (mediaPlayer.isCommandAvailable(Player.COMMAND_GET_CURRENT_MEDIA_ITEM)) {
            if (mediaPlayer.duration >= 0) return mediaPlayer.duration
        }
        return null
    }

    @androidx.media3.common.util.UnstableApi
    actual fun currentPlayerState(): GadulkaPlayerState? {
        if (mediaPlayer.isReleased) {
            return null
        }
        // https://mofazhe.github.io/ExoPlayer-ffmpeg/listening-to-player-events.html
        val state = mediaPlayer.playbackState
        val playWhenReady = mediaPlayer.playWhenReady
        return when {
            state == Player.STATE_READY && playWhenReady -> GadulkaPlayerState.PLAYING
            state == Player.STATE_READY && !playWhenReady -> GadulkaPlayerState.PAUSED
            state == Player.STATE_BUFFERING -> GadulkaPlayerState.BUFFERING
            state == Player.STATE_IDLE -> GadulkaPlayerState.IDLE
            state == Player.STATE_ENDED -> GadulkaPlayerState.IDLE
            else -> GadulkaPlayerState.IDLE
        }
    }

    actual fun release() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    actual fun stop() {
        mediaPlayer.stop()
    }

    actual fun pause() {
        mediaPlayer.pause()
    }

    actual fun currentVolume(): Float? {
       if(mediaPlayer.isCommandAvailable(Player.COMMAND_GET_VOLUME)) {
           return mediaPlayer.volume
       }
       return null
    }

    actual fun setVolume(volume: Float) {
        if (!mediaPlayer.isCommandAvailable(Player.COMMAND_GET_VOLUME)) return
        mediaPlayer.volume = volume
    }

    actual fun setRate(rate: Float) {
        if (!mediaPlayer.isCommandAvailable(Player.COMMAND_SET_SPEED_AND_PITCH)) return
        mediaPlayer.playbackParameters = mediaPlayer.playbackParameters.withSpeed(rate)
    }

    actual fun seekTo(time: Long) {
        if (!mediaPlayer.isCommandAvailable(Player.COMMAND_SEEK_IN_CURRENT_MEDIA_ITEM)) return
        mediaPlayer.seekTo(time)
    }
}