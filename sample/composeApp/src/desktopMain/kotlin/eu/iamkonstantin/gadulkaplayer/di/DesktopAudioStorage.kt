package eu.iamkonstantin.gadulkaplayer.di

import co.touchlab.kermit.Logger
import eu.iamkonstantin.gadulkaplayer.compose.resources.Res

class DesktopAudioStorage(private val homeDirectory: String) : AudioStorage {

    override suspend fun prepareAudio(fileName: String?): String? {
        if (fileName == null) return null
        return try {
            val tempFile = java.io.File(homeDirectory, "temp_$fileName")
            Logger.d("Prepare audio '$fileName', tempFile: '$tempFile'")
            if (!tempFile.exists()) {
                Logger.d("Cached audio '$fileName', tempFile: '$tempFile'")
                val bytes = Res.readBytes("files/$fileName")
                tempFile.writeBytes(bytes)
            }

            tempFile.toURI().toString()
        } catch (e: Exception) {
            Logger.e("Could not cache audio '$fileName'", e)
            null
        }
    }
}
