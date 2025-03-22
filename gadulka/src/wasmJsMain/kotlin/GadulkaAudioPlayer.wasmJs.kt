package eu.iamkonstantin.kotlin.gadulka

import kotlinx.browser.document
import kotlinx.dom.appendElement
import org.w3c.dom.HTMLAudioElement

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class GadulkaPlayer(val htmlId: String) {
    actual fun play(url: String) {
        release()
        document.body?.appendElement("audio") {
            this as HTMLAudioElement
            this.id = htmlId
            this.src = url
        }

        val playerEl = getPlayerElement()
        playerEl?.play()
    }

    actual fun play() {
        getPlayerElement()?.play()
    }

    actual fun stop() {
        getPlayerElement()?.pause()
        getPlayerElement()?.currentTime = 0.0
    }

    actual fun pause() {
        getPlayerElement()?.pause()
    }

    /**
     * Stops playback and removes the player element from the DOM.
     */
    actual fun release() {
        val playerEl = getPlayerElement()
        playerEl?.pause()
        playerEl?.remove()
    }

    private fun getPlayerElement(): HTMLAudioElement? {
        return document.getElementById(htmlId) as? HTMLAudioElement
    }

    actual fun currentPosition(): Long? {
        // https://www.w3schools.com/jsref/prop_audio_currenttime.asp
        val currentTimeSeconds =  getPlayerElement()?.currentTime?.toLong()
        if (currentTimeSeconds != null && currentTimeSeconds >= 0) {
            return currentTimeSeconds * 1000
        }
        return null
    }

    actual fun currentDuration(): Long? {
        // https://www.w3schools.com/jsref/prop_audio_duration.asp
        val currentTimeSeconds =  getPlayerElement()?.duration?.toLong()
        if (currentTimeSeconds != null && currentTimeSeconds >= 0) {
            return currentTimeSeconds * 1000
        }
        return null
    }

    actual fun currentPlayerState(): GadulkaPlayerState? {
        // todo: observe the player events
        // https://developer.mozilla.org/en-US/docs/Web/HTML/Element/audio#events
        return null
    }

    actual fun currentVolume(): Float? {
        // https://developer.mozilla.org/en-US/docs/Web/API/HTMLMediaElement/volume
        return getPlayerElement()?.volume?.toFloat()
    }


    actual fun setVolume(volume: Float) {
        // https://developer.mozilla.org/en-US/docs/Web/API/HTMLMediaElement/volume
        getPlayerElement()?.volume = volume.toDouble()
    }

    actual fun setRate(rate: Float) {
        // https://developer.mozilla.org/en-US/docs/Web/API/HTMLMediaElement/playbackRate
        // Web browsers may choose to mute playback if rate is outside the useful range
        // Acceptable values are 0.25 to 4.0
        getPlayerElement()?.playbackRate = rate.toDouble()
    }

    actual fun seekTo(time: Long) {
        // https://developer.mozilla.org/en-US/docs/Web/API/HTMLMediaElement/currentTime
        getPlayerElement()?.currentTime = time.toDouble()
    }
}
