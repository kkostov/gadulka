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
     * @return The current playback position in milliseconds, or null if the position cannot be determined.
     */
    fun getCurrentPosition(): Long?
}