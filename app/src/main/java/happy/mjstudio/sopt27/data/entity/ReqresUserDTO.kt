package happy.mjstudio.sopt27.data.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ReqresUserDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("email") val email: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("avatar") val avatar: String
) : Parcelable