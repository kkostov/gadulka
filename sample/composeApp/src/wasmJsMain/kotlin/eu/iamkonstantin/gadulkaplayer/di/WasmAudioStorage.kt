package eu.iamkonstantin.gadulkaplayer.di

import eu.iamkonstantin.gadulkaplayer.compose.resources.Res
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.set
import org.w3c.dom.url.URL
import org.w3c.files.Blob


class WasmAudioStorage() : AudioStorage {

    override suspend fun prepareAudio(fileName: String?): String? = runCatching {
        val bytes: ByteArray = Res.readBytes("files/$fileName")
        val buffer = Uint8Array(bytes.size)
        bytes.forEachIndexed { i, b -> buffer[i] = b }
        val blob = createBlobFromBuffer(buffer, "audio/mpeg")

        URL.createObjectURL(blob)
    }.getOrNull()
}

@JsFun("(buffer, mimeType) => new Blob([buffer], { type: mimeType })")
private external fun createBlobFromBuffer(buffer: Uint8Array, mimeType: String): Blob
