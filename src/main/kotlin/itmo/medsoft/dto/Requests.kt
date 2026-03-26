package itmo.medsoft.dto

data class RegisterRequest(
    val login: String,
    val password: String
)

data class LoginRequest(
    val login: String,
    val password: String
)