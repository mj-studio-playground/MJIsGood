package happy.mjstudio.sopt27.data.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import happy.mjstudio.sopt27.data.entity.ReqresUserDTO
import kotlinx.android.parcel.Parcelize
import retrofit2.http.GET
import retrofit2.http.Query

interface ReqresService {
    @GET("users")
    suspend fun listUsers(@Query("page") page: Int): ReqresListUsersResponse
}

@Parcelize
data class ReqresListUsersResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("per_page") val perPage: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("data") val users: List<ReqresUserDTO>,
) : Parcelable