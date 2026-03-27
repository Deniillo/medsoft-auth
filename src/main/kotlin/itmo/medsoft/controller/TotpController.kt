package itmo.medsoft.controller

import itmo.medsoft.repository.UserRepository
import itmo.medsoft.service.CryptoService
import itmo.medsoft.service.QrService
import itmo.medsoft.service.TotpService
import jakarta.servlet.http.HttpSession
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
class TotpController(
    private val userRepository: UserRepository,
    private val totpService: TotpService,
    private val cryptoService: CryptoService,
    private val qrService: QrService
) {

    @GetMapping("/2fa/setup")
    fun setup(session: HttpSession, model: Model): String {
        val login = session.getAttribute("login") as String
        val user = userRepository.findByLogin(login).get()

        val secret = totpService.generateSecret()
        user.totpSecret = cryptoService.encrypt(secret)
        userRepository.save(user)

        val url = totpService.buildOtpUrl(login, secret)
        val qr = qrService.generateBase64(url)

        model.addAttribute("qr", qr)
        model.addAttribute("secret", secret)

        return "2fa-setup"
    }

    @PostMapping("/2fa/confirm")
    fun confirm(code: Int, session: HttpSession, model: Model): String {
        val login = session.getAttribute("login") as String
        val user = userRepository.findByLogin(login).get()

        val secret = cryptoService.decrypt(user.totpSecret!!)

        return if (totpService.verifyCode(secret, code)) {
            user.totpEnabled = true
            userRepository.save(user)
            "redirect:/profile"
        } else {
            model.addAttribute("error", "Invalid code")
            "2fa-setup"
        }
    }

    @GetMapping("/2fa")
    fun page(): String = "2fa"

    @PostMapping("/2fa")
    fun verify(code: Int, session: HttpSession, model: Model): String {
        val login = session.getAttribute("tmp_user") as String
        val user = userRepository.findByLogin(login).get()

        val secret = cryptoService.decrypt(user.totpSecret!!)

        return if (totpService.verifyCode(secret, code)) {
            session.removeAttribute("tmp_user")
            session.setAttribute("login", login)
            "redirect:/profile"
        } else {
            model.addAttribute("error", "Invalid code")
            "2fa"
        }
    }
}