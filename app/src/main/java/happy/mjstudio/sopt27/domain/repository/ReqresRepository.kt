package happy.mjstudio.sopt27.domain.repository

import happy.mjstudio.sopt27.domain.entity.ReqresUserEntity

interface ReqresRepository {
    suspend fun listUsers(): List<ReqresUserEntity>
}