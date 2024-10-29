package eu.iamkonstantin.kotlin.gadulka

import platform.AVFoundation.*
import platform.Foundation.NSURL

actual class GadulkaPlayer {
    private var player: AVPlayer? = null

    actual fun play(url: String) {
        release()

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