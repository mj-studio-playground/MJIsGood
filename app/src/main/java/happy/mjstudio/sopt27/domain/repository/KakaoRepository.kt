package happy.mjstudio.sopt27.domain.repository

import happy.mjstudio.sopt27.domain.entity.KakaoSearchEntity

interface KakaoRepository {
    suspend fun search(query: String): List<KakaoSearchEntity>
}