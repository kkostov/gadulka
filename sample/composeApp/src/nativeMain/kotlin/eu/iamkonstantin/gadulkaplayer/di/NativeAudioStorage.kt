package eu.iamkonstantin.gadulkaplayer.di


class NativeAudioStorage() : AudioStorage {

    override suspend fun prepareAudio(fileName: String?): String? = runCatching {
        null
    }.getOrNull()
}
