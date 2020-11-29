package happy.mjstudio.sopt27.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import happy.mjstudio.sopt27.utils.NativeLib
import happy.mjstudio.sopt27.utils.logE
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Logger
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class RetrofitModule {
    private val loggingInterceptor = HttpLoggingInterceptor(Logger.DEFAULT).apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    private val baseClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

    @Provides
    @Singleton
    @Named(REQRES_QUALIFIER)
    fun provideReqresRetrofit(nativeLib: NativeLib) =
        Retrofit.Builder().baseUrl(nativeLib.reqresBaseUrl).client(baseClient)
            .addConverterFactory(GsonConverterFactory.create()).build()

    @Provides
    @Singleton
    @Named(KAKAO_QUALIFIER)
    fun provideKakaoRetrofit(nativeLib: NativeLib): Retrofit {
        val kakaoNetworkInterceptor = object : Interceptor {
            override fun intercept(chain: Chain): Response {
                logE(nativeLib.kakaoApiKey)
                val req =
                    chain.request().newBuilder().addHeader("Authorization", "KakaoAK " + nativeLib.kakaoApiKey).build()
                return chain.proceed(req)
            }
        }

        val kakaoClient = baseClient.newBuilder().addNetworkInterceptor(kakaoNetworkInterceptor).build()

        return Retrofit.Builder().baseUrl(nativeLib.kakaoBaseUrl).client(kakaoClient)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    companion object {
        const val REQRES_QUALIFIER = "Reqres"
        const val KAKAO_QUALIFIER = "Kakao"
    }
}