package happy.mjstudio.sopt27.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import happy.mjstudio.sopt27.data.datsource.LocalSampleDataSource
import happy.mjstudio.sopt27.domain.datasource.SampleDataSource
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
abstract class DataSourceModule {
    @Binds
    @Singleton
    @Named("Local")
    abstract fun bindLocalSampleDataSource(dataSource: LocalSampleDataSource): SampleDataSource
}