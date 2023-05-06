package net.kerner.entkerner.extractors.keyring

import kotlinx.serialization.Serializable

@Serializable
data class KeyRingItem(
    val id: String,
    val label: String,
    val secret: KeyRingSecret,
    val serviceName: String,
)

@Serializable
data class KeyRingSecret(
    val label: String,
    val path: String,
    val value: String,
    val attributes: Map<String, String>
)