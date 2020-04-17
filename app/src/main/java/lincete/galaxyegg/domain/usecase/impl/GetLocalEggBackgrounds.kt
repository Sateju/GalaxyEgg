package lincete.galaxyegg.domain.usecase.impl

import lincete.galaxyegg.R
import lincete.galaxyegg.domain.usecase.GetEggBackgrounds

class GetLocalEggBackgrounds : GetEggBackgrounds {

    override fun getEggBackgroundsId(): List<Int> = listOf(
            R.drawable.egg1,
            R.drawable.egg2,
            R.drawable.egg3,
            R.drawable.egg4,
            R.drawable.egg5,
            R.drawable.egg6,
            R.drawable.egg7,
            R.drawable.egg8,
            R.drawable.egg9,
            R.drawable.egg10,
            R.drawable.egg11,
            R.drawable.egg12,
            R.drawable.egg13,
            R.drawable.egg14)
}