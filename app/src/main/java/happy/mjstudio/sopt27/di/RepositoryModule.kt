package happy.mjstudio.sopt27.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import happy.mjstudio.sopt27.data.repository.SampleRepositoryImpl
import happy.mjstudio.sopt27.domain.repository.SampleRepository
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindSampleRepository(repository: SampleRepositoryImpl): SampleRepository
}