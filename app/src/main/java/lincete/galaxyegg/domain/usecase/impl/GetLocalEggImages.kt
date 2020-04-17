package lincete.galaxyegg.domain.usecase.impl

import lincete.galaxyegg.R
import lincete.galaxyegg.domain.usecase.GetEggImages

class GetLocalEggImages : GetEggImages {

    override fun getEggImageIds(): List<Int> = listOf(
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

    override fun getEggImageFromCounter(eggCount: Long, totalCount: Long): Int {
        val imageIds = getEggImageIds()
        val passValue: Double = totalCount.div(imageIds.size.toDouble())

        return when {
            eggCount < passValue -> return R.drawable.egg13
            passValue >= eggCount && eggCount < passValue.times(2) -> R.drawable.egg13
            passValue.times(2) >= eggCount && eggCount < passValue.times(3) -> R.drawable.egg12
            passValue.times(3) >= eggCount && eggCount < passValue.times(4) -> R.drawable.egg11
            passValue.times(4) >= eggCount && eggCount < passValue.times(5) -> R.drawable.egg10
            passValue.times(5) >= eggCount && eggCount < passValue.times(6) -> R.drawable.egg9
            passValue.times(6) >= eggCount && eggCount < passValue.times(7) -> R.drawable.egg8
            passValue.times(7) >= eggCount && eggCount < passValue.times(8) -> R.drawable.egg7
            passValue.times(8) >= eggCount && eggCount < passValue.times(9) -> R.drawable.egg6
            passValue.times(9) >= eggCount && eggCount < passValue.times(10) -> R.drawable.egg5
            passValue.times(10) >= eggCount && eggCount < passValue.times(11) -> R.drawable.egg4
            passValue.times(11) >= eggCount && eggCount < passValue.times(12) -> R.drawable.egg3
            passValue.times(12) >= eggCount && eggCount < passValue.times(13) -> R.drawable.egg2
            passValue.times(13) >= eggCount && eggCount < passValue.times(14) -> R.drawable.egg1
            else -> R.drawable.egg1
        }
    }
}