package eu.iamkonstantin.gadulkaplayer.di

import co.touchlab.kermit.DefaultFormatter
import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.MessageStringFormatter
import co.touchlab.kermit.Severity
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

actual fun getPlatformLogWriters(homeDirectoryPath: String, logFileName: String): List<LogWriter> {
    val logDirectory = File(homeDirectoryPath)
    if (!logDirectory.exists()) {
        logDirectory.mkdirs()
    }

    return listOf(
        CustomJvmFileLogWriter(File(logDirectory, logFileName)),
        CustomJvmConsoleWriter()
    )
}

class CustomJvmConsoleWriter(private val messageStringFormatter: MessageStringFormatter = DefaultFormatter) : LogWriter() {

    override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
        val timestamp = LocalDateTime.now().format(formatter)
        val logLine = "$timestamp [${severity.name}] $tag: $message"

        if (severity == Severity.Error) {
            System.err.println(logLine)
        } else {
            println(logLine)
        }

        throwable?.let {
            val thString = it.stackTraceToString()
            if (severity == Severity.Error) {
                System.err.println(thString)
            } else {
                println(thString)
            }
        }
    }
}

class CustomJvmFileLogWriter(private val logFile: File) : LogWriter() {

    override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
        val timestamp = LocalDateTime.now().format(formatter)
        val logLine = "$timestamp [${severity.name}] $tag: $message\n"

        logFile.appendBytes(logLine.toByteArray(Charsets.UTF_8))

        throwable?.let {
            logFile.appendBytes("${it.stackTraceToString()}\n".toByteArray(Charsets.UTF_8))
        }
    }
}
