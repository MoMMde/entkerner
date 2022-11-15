package net.kerner.entkerner.io

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.util.Base64
import javax.imageio.ImageIO

fun BufferedImage.toBase64(format: String = "jpg"): String {
    val stream = ByteArrayOutputStream()
    ImageIO.write(this, format, stream)
    return Base64.getEncoder().encodeToString(stream.toByteArray())
}

fun ByteArray.toBase64() = Base64.getEncoder().encodeToString(this)