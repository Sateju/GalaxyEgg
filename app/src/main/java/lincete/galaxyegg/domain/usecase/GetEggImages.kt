package lincete.galaxyegg.domain.usecase

interface GetEggImages {

    fun getEggImageIds(): List<Int>

    fun getEggImageFromCounter(eggCount: Long, totalCount: Long): Int
}