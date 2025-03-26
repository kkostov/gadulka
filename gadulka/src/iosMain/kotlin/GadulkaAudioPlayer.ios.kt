@file:OptIn(ExperimentalForeignApi::class)

package eu.iamkonstantin.kotlin.gadulka

import kotlinx.cinterop.CValue
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.currentTime
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.Foundation.NSURL
import kotlinx.cinterop.useContents
import platform.AVFoundation.*
import platform.CoreGraphics.CGFloat
import platform.CoreMedia.CMTime
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSNotificationCenter
import platform.darwin.NSEC_PER_SEC
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.time.Duration
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.setActive
import platform.AVFoundation.AVPlayerItem
import platform.AVFoundation.AVPlayerItemDidPlayToEndTimeNotification
import platform.AVFoundation.AVPlayerTimeControlStatusPlaying
import platform.AVFoundation.addPeriodicTimeObserverForInterval
import platform.AVFoundation.currentItem
import platform.AVFoundation.currentTime
import platform.AVFoundation.duration
import platform.AVFoundation.isPlaybackLikelyToKeepUp
import platform.AVFoundation.pause
import platform.AVFoundation.play
import platform.AVFoundation.removeTimeObserver
import platform.AVFoundation.replaceCurrentItemWithPlayerItem
import platform.AVFoundation.seekToTime
import platform.AVFoundation.timeControlStatus
import platform.CoreMedia.CMTimeGetSeconds
import platform.CoreMedia.CMTimeMake
import platform.CoreMedia.CMTimeMakeWithSeconds
import platform.Foundation.NSOperationQueue
import kotlin.time.Duration.Companion.seconds


@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class GadulkaPlayer actual constructor() {
    private var player: AVPlayer? = null
    private var playerObserver: CupertinoAVPlayerObserver? = null

    actual fun play(url: String) {
        release()
        AVAudioSession.sharedInstance().setCategory(AVAudioSessionCategoryPlayback, null)
        val nsUrl = NSURL(string = url)
        player = AVPlayer.playerWithURL(nsUrl)
        setup()
        player?.play()
    }

    actual fun play() {
        // https://developer.apple.com/documentation/avfoundation/avplayer/play()
        player?.play()
    }


    actual fun release() {
        playerObserver?.detach()
        player?.pause()
        player = null
        _state = null
    }

    actual fun stop() {
        player?.pause()
        player?.replaceCurrentItemWithPlayerItem(null)
    }

    actual fun pause() {
        player?.pause()
    }

    actual fun currentPosition(): Long? {
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

    actual fun currentDuration(): Long? {
        player?.currentItem?.let {
            val dur = it.duration.useContents { this.value.seconds }
            return dur.inWholeMilliseconds
        }

        return null
    }

    actual fun currentVolume(): Float? {
        // https://developer.apple.com/documentation/avfoundation/avplayer/volume
        return player?.volume
    }

    actual fun setVolume(volume: Float) {
        player?.volume = volume
    }

    actual fun setRate(rate: Float) {
        // https://developer.apple.com/documentation/avfoundation/controlling-the-transport-behavior-of-a-player#Control-the-playback-rate
        player?.rate = rate
    }


    actual fun seekTo(time: Long) {
        // https://developer.apple.com/documentation/avfoundation/avplayer/seek(to:)-87h2r
        val duration = CMTimeMake(time, 1000)
        player?.seekToTime(duration)
    }

    actual fun currentPlayerState(): GadulkaPlayerState? {
        return _state
    }

    private var _state: GadulkaPlayerState? = null



    private fun setup() {
        val observer = CupertinoAVPlayerObserver(player)
        observer.attach {
            val rate: Float = player?.rate ?: 0f
            val hasItem = player?.currentItem != null
            val avStatus = player?.currentItem?.status
            val bufferingEnding = player?.currentItem?.isPlaybackLikelyToKeepUp() == true
            val bufferIsEmpty = player?.currentItem?.isPlaybackBufferEmpty() == true
            // https://developer.apple.com/documentation/avfoundation/avplayer/timecontrolstatus-swift.property/#Discussion
            val waitingToPlayAtRate = player?.timeControlStatus == AVPlayerTimeControlStatusWaitingToPlayAtSpecifiedRate
            _state = when {
                rate > 0 -> GadulkaPlayerState.PLAYING
                !hasItem -> GadulkaPlayerState.IDLE
                avStatus == AVPlayerItemStatusFailed -> GadulkaPlayerState.IDLE
                !bufferingEnding || bufferIsEmpty || waitingToPlayAtRate -> GadulkaPlayerState.BUFFERING
                else -> GadulkaPlayerState.PAUSED
            }
        }
    }
}

class CupertinoAVPlayerObserver(private val player: AVPlayer?) {
    // based on https://developer.apple.com/documentation/avfoundation/monitoring-playback-progress-in-your-app
    private lateinit var timeObserver: Any

    fun attach(onAVPlayerUpdated: () -> Unit) {
        detach()
        if (player == null) return
        val interval = CMTimeMakeWithSeconds(0.5, NSEC_PER_SEC.toInt()) // update every ~0.5 seconds
        timeObserver = player.addPeriodicTimeObserverForInterval(interval, null) { _: CValue<CMTime> ->
            onAVPlayerUpdated()
        }
    }

    fun detach() {
        if (::timeObserver.isInitialized) player?.removeTimeObserver(timeObserver)
    }
}