package eu.iamkonstantin.kotlin.gadulka

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.appendElement
import kotlinx.dom.appendText
import org.w3c.dom.Element
import org.w3c.dom.HTMLAudioElement
import org.w3c.dom.HTMLInputElement


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

    /**
     * Stop playback.
     *
     * Note: the player element remains in the DOM.
     */
    actual fun stop() {
        val playerEl = getPlayerElement()
        playerEl?.pause()
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

}
