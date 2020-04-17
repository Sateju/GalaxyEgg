package lincete.galaxyegg.di

import lincete.galaxyegg.domain.usecase.GetBannerAlert
import lincete.galaxyegg.domain.usecase.GetEggImages
import lincete.galaxyegg.domain.usecase.impl.GetBannerAlertImpl
import lincete.galaxyegg.domain.usecase.impl.GetLocalEggImages
import org.koin.dsl.module

val useCaseModule = module {

    single<GetEggImages> { GetLocalEggImages() }
    single<GetBannerAlert> { GetBannerAlertImpl() }
}