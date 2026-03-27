package itmo.medsoft.controller

import itmo.medsoft.dto.LoginRequest
import itmo.medsoft.dto.RegisterRequest
import itmo.medsoft.model.User
import itmo.medsoft.repository.UserRepository
import itmo.medsoft.service.AuthService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import jakarta.servlet.http.HttpSession

@Controller
class AuthPageController(
    private val authService: AuthService,
    private val userRepository: UserRepository
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
    fun handleLogin(request: LoginRequest, session: HttpSession, model: Model): String {
        return try {
            authService.login(request.login, request.password)
            session.setAttribute("login", request.login)
            "redirect:/profile"
        } catch (e: Exception) {
            model.addAttribute("error", "Login failed: ${e.message}")
            "login"
        }
    }

    @GetMapping("/profile")
    fun profilePage(session: HttpSession, model: Model): String {
        val login = session.getAttribute("login") as? String
        if (login == null) {
            model.addAttribute("error", "You are not logged in")
            return "login"
        }

        val userOpt = userRepository.findByLogin(login)
        if (userOpt.isPresent) {
            val user: User = userOpt.get()
            model.addAttribute("login", user.login)
            model.addAttribute("createdAt", user.createdAt)
        } else {
            model.addAttribute("error", "User not found")
            return "login"
        }
        return "profile"
    }

    @GetMapping("/logout")
    fun logout(session: HttpSession): String {
        session.invalidate()
        return "redirect:/login"
    }
}