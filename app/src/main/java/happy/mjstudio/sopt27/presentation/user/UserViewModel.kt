package happy.mjstudio.sopt27.presentation.user

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import happy.mjstudio.sopt27.domain.entity.ReqresUserEntity
import happy.mjstudio.sopt27.domain.repository.ReqresRepository
import kotlinx.coroutines.launch

class UserViewModel @ViewModelInject constructor(private val repository: ReqresRepository) : ViewModel() {
    private val _users: MutableLiveData<List<ReqresUserEntity>> = MutableLiveData(listOf())
    val users: LiveData<List<ReqresUserEntity>> = _users

    private val _loadingUsers: MutableLiveData<Boolean> = MutableLiveData(true)
    val loadingUsers: LiveData<Boolean> = _loadingUsers

    init {
        listUsers()
    }

    private fun listUsers() = viewModelScope.launch {
        kotlin.runCatching {
            _loadingUsers.value = true
            repository.listUsers()
        }.onSuccess {
            _users.value = it
        }.onFailure {}
        _loadingUsers.value = false
    }
}