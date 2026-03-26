package itmo.medsoft.service

import itmo.medsoft.model.enum.HashAlgorithm
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class PasswordService {

    private val bcrypt = BCryptPasswordEncoder()
    private val argon2 = Argon2PasswordEncoder(
        16,
        32,
        1,
        65536,
        3
    )

    private val algorithms = listOf(HashAlgorithm.BCRYPT, HashAlgorithm.ARGON2)

    fun hash(password: String): Pair<String, HashAlgorithm> {
        val algorithm = algorithms.random()
        val hash: String = when (algorithm) {
            HashAlgorithm.BCRYPT -> bcrypt.encode(password)
            HashAlgorithm.ARGON2 -> argon2.encode(password)
        }!!
        return hash to algorithm
    }

    fun matches(password: String, hash: String, algorithm: String): Boolean {
        return when (algorithm) {
            HashAlgorithm.BCRYPT.name -> bcrypt.matches(password, hash)
            HashAlgorithm.ARGON2.name -> argon2.matches(password, hash)
            else -> false
        }
    }
}