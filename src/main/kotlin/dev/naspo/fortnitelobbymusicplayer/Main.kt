package dev.naspo.fortnitelobbymusicplayer

import io.github.oshai.kotlinlogging.KotlinLogging
import net.sourceforge.tess4j.Tesseract
import net.sourceforge.tess4j.TesseractException
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.jvm.javaClass
import kotlin.system.exitProcess

val logger = KotlinLogging.logger { }

fun main() {
    // Program init
    performPlatformCheck()
    registerShutdownHook()
    initOCR()
    logger.info { "Fortnite Lobby Music Player initialized!" }

    testOCR()
}

// If not on Windows, terminate the program.
private fun performPlatformCheck() {
    fun isOnWindows() = System.getProperty("os.name").lowercase().startsWith("win")

    if (!isOnWindows()) {
        logger.error { "Fortnite Lobby Music Player must be running on Windows." }
        exitProcess(1)
    }
}

/**
 * Register's a shutdown hook to execute logic upon program shutdown.
 */
private fun registerShutdownHook() {
    Runtime.getRuntime().addShutdownHook(Thread {
        runOCRCleanup()
    })
}
