package eu.iamkonstantin.kotlin.gadulka

/**
 * A minimalistic audio player
 *
 *
 * Example:
 *
 * ```kotlin
val player = GadulkaPlayer()
player.play(url = "...")
player.stop()
player.release()
```
 */
@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class GadulkaPlayer {
    /**
     * Start playback of the audio resource at the provided [url].
     *
     * ### Resource URI
     *
     * Can be a remote HTTP(s) url, or a `files` URI obtained via `Res.getUri("files/sample.mp3")`.
     *
     * Check the [JetBrains docs on how to store raw](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform-resources-usage.html#raw-files) files as part of multiplatform project resources.
     *
     * On Android, you can resolve the resource URI using something like:
     *
     * ```kotlin
     *  val uri = Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .appendPath("${R.raw.name_of_your_resource}")
                .build().toString()
     * ```
     *
     */
    fun play(url: String)

    /**
     * Stop playback.
     *
     * Note: If the player is currently not playing, this action has no effect.
     */
    fun stop()

    /**
     * Pause and attempts to perform cleanup in order to dispose of any player resources.
     *
     */
    fun release()


    /**
     * Retrieves the current playback position in milliseconds.
     *
     * @return The current playback position in milliseconds, or null if it cannot be determined.
     */
    fun currentPosition(): Long?

    /**
     * Retrieves the total duration of the playback item in milliseconds.
     *
     * @return The duration of the playing item in milliseconds, or null if it cannot be determined.
     */
    fun currentDuration(): Long?

    /**
     * Retrieves the current state of the player
     *
     * @return The current state like [PLAYING], [BUFFERING], [IDLE], [PAUASED].
     */
    fun currentPlayerState(): GadulkaPlayerState?

    /**
     * Retrieves the current volume level of the player.
     *
     * A value of 0.0 indicates silence. A value of 1.0 indicates full audio volume for the player instance.
     * @return The current volume as a floating-point value, or null if it cannot be determined.
     */
    fun currentVolume(): Float?
}


/**
 * Checks whether the player is currently in a playing or buffering state.
 *
 * @return `true` if the player is in the PLAYING or BUFFERING state, otherwise `false`.
 */
fun GadulkaPlayer.isPlaying(): Boolean = currentPlayerState() in listOf(GadulkaPlayerState.PLAYING, GadulkaPlayerState.BUFFERING)