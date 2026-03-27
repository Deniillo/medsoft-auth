package itmo.medsoft.service

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.client.j2se.MatrixToImageWriter
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream
import java.util.*

@Service
class QrService {

    fun generateBase64(text: String): String {
        val matrix = MultiFormatWriter()
            .encode(text, BarcodeFormat.QR_CODE, 250, 250)

        val image = MatrixToImageWriter.toBufferedImage(matrix)

        val baos = ByteArrayOutputStream()
        javax.imageio.ImageIO.write(image, "png", baos)

        return Base64.getEncoder().encodeToString(baos.toByteArray())
    }
}