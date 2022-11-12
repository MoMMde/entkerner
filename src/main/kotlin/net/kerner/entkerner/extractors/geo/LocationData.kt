package net.kerner.entkerner.extractors.geo

import kotlinx.serialization.Serializable

/**
 * We are using Google's Location ID's for locating geo. position
 */

@Serializable
data class LocationData(
    val id: String
)