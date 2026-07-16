package eu.iamkonstantin.gadulkaplayer.di

import co.touchlab.kermit.LogWriter

expect fun getPlatformLogWriters(homeDirectoryPath: String, logFileName: String): List<LogWriter>
