package eu.iamkonstantin.kotlin.gadulka

interface GadulkaAudioPlayer {
    fun play(url: String)
}

expect class GadulkaPlayer : GadulkaAudioPlayer {
    override fun play(url: String)
}