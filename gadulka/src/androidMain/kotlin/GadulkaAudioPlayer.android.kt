package eu.iamkonstantin.kotlin.gadulka

import android.media.MediaPlayer

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

//
//actual class AudioPlayer(private val context: Context) {
//
//    private val mediaPlayer = ExoPlayer.Builder(context).build()
//    private val mediaItems = soundResList.map {
//        MediaItem.fromUri(Res.getUri(it))
//    }
//
//    init {
//        mediaPlayer.prepare()
//    }
//
//    @OptIn(ExperimentalResourceApi::class)
//    actual fun playSound(id: Int) {
//        mediaPlayer.setMediaItem(mediaItems[id])
//        mediaPlayer.play()
//    }
//
//    actual fun release() {
//        mediaPlayer.release()
//    }
//}
//
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

    actual fun release() {
        mediaPlayer.release()
    }

    actual fun stop() {
        mediaPlayer.pause()
    }
}