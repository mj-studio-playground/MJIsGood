package happy.mjstudio.sopt27.presentation.frame

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class FrameViewModel @ViewModelInject constructor(@Assisted private val savedStateHandle: SavedStateHandle) :
    ViewModel() {
    private val _pageIdx: MutableLiveData<Int> = MutableLiveData(0)
    val pageIdx: LiveData<Int> = _pageIdx

    fun onPageSelected(idx: Int) {
        if (pageIdx.value != idx) _pageIdx.value = idx
    }
}