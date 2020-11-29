package happy.mjstudio.sopt27.data.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
data class KakaoSearchDTO(
    @SerializedName("contents") val contents: String,
    @SerializedName("datetime") val datetime: String,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String
) : Parcelable