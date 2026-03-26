package itmo.medsoft.controller

import itmo.medsoft.dto.LoginRequest
import itmo.medsoft.dto.RegisterRequest
import itmo.medsoft.service.AuthService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): Map<String, Any> {
        val id = authService.register(request.login, request.password)
        return mapOf("id" to id)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): Map<String, Any> {
        authService.login(request.login, request.password)
        return mapOf("status" to "OK")
    }
}