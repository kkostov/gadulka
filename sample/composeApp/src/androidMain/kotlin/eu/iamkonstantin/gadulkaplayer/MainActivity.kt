package eu.iamkonstantin.gadulkaplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import eu.iamkonstantin.gadulkaplayer.di.platformModule
import eu.iamkonstantin.gadulkaplayer.di.sharedModule
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Logger.setTag("GADULKA")
        Logger.setMinSeverity(Severity.Debug)

        startKoin {
            androidContext(this@MainActivity)
            workManagerFactory()
            modules(sharedModule, platformModule)
        }

        getKoin().declare<android.app.Activity>(this)

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
