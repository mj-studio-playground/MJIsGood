package happy.mjstudio.sopt27.presentation.signin

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import happy.mjstudio.sopt27.utils.EventLiveData
import happy.mjstudio.sopt27.utils.PrefSettingsManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

typealias AutoSignIn = Boolean

class SignInViewModel @ViewModelInject constructor(
    private val settingsManager: PrefSettingsManager, /*@Assisted private val savedStateHandle: SavedStateHandle*/
) : ViewModel() {
    // StateFlow data binding support is coming in AGP 4.3
    // https://twitter.com/manuelvicnt/status/1314621067831521282
    val id = MutableLiveData("")
    val pw = MutableLiveData("")

    private var isAutoSignInTried = false

    val onSignInSuccess = EventLiveData<AutoSignIn>()
    val onSignInFail = EventLiveData<AutoSignIn>()

    private suspend fun matchWithLastSignInInfo(): Boolean {
        val (lastId, lastPw) = settingsManager.lastSignInInfo.first()
        return id.value == lastId && pw.value == lastPw && lastId.isNotBlank() && lastPw.isNotBlank()
    }

    suspend fun canAutoSignIn(): Boolean {
        if (isAutoSignInTried) return false
        isAutoSignInTried = true

        val (lastId, lastPw) = settingsManager.lastSignInInfo.first()
        val result = lastId.isNotBlank() && lastPw.isNotBlank()
        if (result) {
            id.value = lastId
            pw.value = lastPw
        }
        return result
    }

    fun tryManualSignIn() = viewModelScope.launch {
        if (matchWithLastSignInInfo()) {
            onSignInSuccess.emit(false)
        } else {
            onSignInFail.emit(false)
        }
    }
}