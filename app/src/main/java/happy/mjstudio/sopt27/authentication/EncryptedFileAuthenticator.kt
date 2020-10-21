package happy.mjstudio.sopt27.authentication

import android.content.Context
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class EncryptedFileAuthenticator @Inject constructor(context: Context, private val validator: IdValidator) :
    Authenticator {
    private val masterKey = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    private val file = File(context.getExternalFilesDir(null), "data.txt")
    private val encryptedFile = EncryptedFile.Builder(
        context, file, masterKey, AES256_GCM_HKDF_4KB
    ).build()

    override suspend fun canAutoSignIn(): Boolean = withContext(Dispatchers.IO) {
        file.exists()
    }

    override suspend fun signUpWithId(id: String, password: String) {
        encryptedFile.openFileOutput().use {
            it.write("$id\n$password\n".toByteArray())
        }
    }

    override suspend fun signInWithId(id: String, password: String): Boolean = withContext(Dispatchers.IO) {
        createFileIfNotExist()
        val content = encryptedFile.openFileInput().bufferedReader().useLines {
            it.fold("") { acc, line -> acc + "$line\n" }
        }
        val lastId = content.split("\n").firstOrNull()
        val lastPw = content.split("\n")[1]

        validator.validateIdAndPwWithOthers(id, password, lastId, lastPw)
    }

    private fun createFileIfNotExist() {
        if (!file.exists()) file.createNewFile()
    }

    override suspend fun signOut() {
        deleteFile()
    }

    private suspend fun deleteFile() = withContext(Dispatchers.IO) {
        file.delete()
    }
}