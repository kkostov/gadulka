package eu.iamkonstantin.gadulkaplayer.di

import eu.iamkonstantin.kotlin.gadulka.GadulkaPlayer
import org.koin.core.module.Module
import org.koin.dsl.module

actual val homeDirectory: String
    get() = ""

actual val platformModule: Module
    get() = module {

        single<GadulkaPlayer> { GadulkaPlayer() }

        single<AudioStorage> { IosAudioStorage() }
    }
