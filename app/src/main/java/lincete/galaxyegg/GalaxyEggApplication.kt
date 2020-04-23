package lincete.galaxyegg

import androidx.multidex.MultiDexApplication
import com.google.android.gms.ads.MobileAds
import lincete.galaxyegg.di.persistenceModule
import lincete.galaxyegg.di.useCaseModule
import lincete.galaxyegg.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GalaxyEggApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(viewModelModule, persistenceModule, useCaseModule)
            androidContext(this@GalaxyEggApplication)
        }

        MobileAds.initialize(this)
    }
}