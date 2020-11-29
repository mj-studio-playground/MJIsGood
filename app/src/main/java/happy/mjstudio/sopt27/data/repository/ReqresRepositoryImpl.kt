package happy.mjstudio.sopt27.data.repository

import happy.mjstudio.sopt27.data.adapter.ReqresUserAdapter
import happy.mjstudio.sopt27.data.api.ReqresService
import happy.mjstudio.sopt27.domain.entity.ReqresUserEntity
import happy.mjstudio.sopt27.domain.repository.ReqresRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReqresRepositoryImpl @Inject constructor(private val service: ReqresService) : ReqresRepository {
    override suspend fun listUsers(): List<ReqresUserEntity> {
        return service.listUsers(1).users.map(ReqresUserAdapter::toEntity)
    }
}