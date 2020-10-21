package happy.mjstudio.sopt27.authentication

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.datastore.preferences.remove
import happy.mjstudio.sopt27.utils.logE
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataStorePreferencesAuthenticator @Inject constructor(context: Context, private val validator: IdValidator) :
    Authenticator {
    private val dataStore = context.createDataStore("DataStorePreferencesAuthenticator")

    override suspend fun canAutoSignIn() = runCatching {
        val pref = dataStore.data.first()
        !pref[ID_KEY].isNullOrBlank() && !pref[PW_KEY].isNullOrBlank()
    }.getOrDefault(false)

    override suspend fun signUpWithId(id: String, password: String) {
        logE("$id $password")
        dataStore.edit { pref ->
            pref[ID_KEY] = id
            pref[PW_KEY] = password
        }
    }

    override suspend fun signInWithId(id: String, password: String): Boolean {
        return runCatching {
            val pref = dataStore.data.first()
            validator.validateIdAndPwWithOthers(id, password, pref[ID_KEY], pref[PW_KEY])
        }.getOrDefault(false)
    }

    override suspend fun signOut() {
        dataStore.edit { pref ->
            pref.remove(ID_KEY)
            pref.remove(PW_KEY)
        }
    }

    companion object {
        private val ID_KEY = preferencesKey<String>("id")
        private val PW_KEY = preferencesKey<String>("pw")
    }
}