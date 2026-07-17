package eu.iamkonstantin.gadulkaplayer

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import eu.iamkonstantin.gadulkaplayer.di.getPlatformLogWriters
import eu.iamkonstantin.gadulkaplayer.di.platformModule
import eu.iamkonstantin.gadulkaplayer.di.sharedModule
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named

fun main() = application {

    val koinApp = startKoin {
        modules(sharedModule, platformModule)
    }

    val homeDirectoryPath = koinApp.koin.get<String>(named("homeDirectory"))
    val writers = getPlatformLogWriters(homeDirectoryPath, "ShipermansFriend.log")
    Logger.setLogWriters(writers)
    Logger.setTag("GADULKA")
    Logger.setMinSeverity(Severity.Debug)

    Window(
        onCloseRequest = ::exitApplication,
        title = "Gadulka Player",
    ) {
        App()
    }
}
