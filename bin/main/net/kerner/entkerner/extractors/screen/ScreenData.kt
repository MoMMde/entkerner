package net.kerner.entkerner.extractors.screen

@kotlinx.serialization.Serializable
data class ScreenData(
        val height: Int,
        val width: Int,
        val refreshRate: Int,
        val default: Boolean = false
)