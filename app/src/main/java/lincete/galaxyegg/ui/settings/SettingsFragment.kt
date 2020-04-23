package lincete.galaxyegg.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import lincete.galaxyegg.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}
