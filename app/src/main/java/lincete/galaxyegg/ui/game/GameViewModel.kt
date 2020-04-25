package lincete.galaxyegg.ui.game

import android.app.Application
import android.content.SharedPreferences
import android.os.CountDownTimer
import android.text.format.DateUtils
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
        const val MULTIPLIER_DEFAULT_VALUE = 1

        private const val REWARD_DONE = 0L
        private const val REWARD_TIME_ONE_SECOND = 1000L
        private const val REWARD_COUNTDOWN_TIME = 90 * 1000L
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

    private val _multiplier = MutableLiveData<Int>()
    val multiplier: LiveData<Int>
        get() = _multiplier

    // Countdown time
    private val _rewardCurrentTime = MutableLiveData<Long>()
    private val rewardCurrentTime: LiveData<Long>
        get() = _rewardCurrentTime

    val rewardCountDownText = Transformations.map(rewardCurrentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    private val timer: CountDownTimer
    private lateinit var sharedPreferencesListener: SharedPreferences.OnSharedPreferenceChangeListener

    init {
        initializeSound()
        initializeEgg()
        _startAnimationEvent.value = false
        _startSoundEvent.value = false
        _multiplier.value = MULTIPLIER_DEFAULT_VALUE

        timer = object : CountDownTimer(REWARD_COUNTDOWN_TIME, REWARD_TIME_ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _rewardCurrentTime.value = millisUntilFinished / REWARD_TIME_ONE_SECOND
            }

            override fun onFinish() {
                _rewardCurrentTime.value = REWARD_DONE
                _multiplier.value = MULTIPLIER_DEFAULT_VALUE
            }
        }
    }

    private fun initializeSound() {
        _isVolumeActive.value = preferenceHelper.isPreferenceSoundEnabled()
        sharedPreferencesListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == PREFERENCE_SOUND) {
                _isVolumeActive.value = preferenceHelper.isPreferenceSoundEnabled()
            }
        }
        preferenceHelper.sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferencesListener)
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

    // On clicks

    fun onRewardedAdClicked() {
        _shouldShowAdd.value = true
    }

    fun onEggClicked() {
        val damage = multiplier.value ?: MULTIPLIER_DEFAULT_VALUE
        egg.value?.apply {
            count = count.minus(damage)
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

    // public methods

    fun setAnimationIsFinished() {
        _startAnimationEvent.value = false
    }

    fun enableMultiplier(multiplier: Int) {
        _multiplier.value = multiplier
    }

    fun resetShouldShowAd() {
        _shouldShowAdd.value = false
    }

    fun startRewardTimer() {
        timer.start()
    }

    // private methods

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
}