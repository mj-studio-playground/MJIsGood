package happy.mjstudio.sopt27.data.adapter

import com.google.common.truth.Truth
import happy.mjstudio.sopt27.data.entity.KakaoSearchDTO
import junit.framework.TestCase
import java.util.*

class KakaoSearchAdapterTest : TestCase() {
    private val mockDto = KakaoSearchDTO("content", "1997-04-04T00:00:00.000+09:00", "title", "url")
    private var mockEntity = KakaoSearchAdapter.toEntity(mockDto)

    fun testToEntity() {
        val entity = KakaoSearchAdapter.toEntity(mockDto)
        Truth.assertThat(entity.title).isEqualTo("title")
        Truth.assertThat(entity.datetime[Calendar.YEAR]).isEqualTo(1997)
        Truth.assertThat(entity.datetime[Calendar.MONTH] + 1).isEqualTo(4)
        Truth.assertThat(entity.datetime[Calendar.DAY_OF_MONTH]).isEqualTo(4)
    }

    fun testToDTO() {
        Truth.assertThat(KakaoSearchAdapter.toDTO(mockEntity)).isEqualTo(mockDto)
    }
}