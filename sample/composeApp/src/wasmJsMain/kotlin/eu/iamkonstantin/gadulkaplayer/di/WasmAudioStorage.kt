package eu.iamkonstantin.gadulkaplayer.di


class WasmAudioStorage() : AudioStorage {

    override suspend fun prepareAudio(fileName: String): String? = runCatching {
        null
    }.getOrNull()
}
