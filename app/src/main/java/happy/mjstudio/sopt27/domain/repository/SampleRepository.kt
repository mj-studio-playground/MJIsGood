package happy.mjstudio.sopt27.domain.repository

import happy.mjstudio.sopt27.domain.entity.Sample

interface SampleRepository {
    suspend fun fetchSamples(): List<Sample>
}