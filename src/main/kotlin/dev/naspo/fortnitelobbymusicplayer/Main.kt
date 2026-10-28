package dev.naspo.fortnitelobbymusicplayer

import javazoom.jl.player.Player
import net.sourceforge.tess4j.Tesseract
import net.sourceforge.tess4j.TesseractException
import java.awt.image.BufferedImage
import java.io.File
import java.io.InputStream
import javax.imageio.ImageIO
import kotlin.jvm.javaClass
import kotlin.system.exitProcess

fun main() {
    performPlatformCheck()

    val classicFortniteSoundtrackFileName = "Fortnite_Classic_Lobby_Music.mp3"
}

// If not on Windows, terminate the program.
private fun performPlatformCheck() {
    fun isOnWindows() = System.getProperty("os.name").lowercase().startsWith("win")

    if (!isOnWindows()) {
        print("Fortnite Lobby Music Player must be running on Windows.")
        exitProcess(0)
    }
}

fun loadFile(): BufferedImage = object{}.javaClass.classLoader.getResourceAsStream("Fortnite_Lobby_Test.jpg")
        ?.use { ImageIO.read(it) }
        ?: error("Test screenshot file does not exist.")

fun extractTessDatsa() {
    val tessDataDir = File(System.getProperty("user.home"), "tessdata")
    if (!tessDataDir.exists()) { 
        tessDataDir.mkdirs()
    }


}

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