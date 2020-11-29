package happy.mjstudio.sopt27.data.adapter

import com.google.common.truth.Truth
import happy.mjstudio.sopt27.data.entity.ReqresUserDTO
import happy.mjstudio.sopt27.domain.entity.ReqresUserEntity
import junit.framework.TestCase

class ReqresUserAdapterTest : TestCase() {
    private val mockDto = ReqresUserDTO(1, "mym0404@gmail.com", "myeongju", "Mun", "url")
    private val mockEntity = ReqresUserEntity(1, "mym0404@gmail.com", "myeongju", "Mun", "url")

    fun test_toEntity() {
        Truth.assertThat(ReqresUserAdapter.toEntity(mockDto)).isEqualTo(mockEntity)
    }

    fun test_toDTO() {
        Truth.assertThat(ReqresUserAdapter.toDTO(mockEntity)).isEqualTo(mockDto)
    }
}