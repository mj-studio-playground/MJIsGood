package happy.mjstudio.sopt27.presentation.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import happy.mjstudio.sopt27.model.Sample
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(/*@Assisted private val savedStateHandle: SavedStateHandle*/) :
    ViewModel() {

    private val _datas: MutableStateFlow<List<Sample>> = MutableStateFlow((1..100).map {
        Sample("Title$it", "sub title - $it")
    })
    val datas: StateFlow<List<Sample>> = _datas

}