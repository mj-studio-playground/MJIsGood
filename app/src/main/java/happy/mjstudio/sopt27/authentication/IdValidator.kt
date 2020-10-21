package happy.mjstudio.sopt27.authentication

import happy.mjstudio.sopt27.utils.logE

class IdValidator {
    fun validateIdAndPwWithOthers(id: String, pw: String, lastId: String?, lastPw: String?): Boolean {
        logE("$id $pw $lastId $lastPw")
        return id.isNotBlank() && pw.isNotBlank() && id == lastId && pw == lastPw
    }
}