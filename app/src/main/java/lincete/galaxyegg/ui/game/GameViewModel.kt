package lincete.galaxyegg.ui.game

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lincete.galaxyegg.R
import lincete.galaxyegg.data.database.EggDao
import lincete.galaxyegg.data.database.EggEntity
import lincete.galaxyegg.ui.base.BaseViewModel

class GameViewModel(
        private val database: EggDao,
        application: Application) : BaseViewModel(application) {

    private val _egg = MutableLiveData<EggEntity>()
    private val egg: LiveData<EggEntity>
        get() = _egg

    private val _eggCount = MutableLiveData<Long>()
    private val eggCount: LiveData<Long>
        get() = _eggCount

    // The string version of the egg count
    val eggCountText = Transformations.map(eggCount) { eggCount ->
        eggCount.toString()
    }

    init {
        initializeEgg(application)
    }

    private fun initializeEgg(application: Application) {
        launch {
            val eggFromDatabase = getEggFromDatabase()
            if (eggFromDatabase != null) {
                _egg.value = eggFromDatabase
            } else {
                val newEgg = EggEntity(count =
                application.resources.getInteger(R.integer.countdown_initial_value).toLong())
                _egg.value = newEgg
                insert(newEgg)
            }
            _eggCount.value = _egg.value?.count
        }
    }

    private suspend fun getEggFromDatabase(): EggEntity? {
        return withContext(Dispatchers.IO) {
            database.getEgg()
        }
    }

    private suspend fun insert(egg: EggEntity) {
        return withContext(Dispatchers.IO) {
            database.insert(egg)
        }
    }

    private suspend fun update(egg: EggEntity) {
        return withContext(Dispatchers.IO) {
            database.update(egg)
        }
    }

    fun onVolumeChanged() {
        egg.value?.apply {
            count = count.minus(1)
            _eggCount.value = count
        }
        egg.value?.let { egg ->
            launch {
                update(egg)
            }
        }
    }

    fun onEggClicked() {
    }
}