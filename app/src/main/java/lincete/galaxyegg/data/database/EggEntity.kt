package lincete.galaxyegg.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "egg_table")
data class EggEntity(
        @PrimaryKey(autoGenerate = true)
        var eggId: Long = 0L,

        @ColumnInfo(name = "egg_count")
        var count: Long
)