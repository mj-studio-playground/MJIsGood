package happy.mjstudio.sopt27.domain.datasource

import happy.mjstudio.sopt27.domain.entity.Sample

interface SampleDataSource {
    suspend fun fetchSamples(): List<Sample>
}