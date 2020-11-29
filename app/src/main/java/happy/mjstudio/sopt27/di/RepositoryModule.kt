package happy.mjstudio.sopt27.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import happy.mjstudio.sopt27.data.repository.KakaoRepositoryImpl
import happy.mjstudio.sopt27.data.repository.ReqresRepositoryImpl
import happy.mjstudio.sopt27.data.repository.SampleRepositoryImpl
import happy.mjstudio.sopt27.domain.repository.KakaoRepository
import happy.mjstudio.sopt27.domain.repository.ReqresRepository
import happy.mjstudio.sopt27.domain.repository.SampleRepository
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSampleRepository(repository: SampleRepositoryImpl): SampleRepository

    @Binds
    @Singleton
    abstract fun bindReqresRepository(repository: ReqresRepositoryImpl): ReqresRepository

    @Binds
    @Singleton
    abstract fun bindKakaoRepository(repository: KakaoRepositoryImpl): KakaoRepository
}