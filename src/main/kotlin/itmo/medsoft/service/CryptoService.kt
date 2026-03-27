package itmo.medsoft.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@Service
class CryptoService(

    @Value("\${app.crypto.key}")
    private val key: String

) {

    private val algorithm = "AES"

    private fun getKey(): SecretKeySpec {
        val keyBytes = key.toByteArray().copyOf(16)
        return SecretKeySpec(keyBytes, algorithm)
    }

    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, getKey())

        val encrypted = cipher.doFinal(data.toByteArray())
        return Base64.getEncoder().encodeToString(encrypted)
    }

    fun decrypt(data: String): String {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, getKey())

        val decoded = Base64.getDecoder().decode(data)
        return String(cipher.doFinal(decoded))
    }
}