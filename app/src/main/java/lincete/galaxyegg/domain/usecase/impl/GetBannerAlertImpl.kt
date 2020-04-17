package lincete.galaxyegg.domain.usecase.impl

import android.content.Context
import androidx.appcompat.app.AlertDialog
import lincete.galaxyegg.R
import lincete.galaxyegg.domain.usecase.GetBannerAlert

class GetBannerAlertImpl : GetBannerAlert {

    interface BannerAlertClickListener {

        fun onConfirmClicked()
        fun onCancelClicked()
    }

    override fun getBannerAlert(context: Context, bannerAlertClickListener: BannerAlertClickListener): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)

        builder.setTitle(R.string.banner_alert_title)
        builder.setMessage(R.string.banner_alert_content)
        builder.setPositiveButton(R.string.banner_alert_confirm_button_text) { _, _ ->
            bannerAlertClickListener.onConfirmClicked()
        }
        builder.setNegativeButton(R.string.banner_alert_cancel_button_text) { _, _ ->
            bannerAlertClickListener.onCancelClicked()
        }

        return builder.create()
    }
}