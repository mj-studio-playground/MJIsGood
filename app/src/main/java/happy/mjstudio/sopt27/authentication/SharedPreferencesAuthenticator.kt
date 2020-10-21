package happy.mjstudio.sopt27.authentication

import android.content.Context
import androidx.core.content.edit
import javax.inject.Inject

class SharedPreferencesAuthenticator @Inject constructor(context: Context, private val validator: IdValidator) :
    Authenticator {
    private val sharedPreferences = context.getSharedPreferences("sharedPreferences", 0)

    override suspend fun canAutoSignIn() =
        !sharedPreferences.getString(ID_KEY, "").isNullOrBlank() && !sharedPreferences.getString(PW_KEY, "")
            .isNullOrBlank()

    override suspend fun signUpWithId(id: String, password: String) = sharedPreferences.edit(true) {
        putString(ID_KEY, id)
        putString(PW_KEY, password)
    }

    override suspend fun signInWithId(id: String, password: String) = validator.validateIdAndPwWithOthers(
        id, password, sharedPreferences.getString(ID_KEY, ""), sharedPreferences.getString(PW_KEY, "")
    )

    override suspend fun signOut() {
        sharedPreferences.edit(true) {
            remove(ID_KEY)
            remove(PW_KEY)
        }
    }

    companion object {
        private const val ID_KEY = "ID"
        private const val PW_KEY = "PW"
    }
}