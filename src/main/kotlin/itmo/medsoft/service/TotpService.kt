package itmo.medsoft.service

import org.apache.commons.codec.binary.Base32
import org.bouncycastle.crypto.digests.SHA1Digest
import org.bouncycastle.crypto.macs.HMac
import org.bouncycastle.crypto.params.KeyParameter
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.time.Instant

@Service
class TotpService {

    private val base32 = Base32()
    private val random = SecureRandom()

    fun generateSecret(): String {
        val bytes = ByteArray(20)
        random.nextBytes(bytes)
        return base32.encodeToString(bytes)
    }

    fun generateCode(secret: String, time: Long = Instant.now().epochSecond): Int {
        val key = base32.decode(secret)
        val timestep = time / 30

        val data = ByteArray(8)
        for (i in 7 downTo 0) {
            data[i] = (timestep shr (8 * (7 - i))).toByte()
        }

        val mac = HMac(SHA1Digest())
        mac.init(KeyParameter(key))
        mac.update(data, 0, data.size)

        val hash = ByteArray(mac.macSize)
        mac.doFinal(hash, 0)

        val offset = hash.last().toInt() and 0xF

        val binary = ((hash[offset].toInt() and 0x7F) shl 24) or
                ((hash[offset + 1].toInt() and 0xFF) shl 16) or
                ((hash[offset + 2].toInt() and 0xFF) shl 8) or
                (hash[offset + 3].toInt() and 0xFF)

        return binary % 1_000_000
    }

    fun verifyCode(secret: String, code: Int): Boolean {
        val now = Instant.now().epochSecond

        return (-1..1).any {
            generateCode(secret, now + it * 30) == code
        }
    }

    fun buildOtpUrl(login: String, secret: String): String {
        val issuer = "MedSoft"
        return "otpauth://totp/$issuer:$login?secret=$secret&issuer=$issuer"
    }
}