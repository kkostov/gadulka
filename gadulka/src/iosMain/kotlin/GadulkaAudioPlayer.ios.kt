package eu.iamkonstantin.kotlin.gadulka

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.currentTime
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.Foundation.NSURL

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class GadulkaPlayer {
    private var player: AVPlayer? = null

    @OptIn(ExperimentalForeignApi::class)
    actual fun play(url: String) {
        release()
        AVAudioSession.sharedInstance().setCategory(AVAudioSessionCategoryPlayback, null)
        val nsUrl = NSURL(string = url)
        player = AVPlayer.playerWithURL(nsUrl)
        player?.play()
    }

    actual fun release() {
        player?.pause()
        player = null
    }

    actual fun stop() {
        player?.pause()
    }

    @OptIn(ExperimentalForeignApi::class)
    actual fun getCurrentPosition(): Long? {
        try {
            val currentTimeSeconds = player?.currentTime()?.useContents { this.value / this.timescale }
            if (currentTimeSeconds != null && currentTimeSeconds >= 0) {
                return (currentTimeSeconds * 1000)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

}