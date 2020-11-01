package happy.mjstudio.sopt27.presentation.profile

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class ProfileViewModel @ViewModelInject constructor(@Assisted private val savedStateHandle: SavedStateHandle) :
    ViewModel() {

    private val _tabIdx: MutableLiveData<Int> = MutableLiveData(0)
    val tabIdx: LiveData<Int> = _tabIdx

    fun onTabIdxChanged(idx: Int) {
        if (tabIdx.value != idx) {
            _tabIdx.value = idx
        }
    }
}