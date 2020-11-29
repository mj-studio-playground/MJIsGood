package happy.mjstudio.sopt27.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import happy.mjstudio.sopt27.data.api.KakaoService
import happy.mjstudio.sopt27.data.api.ReqresService
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ApiServiceModule {
    @Provides
    @Singleton
    fun provideReqresService(@Named(RetrofitModule.REQRES_QUALIFIER) retrofit: Retrofit) = retrofit.create(
        ReqresService::class.java
    )

    @Provides
    @Singleton
    fun provideKakaoService(@Named(RetrofitModule.KAKAO_QUALIFIER) retrofit: Retrofit) = retrofit.create(
        KakaoService::class.java
    )
}