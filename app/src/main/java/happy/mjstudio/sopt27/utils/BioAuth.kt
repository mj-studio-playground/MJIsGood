package happy.mjstudio.sopt27.utils

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.AuthenticationCallback
import androidx.biometric.BiometricPrompt.AuthenticationResult
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class BioAuth @Inject constructor(private val context: Context) {
    private val promptInfo = BiometricPrompt.PromptInfo.Builder().apply {
        this.setTitle("Title")
        this.setDescription("Description")
        setNegativeButtonText("Cancel")
        setAllowedAuthenticators(AUTHENTICATORS)
    }.build()

    val biometricEnabled: Boolean
        get() = BiometricManager.from(context).canAuthenticate(AUTHENTICATORS) == BIOMETRIC_SUCCESS

    suspend fun authenticate(fragment: Fragment) = suspendCancellableCoroutine<Boolean> { continuation ->
        BiometricPrompt(fragment, ContextCompat.getMainExecutor(context), object : AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                continuation.resume(false)
            }

            override fun onAuthenticationSucceeded(result: AuthenticationResult) {
                continuation.resume(true)
            }

            override fun onAuthenticationFailed() {
                continuation.resume(false)
            }
        }).authenticate(promptInfo)
    }

    companion object {
        private const val AUTHENTICATORS = BIOMETRIC_WEAK
    }
}