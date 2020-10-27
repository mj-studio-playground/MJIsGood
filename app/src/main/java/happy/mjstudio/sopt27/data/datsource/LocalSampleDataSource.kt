package happy.mjstudio.sopt27.data.datsource

import com.thedeanda.lorem.LoremIpsum
import happy.mjstudio.sopt27.domain.datasource.SampleDataSource
import happy.mjstudio.sopt27.domain.entity.Sample
import javax.inject.Inject

class LocalSampleDataSource @Inject constructor(private val loremIpsum: LoremIpsum) : SampleDataSource {
    override suspend fun fetchSamples(): List<Sample> {
        return (1..100).map {
            Sample(loremIpsum.getWords(2), loremIpsum.getWords(6, 12))
        }
    }
}