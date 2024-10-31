package eu.iamkonstantin.kotlin.gadulka

import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFoundation.*
import platform.Foundation.NSURL
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback

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
}