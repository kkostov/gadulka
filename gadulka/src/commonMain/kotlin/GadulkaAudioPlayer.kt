package eu.iamkonstantin.kotlin.gadulka

internal interface GadulkaAudioPlayer {
    fun play(url: String)
}

expect class GadulkaPlayer() : GadulkaAudioPlayer {
    override fun play(url: String)
}