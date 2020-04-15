package lincete.galaxyegg.di

import lincete.galaxyegg.ui.game.GameViewModelFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val viewModelModule = module {

    factory { GameViewModelFactory(get(), androidApplication()) }
}