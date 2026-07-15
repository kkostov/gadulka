package eu.iamkonstantin.gadulkaplayer

import co.touchlab.kermit.LogWriter

expect fun getPlatformLogWriters(homeDirectoryPath: String, logFileName: String): List<LogWriter>
