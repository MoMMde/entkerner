package net.kerner.entkerner.backdoor.discord.jsexecinstaller

import io.ktor.client.*
import net.kerner.entkerner.Entkerner
import net.kerner.entkerner.abstract.AbstractConsoleWorker
import net.kerner.entkerner.abstract.SystemFileURI
import net.kerner.entkerner.io.nullPaths
import java.io.File

class NodeJSNPMInstaller(ioWorker: HttpClient, entkerner: Entkerner) : AbstractConsoleWorker(ioWorker, entkerner) {
    override val name: String = "Node.JSNPMInstaller"
    override val file: SystemFileURI = nullPaths
    init {
        linux = listOf(
                "if"
        )
        windows = listOf(
                ""
        )
    }

    override fun buildDoor(file: File): Boolean {
        return true
    }
}