package itmo.medsoft.security

object AuthValidator {

    private val loginRegex = Regex("^[a-zA-Z0-9]{3,16}$")

    fun validateLogin(login: String) {
        if (!loginRegex.matches(login)) {
            throw IllegalArgumentException("Invalid login format")
        }
    }

    fun validatePassword(password: String) {
        if (password.length !in 10..16) {
            throw IllegalArgumentException("Password must be 10-16 characters")
        }
    }
}