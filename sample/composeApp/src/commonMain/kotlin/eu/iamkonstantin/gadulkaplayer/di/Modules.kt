package eu.iamkonstantin.gadulkaplayer.di

import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

expect val platformModule: Module

expect val homeDirectory: String


val sharedModule = module {

    single(named("homeDirectory")) { homeDirectory }
}
