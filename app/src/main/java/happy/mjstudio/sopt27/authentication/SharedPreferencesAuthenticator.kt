package happy.mjstudio.sopt27.authentication

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import happy.mjstudio.sopt27.utils.logE
import javax.inject.Inject

class SharedPreferencesAuthenticator @Inject constructor(
    context: Context, private val validator: IdValidator
) : Authenticator {
    private var sharedPreferences = context.getSharedPreferences("sharedPreferences", 0)

    fun replaceSharedPreferences(sharedPreferences: SharedPreferences) {
        logE(sharedPreferences)
        this.sharedPreferences = sharedPreferences
    }

    override suspend fun canAutoSignIn() = sharedPreferences.getBoolean(AUTO_SIGNIN_KEY, false)

    override suspend fun signUpWithId(id: String, password: String) = sharedPreferences.edit(true) {
        putString(ID_KEY, id)
        putString(PW_KEY, password)
    }

    override suspend fun signInWithId(id: String, password: String) = validator.validateIdAndPwWithOthers(
        id, password, sharedPreferences.getString(ID_KEY, ""), sharedPreferences.getString(PW_KEY, "")
    ).also {
        if (it) {
            sharedPreferences.edit(true) {
                putBoolean(AUTO_SIGNIN_KEY, true)
            }
        }
    }

    override suspend fun signOut() {
        sharedPreferences.edit(true) {
            remove(AUTO_SIGNIN_KEY)
        }
    }

    companion object {
        private const val ID_KEY = "ID"
        private const val PW_KEY = "PW"
        private const val AUTO_SIGNIN_KEY = "AUTO_SIGNIN"
    }
}