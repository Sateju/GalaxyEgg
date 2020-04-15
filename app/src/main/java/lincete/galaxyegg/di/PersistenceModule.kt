package lincete.galaxyegg.di

import androidx.room.Room
import lincete.galaxyegg.data.database.EggDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val persistenceModule = module {

    single {
        Room.databaseBuilder(androidApplication(), EggDatabase::class.java,
                "galaxy_egg_database")
                .fallbackToDestructiveMigration()
                .build()
    }

    single { get<EggDatabase>().eggDao }
}