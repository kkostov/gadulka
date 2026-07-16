package eu.iamkonstantin.gadulkaplayer.di

import co.touchlab.kermit.LogWriter

actual fun getPlatformLogWriters(
    homeDirectoryPath: String,
    logFileName: String
): List<LogWriter> {
    return listOf()
}
