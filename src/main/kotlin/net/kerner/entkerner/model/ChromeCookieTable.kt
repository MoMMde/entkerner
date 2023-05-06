package net.kerner.entkerner.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kerner.entkerner.extractors.chrome.ChromeBasedSystemCookieStorage
import org.jetbrains.exposed.sql.Table

private val SIXTYFOUR_KIB = 65535

object ChromeCookieTable : Table("cookies") {
    val createTimeUTC = long("creation_utc")
    val hostKey = varchar("host_key", SIXTYFOUR_KIB)
    val name = varchar("name", SIXTYFOUR_KIB)
    val value = varchar("value", SIXTYFOUR_KIB)
    val path = varchar("path", SIXTYFOUR_KIB)
    val isSecure = bool("is_secure")
    val isHttpOnly = bool("is_httponly")
    val lastAccessUTC = long("last_access_utc")
    val hasExpires = bool("has_expires")
    val priority = long("priority")
    val encryptedValue = blob("encrypted_value")
    val samesite = varchar("samesite", SIXTYFOUR_KIB)
}

@Serializable
data class ChromeCookie(
    val name: String,
    val encryptedValue: String,
    val decryptedValue: String,
    val osCryptMasterKey: String,
    val website: String,
    @SerialName("creation_utc")
    val createTimeUTC: Long,
    val profile: ChromeBasedSystemCookieStorage
)