package lincete.galaxyegg.utils

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

class SharedPreferencesHelper(application: Application) {

    companion object {
        const val PREFERENCE_SOUND = "preference_sound"
    }

    val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)

    fun isPreferenceSoundEnabled(): Boolean = sharedPreferences.getBoolean(PREFERENCE_SOUND, true)
}