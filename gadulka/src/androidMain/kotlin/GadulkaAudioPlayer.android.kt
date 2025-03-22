package eu.iamkonstantin.kotlin.gadulka

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class GadulkaPlayer(private val context: Context) {
    private val mediaPlayer = ExoPlayer.Builder(context).build()

    init {
        mediaPlayer.prepare()
    }

    actual fun play(url: String) {
        if (mediaPlayer.isPlaying) mediaPlayer.pause()
        val mediaItem = MediaItem.fromUri(url)
        mediaPlayer.setMediaItem(mediaItem)
        mediaPlayer.play()
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
            return mediaPlayer.duration
        }
        return null
    }

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
        mediaPlayer.pause()
        mediaPlayer.release()
    }

    actual fun stop() {
        mediaPlayer.pause()
    }
}