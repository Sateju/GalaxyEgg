package lincete.galaxyegg.domain.usecase.impl

import android.content.Context
import androidx.appcompat.app.AlertDialog
import lincete.galaxyegg.R
import lincete.galaxyegg.domain.usecase.GetBannerAlert

class GetBannerAlertImpl : GetBannerAlert {

    override fun getBannerAlert(context: Context): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.banner_alert_title)
        builder.setMessage(R.string.banner_alert_content)
        return builder.create()
    }
}