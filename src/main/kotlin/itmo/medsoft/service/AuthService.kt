package itmo.medsoft.service

import itmo.medsoft.model.User
import itmo.medsoft.repository.UserRepository
import itmo.medsoft.security.AuthValidator
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordService: PasswordService
) {

    fun register(login: String, password: String): Long {

        AuthValidator.validateLogin(login)
        AuthValidator.validatePassword(password)

        if (userRepository.findByLogin(login).isPresent) {
            throw IllegalArgumentException("Login already exists")
        }

        val (hash, algorithm) = passwordService.hash(password)

        val user = User(
            login = login,
            passwordHash = hash,
            algorithm = algorithm.name
        )

        val saved = userRepository.save(user)

        return saved.id
    }

    fun login(login: String, password: String): User {

        val user = userRepository.findByLogin(login)
            .orElseThrow { IllegalArgumentException("Invalid credentials") }

        val matches = passwordService.matches(
            password,
            user.passwordHash,
            user.algorithm
        )

        if (!matches) {
            throw IllegalArgumentException("Invalid credentials")
        }

        return user
    }
}