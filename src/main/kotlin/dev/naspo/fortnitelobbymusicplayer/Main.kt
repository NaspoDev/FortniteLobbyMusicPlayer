package dev.naspo.fortnitelobbymusicplayer

import net.sourceforge.tess4j.Tesseract
import net.sourceforge.tess4j.TesseractException
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.jvm.javaClass
import kotlin.system.exitProcess

const val classicFortniteSoundtrackFileName = "Fortnite_OG_Classic.mp3"

fun main() {
    performPlatformCheck()
    registerShutdownHook()

    // OCR
    initOCR()
    testOCR()
}

// If not on Windows, terminate the program.
private fun performPlatformCheck() {
    fun isOnWindows() = System.getProperty("os.name").lowercase().startsWith("win")

    if (!isOnWindows()) {
        println("Fortnite Lobby Music Player must be running on Windows.")
        exitProcess(0)
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
