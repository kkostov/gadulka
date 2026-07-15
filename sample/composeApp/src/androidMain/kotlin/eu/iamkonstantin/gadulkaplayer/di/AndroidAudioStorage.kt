package eu.iamkonstantin.gadulkaplayer.di

import android.content.Context
import eu.iamkonstantin.gadulkaplayer.compose.resources.Res
import java.io.File
import kotlin.io.readBytes

class AndroidAudioStorage(private val context: Context) : AudioStorage {

    override suspend fun prepareAudio(fileName: String): String? = runCatching {
        val bytes = Res.readBytes("files/$fileName")
        val tempFile = File(context.cacheDir, "temp_$fileName")
        if (!tempFile.exists()) {
            tempFile.writeBytes(bytes)
        }

        tempFile.absolutePath
    }.getOrNull()
}
