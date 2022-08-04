package net.kerner.entkerner.abstract

import io.ktor.client.*
import net.kerner.entkerner.Entkerner
import net.kerner.entkerner.model.SystemType

abstract class AbstractConsoleWorker(ioClient: HttpClient, entkerner: Entkerner) : AbstractBackdooringWorker(ioClient, entkerner) {
    var linux = listOf<String>()
    var windows = listOf<String>()
    var xnu = listOf<String>()
    val console = System.console()
    fun execute() {
        val commandStack = when(entkerner.system) {
            SystemType.WINDOWS -> windows
            SystemType.XNU -> xnu
            SystemType.LINUX -> linux
        }
        commandStack.forEach { command ->
            console.printf(command)
        }
    }
}