package happy.mjstudio.sopt27.presentation.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import happy.mjstudio.sopt27.domain.repository.SampleRepository

class MainViewModel @ViewModelInject constructor(private val sampleRepository: SampleRepository /*@Assisted private val savedStateHandle: SavedStateHandle*/) :
    ViewModel() {
    val samples = liveData {
        emit(sampleRepository.fetchSamples())
    }
}