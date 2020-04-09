package lincete.galaxyegg.ui.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import lincete.galaxyegg.R
import lincete.galaxyegg.data.database.EggDatabaseDao
import lincete.galaxyegg.data.database.EggEntity

class GameViewModel(
        private val database: EggDatabaseDao,
        application: Application) : AndroidViewModel(application) {

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val egg = MutableLiveData<EggEntity>()

    // The string version of the egg count
    val eggCount = Transformations.map(egg) { egg ->
        egg.count.toString()
    }

    init {
        initializeEgg(application)
    }

    private fun initializeEgg(application: Application) {
        uiScope.launch {
            val eggFromDatabase = getEggFromDatabase()
            if (eggFromDatabase != null) {
                egg.value = getEggFromDatabase()
            } else {
                val newEgg = EggEntity(count =
                application.resources.getInteger(R.integer.countdown_initial_value).toLong())
                egg.value = newEgg
                insert(newEgg)
            }
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
        // TODO remove, just for testing
        egg.value?.apply {
            count = count--
        }
    }

    fun onEggClicked() {
        egg.value?.apply {
            count = count--
        }
    }

    /**
     * Called when the ViewModel is dismantled.
     * At this point, we want to cancel all coroutines;
     * otherwise we end up with processes that have nowhere to return to
     * using memory and resources.
     */
    override fun onCleared() {
        super.onCleared()
        uiScope.launch {
           val oldEgg = egg.value ?: return@launch
            update(oldEgg)
        }
        viewModelJob.cancel()
    }
}