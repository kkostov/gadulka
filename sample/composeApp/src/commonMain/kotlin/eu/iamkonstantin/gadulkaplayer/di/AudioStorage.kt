package eu.iamkonstantin.gadulkaplayer.di

interface AudioStorage {
    suspend fun prepareAudio(fileName: String): String?
}
