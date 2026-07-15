package eu.iamkonstantin.gadulkaplayer.di

import eu.iamkonstantin.gadulkaplayer.compose.resources.Res

class DesktopAudioStorage(private val homeDirectory: String) : AudioStorage {

    override suspend fun prepareAudio(fileName: String): String? = runCatching {
        val bytes = Res.readBytes("files/$fileName")
        val tempFile = java.io.File(homeDirectory, "temp_$fileName")
        if (!tempFile.exists()) {
            tempFile.writeBytes(bytes)
        }

        tempFile.toURI().toString()
    }.getOrNull()
}
