package net.kerner.entkerner.backdoor.discord

import io.ktor.client.*
import net.kerner.entkerner.Entkerner
import net.kerner.entkerner.abstract.AbstractBackdooringWorker
import net.kerner.entkerner.abstract.SystemFileURI
import net.kerner.entkerner.io.nullPaths
import java.io.File

class BetterDiscordInstaller(ioClient: HttpClient, entkerner: Entkerner) : AbstractBackdooringWorker(ioClient, entkerner) {
    override val name = "BetterDiscordInstallerService"
    override val file: SystemFileURI = nullPaths

    private fun cloneBetterDiscord() {

    }

    override fun buildDoor(file: File): Boolean {
        return true
    }
}