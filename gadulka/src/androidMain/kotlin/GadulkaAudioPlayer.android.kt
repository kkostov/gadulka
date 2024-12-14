package eu.iamkonstantin.kotlin.gadulka

import android.content.ContentResolver
import android.media.MediaPlayer

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

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

    actual fun release() {
        mediaPlayer.pause()
        mediaPlayer.release()
    }

    actual fun stop() {
        mediaPlayer.pause()
    }
}