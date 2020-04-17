package lincete.galaxyegg

import android.app.Application
import lincete.galaxyegg.di.persistenceModule
import lincete.galaxyegg.di.useCaseModule
import lincete.galaxyegg.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GalaxyEggApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(viewModelModule, persistenceModule, useCaseModule)
            androidContext(this@GalaxyEggApplication)
        }
    }
}