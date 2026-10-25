package dev.naspo.fortnitelobbymusicplayer

import javazoom.jl.player.Player
import net.sourceforge.tess4j.Tesseract
import net.sourceforge.tess4j.TesseractException
import java.awt.image.BufferedImage
import java.io.File
import java.io.InputStream
import javax.imageio.ImageIO
import kotlin.jvm.javaClass

fun main() {
    val classicFortniteSoundtrackFileName = "Fortnite_Classic_Lobby_Music.mp3"
//    play(classicFortniteSoundtrackFileName)
    testOCR()
}

fun play(audioFileName: String) {
    // Getting InputStream of audio file.
    // Creating an empty object{} to access the classpath (as an object is required, and we are not in one).
    val inputStream: InputStream = object{}.javaClass.classLoader.getResourceAsStream(audioFileName)
        ?: error("Audio file $audioFileName not found.")

    // Playing audio...
    inputStream.use {
        val player = Player(it)
        println("Playing $audioFileName.")
        player.play()
    }
}

fun loadFile(): BufferedImage = object{}.javaClass.classLoader.getResourceAsStream("Fortnite_Lobby_Test.jpg")
        ?.use { ImageIO.read(it) }
        ?: error("Test screenshot file does not exist.")

fun extractTessData() {
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