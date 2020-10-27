package happy.mjstudio.sopt27.data.repository

import happy.mjstudio.sopt27.domain.datasource.SampleDataSource
import happy.mjstudio.sopt27.domain.entity.Sample
import happy.mjstudio.sopt27.domain.repository.SampleRepository
import javax.inject.Inject
import javax.inject.Named

class SampleRepositoryImpl @Inject constructor(@Named("Local") private val localDataSource: SampleDataSource) :
    SampleRepository {
    override suspend fun fetchSamples(): List<Sample> {
        return localDataSource.fetchSamples()
    }
}