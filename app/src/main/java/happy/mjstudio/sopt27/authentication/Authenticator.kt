package happy.mjstudio.sopt27.authentication

interface Authenticator {
    suspend fun canAutoSignIn(): Boolean
    suspend fun signUpWithId(id: String, password: String)
    suspend fun signInWithId(id: String, password: String): Boolean
    suspend fun signOut()
}