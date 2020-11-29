package happy.mjstudio.sopt27.presentation.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import happy.mjstudio.sopt27.domain.entity.KakaoSearchEntity
import happy.mjstudio.sopt27.domain.repository.KakaoRepository
import happy.mjstudio.sopt27.utils.logE
import kotlinx.coroutines.launch

class SearchViewModel @ViewModelInject constructor(/*@Assisted private val savedStateHandle: SavedStateHandle*/ private val repository: KakaoRepository) :
    ViewModel() {

    private val _items: MutableLiveData<List<KakaoSearchEntity>> = MutableLiveData(listOf())
    val items: LiveData<List<KakaoSearchEntity>> = _items

    fun search(query: String) = viewModelScope.launch {
        kotlin.runCatching {
            logE("search $query")
            repository.search(query)
        }.onSuccess {
            _items.value = it
        }
    }
}