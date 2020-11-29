package happy.mjstudio.sopt27.data.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import happy.mjstudio.sopt27.data.entity.KakaoSearchDTO
import kotlinx.android.parcel.Parcelize
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoService {
    @GET("v2/search/web")
    suspend fun search(@Query("query") query: String): KakaoSearchResponse
}

@Parcelize
data class KakaoSearchResponse(
    @SerializedName("documents") val items: List<KakaoSearchDTO>,
) : Parcelable