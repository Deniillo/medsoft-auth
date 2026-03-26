package itmo.medsoft.controller

import itmo.medsoft.dto.LoginRequest
import itmo.medsoft.dto.RegisterRequest
import itmo.medsoft.service.AuthService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@Controller
class AuthPageController(
    private val authService: AuthService
) {

    @GetMapping("/register")
    fun registerPage(): String = "register"

    @GetMapping("/login")
    fun loginPage(): String = "login"

    @PostMapping("/register")
    fun handleRegister(request: RegisterRequest, model: Model): String {
        return try {
            val id = authService.register(request.login, request.password)
            model.addAttribute("message", "Registered successfully! Your ID: $id")
            "register"
        } catch (e: Exception) {
            model.addAttribute("error", e.message)
            "register"
        }
    }

    @PostMapping("/login")
    fun handleLogin(request: LoginRequest, model: Model): String {
        return try {
            authService.login(request.login, request.password)
            model.addAttribute("message", "Login successful!")
            "login"
        } catch (e: Exception) {
            model.addAttribute("error", "Login failed: ${e.message}")
            "login"
        }
    }
}