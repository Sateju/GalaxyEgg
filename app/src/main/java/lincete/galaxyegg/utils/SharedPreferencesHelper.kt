package lincete.galaxyegg.utils

import android.app.Application
import androidx.preference.PreferenceManager

class SharedPreferencesHelper(application: Application) {

    companion object {
        const val PREFERENCE_SOUND = "preference_sound"
    }

    private val preferences = PreferenceManager.getDefaultSharedPreferences(application)

    fun isPreferenceSoundEnabled(): Boolean = preferences.getBoolean(PREFERENCE_SOUND, false)
}