package lincete.galaxyegg.ui.game

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.*
import lincete.galaxyegg.R
import lincete.galaxyegg.data.database.EggDatabaseDao
import lincete.galaxyegg.data.database.EggEntity
import lincete.galaxyegg.ui.base.BaseViewModel

class GameViewModel(
        private val database: EggDatabaseDao,
        application: Application) : BaseViewModel(application) {

    private val egg = MutableLiveData<EggEntity>()

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
                egg.value = getEggFromDatabase()
            } else {
                val newEgg = EggEntity(count =
                application.resources.getInteger(R.integer.countdown_initial_value).toLong())
                egg.value = newEgg
                insert(newEgg)
            }
            _eggCount.value = egg.value?.count
        }
    }

    private suspend fun getEggFromDatabase(): EggEntity? {
        return withContext(Dispatchers.IO) {
            database.getEggCount()
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
        _eggCount.value = _eggCount.value?.minus(1)
        TODO("remove, just for testing")
    }

    fun onEggClicked() {
        TODO("implement")
    }


    override fun onCleared() {
        super.onCleared()
        launch {
            TODO("implement")
            //val oldEgg = _egg.value ?: return@launch
            //update(oldEgg)
        }
    }
}