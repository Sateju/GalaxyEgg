package lincete.galaxyegg.di

import lincete.galaxyegg.domain.usecase.GetEggBackgrounds
import lincete.galaxyegg.domain.usecase.impl.GetLocalEggBackgrounds
import org.koin.dsl.module

val useCaseModule = module {

    single<GetEggBackgrounds> { GetLocalEggBackgrounds() }
}