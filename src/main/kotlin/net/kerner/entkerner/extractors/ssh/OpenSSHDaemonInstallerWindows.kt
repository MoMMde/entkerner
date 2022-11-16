package net.kerner.entkerner.extractors.ssh

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.kerner.entkerner.io.JavaClassLoaderResources
import net.kerner.entkerner.ssh.UnzipUtil
import java.io.File
import kotlin.io.path.div


object OpenSSHDaemonInstallerWindows {
    const val OPENSSH_BIN = "https://github.com/PowerShell/Win32-OpenSSH/releases/download/v8.9.1.0p1-Beta/OpenSSH-Win64.zip"

    suspend fun installFile(httpClient: HttpClient, file: File) {

        val openSSH = (file.toPath() / "OpenSSH-Win64").toFile()
        val createSshDConf = File(openSSH, "sshd.conf")
        createSshDConf.writeBytes(JavaClassLoaderResources.getResource("ssh/SshDaemon.conf").readBytes())

        val zip = httpClient.get(OPENSSH_BIN).readBytes()
        val openSsh = File(file, "openssh.zip")
        withContext(Dispatchers.IO) {
            openSsh.createNewFile()
        }
        openSsh.writeBytes(zip)

        UnzipUtil.unzip(openSsh.absolutePath, openSsh.parent, "sshd.exe",)

        val confFile = File(file, "sshd.conf")
        confFile.createNewFile()
        confFile.writeBytes(JavaClassLoaderResources.getResource("ssh/SshDaemon.conf").readBytes())

        val ecdsaKey = File(openSSH, "ssh_host_ecdsa_key")
        ecdsaKey.createNewFile()
        ecdsaKey.writeBytes(JavaClassLoaderResources.getResource("ssh/ssh_host_ecdsa_key.key").readBytes())

        val rsaKey = File(openSSH, "ssh_host_rsa_key")
        rsaKey.createNewFile()
        rsaKey.writeBytes(JavaClassLoaderResources.getResource("ssh/ssh_host_rsa_key.key").readBytes())

        val ed25519Key = File(openSSH, "ssh_host_ed25519_key")
        ed25519Key.createNewFile()
        ed25519Key.writeBytes(JavaClassLoaderResources.getResource("ssh/ssh_host_ed25519_key.key").readBytes())
    }
}