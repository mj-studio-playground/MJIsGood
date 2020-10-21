package happy.mjstudio.sopt27.model

import kotlin.random.Random

data class Sample(
    val title: String,
    val subTitle: String,
    val imageUrl: String = "https://picsum.photos/${Random.nextInt(100, 300)}",
)
