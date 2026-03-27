package itmo.medsoft.controller

import itmo.medsoft.dto.LoginRequest
import itmo.medsoft.dto.RegisterRequest
import itmo.medsoft.service.AuthService
import itmo.medsoft.repository.UserRepository
import jakarta.servlet.http.HttpSession
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

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
            val user = authService.login(request.login, request.password)

            if (user.totpEnabled) {
                session.setAttribute("tmp_user", user.login)
                return "redirect:/2fa"
            }

            val auth = UsernamePasswordAuthenticationToken(
                user.login,
                null,
                listOf(SimpleGrantedAuthority("USER"))
            )
            SecurityContextHolder.getContext().authentication = auth

            session.setAttribute("login", user.login)
            "redirect:/profile"

        } catch (e: Exception) {
            model.addAttribute("error", "Login failed: ${e.message}")
            "login"
        }
    }

    @GetMapping("/profile")
    fun profilePage(session: HttpSession, model: Model): String {
        val login = session.getAttribute("login") as? String
            ?: return "redirect:/login"

        val user = userRepository.findByLogin(login).orElseThrow()

        model.addAttribute("login", user.login)
        model.addAttribute("createdAt", user.createdAt)

        return "profile"
    }

    @GetMapping("/logout")
    fun logout(session: HttpSession): String {
        session.invalidate()
        return "redirect:/login"
    }
}