package happy.mjstudio.sopt27.data.repository

import happy.mjstudio.sopt27.data.adapter.KakaoSearchAdapter
import happy.mjstudio.sopt27.data.api.KakaoService
import happy.mjstudio.sopt27.domain.entity.KakaoSearchEntity
import happy.mjstudio.sopt27.domain.repository.KakaoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KakaoRepositoryImpl @Inject constructor(private val service: KakaoService) : KakaoRepository {
    override suspend fun search(query: String): List<KakaoSearchEntity> {
        return service.search(query).items.map(KakaoSearchAdapter::toEntity)
    }
}