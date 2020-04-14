package lincete.galaxyegg.ui.game

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lincete.galaxyegg.R
import lincete.galaxyegg.data.database.EggDao
import lincete.galaxyegg.data.database.EggEntity

class GameViewModel(private val database: EggDao, application: Application) : AndroidViewModel(application) {

    private val _isVolumeActive = MutableLiveData<Boolean>()
    val isVolumeActive: LiveData<Boolean>
        get() = _isVolumeActive

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

    private val _startAnimationEvent = MutableLiveData<Boolean>()
    val startAnimationEvent: LiveData<Boolean>
        get() = _startAnimationEvent

    private val _startSoundEvent = MutableLiveData<Boolean>()
    val startSoundEvent: LiveData<Boolean>
        get() = _startSoundEvent

    init {
        initializeVolumeButton()
        initializeEgg(application)
        _startAnimationEvent.value = false
        _startSoundEvent.value = false
    }

    private fun initializeVolumeButton() {
        _isVolumeActive.value = false
    }

    private fun initializeEgg(application: Application) {
        viewModelScope.launch {
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
        _isVolumeActive.value = isVolumeActive.value?.not()
    }

    fun onEggClicked() {
        egg.value?.apply {
            count = count.minus(1)
            _eggCount.value = count
        }
        egg.value?.let { egg ->
            viewModelScope.launch {
                update(egg)
            }
        }
        startAnimation()
        if (isVolumeActive.value == true) {
            startSound()
        }
    }

    fun setAnimationIsFinished() {
        _startAnimationEvent.value = false
    }

    private fun startAnimation() {
        _startAnimationEvent.value = true
    }

    fun setSoundIsFinished() {
        _startSoundEvent.value = false
    }

    private fun startSound() {
        _startSoundEvent.value = false
        _startSoundEvent.value = true
    }
}