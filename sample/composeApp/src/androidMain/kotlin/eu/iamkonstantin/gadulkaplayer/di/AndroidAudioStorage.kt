package eu.iamkonstantin.gadulkaplayer.di

import android.content.Context
import co.touchlab.kermit.Logger
import java.io.File
import java.io.FileNotFoundException

class AndroidAudioStorage(private val context: Context) : AudioStorage {

    override suspend fun prepareAudio(fileName: String?): String? {
        if (fileName == null) return null
        return try {
            val cleanFileName = fileName.lowercase()
            val tempFile = File(context.cacheDir, "temp_$cleanFileName")

            if (!tempFile.exists()) {
                val assetManager = context.assets

                // workaround a weird behaviour of the android build process to manipulkate filenames al gusto
                val fileNameWithExtension = fileName.substringBeforeLast('.')
                val extension = fileName.substringAfterLast('.')
                val lowercasePath = "composeResources/de.visualdigits.compose.resources/files/${fileNameWithExtension.lowercase()}.$extension"
                val uppercasePath = "composeResources/de.visualdigits.compose.resources/files/${fileNameWithExtension.uppercase()}.$extension"

                val inputStream = try {
                    Logger.d("Prepare audio file '$lowercasePath'")
                    assetManager.open(lowercasePath)
                } catch (_: FileNotFoundException) {
                    Logger.d("Prepare audio file '$uppercasePath'")
                    try {
                        assetManager.open(uppercasePath)
                    } catch (_: FileNotFoundException) {
                        Logger.e("Neither '$lowercasePath' or '$uppercasePath' exists")
                        null
                    }
                }
                inputStream?.use { ins ->
                    tempFile.outputStream().use { outs ->
                        ins.copyTo(outs)
                    }
                }
            }

            tempFile.absolutePath
        } catch (e: Exception) {
            Logger.e("Failed to prepare audio '$fileName': ${e.message}", e)
            null
        }
    }
}
