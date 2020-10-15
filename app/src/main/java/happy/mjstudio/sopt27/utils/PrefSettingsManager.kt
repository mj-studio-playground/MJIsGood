package happy.mjstudio.sopt27.utils

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefSettingsManager @Inject constructor(context: Context) {
    private val dataStore = context.createDataStore("setting")

    val lastSignInInfo: Flow<LastSignInInfo> = dataStore.data.catch { e ->
        if (e is IOException) {
            emit(emptyPreferences())
        } else {
            throw e
        }
    }.map { pref ->
        val id = pref[ID_KEY] ?: ""
        val pw = pref[PW_KEY] ?: ""

        LastSignInInfo(id, pw)
    }

    suspend fun updateLastSignInInfo(id: String, pw: String) {
        dataStore.edit { pref ->
            pref[ID_KEY] = id
            pref[PW_KEY] = pw
        }
    }

    companion object {
        private val ID_KEY = preferencesKey<String>("id")
        private val PW_KEY = preferencesKey<String>("pw")
    }
}