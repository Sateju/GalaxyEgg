package lincete.galaxyegg.domain.usecase

import android.content.Context
import androidx.appcompat.app.AlertDialog

interface GetBannerAlert {

    fun getBannerAlert(context: Context) : AlertDialog
}