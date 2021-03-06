package lincete.galaxyegg.di

import lincete.galaxyegg.ui.game.GameViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        GameViewModel(database = get(),
                preferenceHelper = get(),
                eggImageUseCase = get(),
                application = androidApplication())
    }
}