package happy.mjstudio.sopt27.domain.entity

import kotlin.random.Random

data class Sample(
    val title: String,
    val subTitle: String,
    val imageUrl: String = "https://picsum.photos/${Random.nextInt(100, 300)}",
)
