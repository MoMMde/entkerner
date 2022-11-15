package net.kerner.entkerner.abstract

import io.ktor.client.*
import net.kerner.entkerner.model.SystemType
import java.io.File
import java.nio.file.Path

abstract class AbstractDataExtractor<T : Any?>(
        val ioClient: HttpClient
) {
    abstract val fileDirectories: SystemFileURI
    abstract val name: String
    abstract suspend fun extractData(file: File): T

    // i dont know why this needs to be inside the instance but i dont make the rules
    suspend fun <V : Any> run(file: File) = handleExtractedData<V>(extractData(file))

    /**
     * casts to V but if stuff like bytearrays are returned you can overwrite it
     */
    open suspend fun <V : Any> handleExtractedData(data: T): V {
        return data as V
    }
}

operator fun SystemFileURI.get(system: SystemType): Path {
    this.system = system
    return when(system) {
        SystemType.LINUX -> linux
        SystemType.WINDOWS -> windows
        SystemType.XNU -> darwin
    }
}