package lincete.galaxyegg.ui.game

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lincete.galaxyegg.R
import lincete.galaxyegg.data.database.EggDao
import lincete.galaxyegg.data.database.EggEntity
import lincete.galaxyegg.domain.usecase.GetEggImages
import lincete.galaxyegg.utils.SharedPreferencesHelper
import lincete.galaxyegg.utils.SharedPreferencesHelper.Companion.PREFERENCE_SOUND

class GameViewModel(private val database: EggDao,
                    private val preferenceHelper: SharedPreferencesHelper,
                    private val eggImageUseCase: GetEggImages,
                    application: Application) : AndroidViewModel(application) {

    companion object {
        private const val DONE = 0L

        // Countdown time interval
        private const val ONE_SECOND = 1000L

        // Seconds to show the Add
        private const val COUNTDOWN_TIME = 6000L
    }

    private val totalCount = application.resources.getInteger(R.integer.countdown_initial_value).toLong()

    private val _isVolumeActive = MutableLiveData<Boolean>()
    private val isVolumeActive: LiveData<Boolean>
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

    private val _eggBackground = MutableLiveData<Int>()
    val eggBackground: LiveData<Int>
        get() = _eggBackground

    private val _shouldShowAdd = MutableLiveData<Boolean>()
    val shouldShowAdd: LiveData<Boolean>
        get() = _shouldShowAdd

    private var addTimer: CountDownTimer

    init {
        initializeSound()
        initializeEgg()
        startAddTimer()
        _startAnimationEvent.value = false
        _startSoundEvent.value = false
    }

    private fun initializeSound() {
        _isVolumeActive.value = preferenceHelper.isPreferenceSoundEnabled()
        preferenceHelper.sharedPreferences.registerOnSharedPreferenceChangeListener { _, key ->
            if (key == PREFERENCE_SOUND) {
                _isVolumeActive.value = preferenceHelper.isPreferenceSoundEnabled()
            }
        }
    }

    private fun initializeEgg() {
        viewModelScope.launch {
            val eggFromDatabase = getEggFromDatabase()
            if (eggFromDatabase != null) {
                _egg.value = eggFromDatabase
            } else {
                val newEgg = EggEntity(count = totalCount)
                _egg.value = newEgg
                insert(newEgg)
            }
            _eggCount.value = _egg.value?.count
        }
    }

    private fun startAddTimer() {
        addTimer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onFinish() {
                _shouldShowAdd.value = true
            }

            override fun onTick(p0: Long) {

            }
        }
        addTimer.start()
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
        checkIfBackgroundShouldChange()
    }

    fun setAnimationIsFinished() {
        _startAnimationEvent.value = false
    }

    private fun startAnimation() {
        _startAnimationEvent.value = true
    }

    private fun startSound() {
        _startSoundEvent.value = false
        _startSoundEvent.value = true
    }

    private fun checkIfBackgroundShouldChange() {
        eggCount.value?.let {
            _eggBackground.value = eggImageUseCase.getEggImageFromCounter(it, totalCount)
        }
    }

    fun resetShouldShowAdd() {
        _shouldShowAdd.value = false
    }
}