package happy.mjstudio.sopt27.utils

import android.util.DisplayMetrics
import androidx.annotation.Px
import javax.inject.Inject
import kotlin.math.roundToInt

class PixelRatio @Inject constructor(private val displayMetrics: DisplayMetrics) {
    val screenWidth: Int
        get() = displayMetrics.widthPixels

    val screenHeight: Int
        get() = displayMetrics.heightPixels

    @Px
    fun toPixel(dp: Int) = (dp * displayMetrics.density).roundToInt()

    fun toDP(@Px pixel: Int) = (pixel / displayMetrics.density).roundToInt()
}