package eu.iamkonstantin.gadulkaplayer

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import eu.iamkonstantin.gadulkaplayer.di.platformModule
import eu.iamkonstantin.gadulkaplayer.di.sharedModule
import kotlinx.browser.document
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named

@OptIn(ExperimentalComposeUiApi::class)
fun main() {

    startKoin {
        modules(sharedModule, platformModule)
    }

    Logger.setTag("GADULKA")
    Logger.setMinSeverity(Severity.Debug)

    ComposeViewport(document.body!!) {
        App()
    }
}
