package net.kerner.entkerner.extractors.ssh

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.kerner.entkerner.ssh.UnzipUtil
import java.io.File


object OpenSSHDaemonInstallerWindows {
    const val OPENSSH_BIN = "https://github.com/PowerShell/Win32-OpenSSH/releases/download/v8.9.1.0p1-Beta/OpenSSH-Win64.zip"

    suspend fun installFile(httpClient: HttpClient, file: File, arch: String) {

        val zip = httpClient.get(OPENSSH_BIN).readBytes()
        val openSsh = File(file, "openssh.zip")
        withContext(Dispatchers.IO) {
            openSsh.createNewFile()
        }
        openSsh.writeBytes(zip)

        UnzipUtil.unzip(openSsh.absolutePath, openSsh.parent)

    }
}