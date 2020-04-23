package lincete.galaxyegg.domain.usecase

interface GetEggImages {

    fun getEggImageFromCounter(eggCount: Long, totalCount: Long): Int
}