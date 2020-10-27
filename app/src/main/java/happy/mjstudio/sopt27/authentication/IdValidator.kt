package happy.mjstudio.sopt27.authentication

class IdValidator {
    fun validateIdAndPwWithOthers(id: String, pw: String, lastId: String?, lastPw: String?): Boolean {
        return id.isNotBlank() && pw.isNotBlank() && id == lastId && pw == lastPw
    }
}