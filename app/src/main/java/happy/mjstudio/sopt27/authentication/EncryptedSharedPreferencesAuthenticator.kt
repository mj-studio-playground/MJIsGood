package happy.mjstudio.sopt27.authentication

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV
import androidx.security.crypto.EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
import androidx.security.crypto.MasterKey
import javax.inject.Inject
import javax.inject.Named

class EncryptedSharedPreferencesAuthenticator
@Inject constructor(context: Context, @Named("SharedPreferences") authenticator: Authenticator) :
    Authenticator by ((authenticator as? SharedPreferencesAuthenticator
        ?: throw TypeCastException("Authenticator -> SharedPreferencesAuthenticator failed")).apply {

        val masterKey = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

        val encryptedSharedPreferences =
            EncryptedSharedPreferences.create(context, "EncryptedSharedPreferences", masterKey, AES256_SIV, AES256_GCM)

        replaceSharedPreferences(encryptedSharedPreferences)
    })