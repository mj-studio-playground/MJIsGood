package happy.mjstudio.sopt27.authentication

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV
import androidx.security.crypto.EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
import androidx.security.crypto.MasterKeys
import javax.inject.Inject
import javax.inject.Named

class EncryptedSharedPreferencesAuthenticator
@Inject constructor(context: Context, @Named("SharedPreferences") authenticator: Authenticator) :
    Authenticator by ((authenticator as? SharedPreferencesAuthenticator
        ?: throw RuntimeException("Fix your type casting")).apply {
        val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val encryptedSharedPreferences =
            EncryptedSharedPreferences.create("EncryptedSharedPreferences", masterKey, context, AES256_SIV, AES256_GCM)

        replaceSharedPreferences(encryptedSharedPreferences)
    })