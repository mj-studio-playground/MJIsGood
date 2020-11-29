package happy.mjstudio.sopt27.data.adapter

import happy.mjstudio.sopt27.data.entity.ReqresUserDTO
import happy.mjstudio.sopt27.domain.entity.ReqresUserEntity

object ReqresUserAdapter : ModelAdapter<ReqresUserDTO, ReqresUserEntity> {
    override fun toEntity(source: ReqresUserDTO): ReqresUserEntity {
        return ReqresUserEntity(source.id, source.email, source.firstName, source.lastName, source.avatar)
    }

    override fun toDTO(source: ReqresUserEntity): ReqresUserDTO {
        return ReqresUserDTO(
            source.id, source.email, source.firstName, source.lastName, source.avatar
        )
    }
}