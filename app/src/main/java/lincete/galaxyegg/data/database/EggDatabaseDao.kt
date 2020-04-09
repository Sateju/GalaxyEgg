package lincete.galaxyegg.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface EggDatabaseDao {

    /**
     * Insert a new row with a eggValue
     *
     * @param egg new value to write
     */
    @Insert
    fun insert(egg: EggEntity)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param egg new value to write
     */
    @Update
    fun update(egg: EggEntity)

    /**
     * Delete all values of the egg table
     */
    @Query("DELETE FROM egg_table")
    fun clear()

    /**
     * Selects and returns the only Egg value
     */
    @Query("SELECT * FROM egg_table ORDER BY eggId DESC LIMIT 1")
    fun getEggCount(): EggEntity?
}