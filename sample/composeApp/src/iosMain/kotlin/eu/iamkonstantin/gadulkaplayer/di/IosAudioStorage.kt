package eu.iamkonstantin.gadulkaplayer.di


class IosAudioStorage() : AudioStorage {

    override suspend fun prepareAudio(fileName: String): String? = runCatching {
        null
    }.getOrNull()
}
