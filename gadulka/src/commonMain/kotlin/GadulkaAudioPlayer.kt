package eu.iamkonstantin.kotlin.gadulka

expect class GadulkaPlayer {
    fun play(url: String)

    fun stop()
    
    fun release()
}