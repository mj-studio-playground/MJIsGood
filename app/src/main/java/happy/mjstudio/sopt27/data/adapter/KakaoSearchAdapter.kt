package happy.mjstudio.sopt27.data.adapter

import happy.mjstudio.sopt27.data.entity.KakaoSearchDTO
import happy.mjstudio.sopt27.domain.entity.KakaoSearchEntity
import java.text.SimpleDateFormat
import java.util.*

object KakaoSearchAdapter : ModelAdapter<KakaoSearchDTO, KakaoSearchEntity> {
    private val timeZone = TimeZone.getTimeZone("Asia/Seoul")

    private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.KOREA).apply {
        timeZone = timeZone
    }

    override fun toEntity(source: KakaoSearchDTO): KakaoSearchEntity {

        val cal = Calendar.getInstance(timeZone).apply {
            time = formatter.parse(source.datetime) ?: Date()
        }

        return KakaoSearchEntity(source.contents, cal, source.title, source.url)
    }

    override fun toDTO(source: KakaoSearchEntity): KakaoSearchDTO {
        return KakaoSearchDTO(source.contents, formatter.format(source.datetime.time), source.title, source.url)
    }
}