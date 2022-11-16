package net.kerner.entkerner.abstract

import io.ktor.client.*
import net.kerner.entkerner.Entkerner
import java.io.File

/**
 * look at the fucking name
 */
abstract class AbstractBackdooringWorker(val ioClient: HttpClient, open val entkerner: Entkerner) {
    abstract val name: String
    abstract val file: SystemFileURI
    abstract fun buildDoor(file: File): Boolean
}