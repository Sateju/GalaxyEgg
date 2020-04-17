package lincete.galaxyegg.domain.usecase

import android.content.Context
import androidx.appcompat.app.AlertDialog
import lincete.galaxyegg.domain.usecase.impl.GetBannerAlertImpl

interface GetBannerAlert {

    fun getBannerAlert(context: Context, bannerAlertClickListener: GetBannerAlertImpl.BannerAlertClickListener): AlertDialog
}