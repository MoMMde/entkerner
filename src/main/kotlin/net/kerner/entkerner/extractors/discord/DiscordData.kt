package net.kerner.entkerner.extractors.discord

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@kotlinx.serialization.Serializable
data class DiscordData(
        val username: String,
        val avatar: String,
        @SerialName("avatar_decoration")
        val avatarDecoration: String?,
        val discriminator: String,
        @SerialName("public_flags")
        val publicFlags: Short,
        val flags: Short,
        @SerialName("purchased_flags")
        val purchasedFlags: Short,
        val banner: String?,
        @SerialName("banner_color")
        val bannerColor: String,
        @SerialName("accent_color")
        val accentColor: Long,
        val bio: String,
        val id: String,
        val locale: String,
        @SerialName("nsfw_allowed")
        val nsfwAllowed: Boolean,
        @SerialName("mfa_enabled")
        val mfaEnabled: Boolean,
        val email: String,
        val verified: Boolean,
        val phone: String,

        var token: String? = null,
        var avatarString: String? = null,
)

@Serializable
data class DiscordToken(
        val token: String,
        val discordClient: DiscordClientEngine
)