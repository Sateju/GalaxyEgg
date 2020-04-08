package lincete.galaxyegg

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GalaxyEggApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@GalaxyEggApplication)
        }
    }
}