package eu.iamkonstantin.gadulkaplayer

import android.os.Environment
import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.LogcatWriter
import co.touchlab.kermit.Severity
import java.io.File
import java.io.FileOutputStream

actual fun getPlatformLogWriters(homeDirectoryPath: String, logFileName: String): List<LogWriter> {
    val downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val logFile = File(downloadFolder, "logs.txt")

    // WICHTIG: Du brauchst WRITE_EXTERNAL_STORAGE Permissions in der AndroidManifest.xml
    // und musst diese zur Laufzeit anfragen, wenn du in den Download-Ordner willst.

    return listOf(
        LogcatWriter(), // Immer parallel Logcat behalten!
        FileLogWriter(logFile)
    )
}

class FileLogWriter(private val logFile: File) : LogWriter() {
    override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
        val logEntry = "[$severity] $tag: $message ${throwable?.stackTraceToString() ?: ""}\n"
        try {
            FileOutputStream(logFile, true).use { it.write(logEntry.toByteArray()) }
        } catch (e: Exception) {
            // Falls das Schreiben fehlschlägt, zumindest in Logcat ausgeben
        }
    }
}
