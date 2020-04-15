package lincete.galaxyegg.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [EggEntity::class], version = 1, exportSchema = false)
abstract class EggDatabase : RoomDatabase() {

    abstract val eggDao: EggDao
}