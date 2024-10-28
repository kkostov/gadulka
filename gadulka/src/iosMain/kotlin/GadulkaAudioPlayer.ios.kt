package eu.iamkonstantin.kotlin.gadulka

import platform.AVFoundation.*
import platform.Foundation.NSURL

actual class GadulkaPlayer : GadulkaAudioPlayer {
    private var player: AVPlayer? = null

    actual override fun play(url: String) {
        player?.pause()
        val nsUrl = NSURL(string = url)
        player = AVPlayer.playerWithURL(nsUrl)
        player?.play()
    }
}