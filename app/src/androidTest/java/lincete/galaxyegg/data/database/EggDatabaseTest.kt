package lincete.galaxyegg.data.database

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import lincete.galaxyegg.R
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * @see EggDatabase
 */
@RunWith(AndroidJUnit4::class)
class EggDatabaseTest {

    private lateinit var eggDao: EggDatabaseDao
    private lateinit var database: EggDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(context, EggDatabase::class.java)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build()
        eggDao = database.eggDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetNight() {
        val egg = EggEntity()
        eggDao.insert(egg)
        val eggValues = eggDao.getEggCount()
        assertEquals(eggValues?.count, R.integer.countdown_initial_value.toLong())
    }
}