package dev.naspo.fortnitelobbymusicplayer

import net.sourceforge.tess4j.Tesseract
import net.sourceforge.tess4j.TesseractException
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.jvm.javaClass
import kotlin.system.exitProcess

const val classicFortniteSoundtrackFileName = "Fortnite_Classic_Lobby_Music.mp3"

fun main() {
    performPlatformCheck()
    registerShutdownHook()
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

// TEMP - Testing OCR stuff

fun loadFile(): BufferedImage = object{}.javaClass.classLoader.getResourceAsStream("Fortnite_Lobby_Test.jpg")
        ?.use { ImageIO.read(it) }
        ?: error("Test screenshot file does not exist.")

fun testOCR() {
    val tesseract = Tesseract()
    tesseract.setDatapath("src/main/resources/tessdata")

    try {
        val result: String = tesseract.doOCR(loadFile())
        println(result)
    } catch (e: TesseractException) {
        println(e.message)
    }
}