package happy.mjstudio.sopt27.utils

import javax.inject.Inject

interface NativeLib {
    val reqresBaseUrl: String
    val kakaoBaseUrl: String
    val kakaoApiKey: String
}

class NativeLibImpl @Inject constructor() : NativeLib {
    init {
        System.loadLibrary("native")
    }

    override val reqresBaseUrl: String
        get() = getConstant("REQRES_BASE_URL")
    override val kakaoBaseUrl: String
        get() = getConstant("KAKAO_BASE_URL")
    override val kakaoApiKey: String
        get() = getConstant("KAKAO_API_KEY")

    private external fun getConstant(key: String): String
}