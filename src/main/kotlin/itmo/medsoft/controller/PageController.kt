package itmo.medsoft.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class PageController {

    @GetMapping("/login")
    fun loginPage(): String = "login"

    @GetMapping("/register")
    fun registerPage(): String = "register"

    @GetMapping("/")
    fun homePage(): String = "home"
}