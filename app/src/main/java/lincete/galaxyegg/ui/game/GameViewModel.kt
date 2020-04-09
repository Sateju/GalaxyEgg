package lincete.galaxyegg.ui.game

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import lincete.galaxyegg.data.database.EggDatabaseDao

class GameViewModel(
        val eggDao: EggDatabaseDao,
        application: Application) : AndroidViewModel(application) {


    fun onVolumeChanged() {

    }

    fun onEggClicked() {

    }
}