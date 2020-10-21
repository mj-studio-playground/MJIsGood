package happy.mjstudio.sopt27.authentication

import happy.mjstudio.sopt27.utils.logE

class IdValidator {
    fun validateIdAndPwWithOthers(id: String, pw: String, lastId: String?, lastPw: String?): Boolean {
        return id.isNotBlank() && pw.isNotBlank() && id == lastId && pw == lastPw
    }
}