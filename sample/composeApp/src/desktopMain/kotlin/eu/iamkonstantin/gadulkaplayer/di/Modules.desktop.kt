package eu.iamkonstantin.gadulkaplayer.di

import eu.iamkonstantin.kotlin.gadulka.GadulkaPlayer
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

actual val homeDirectory: String
    get() = File(System.getProperty("user.home"), ".gadulka").canonicalPath

actual val platformModule: Module
    get() = module {

        single<GadulkaPlayer> { GadulkaPlayer() }

        single<AudioStorage> { DesktopAudioStorage(get(named("homeDirectory"))) }

    }
