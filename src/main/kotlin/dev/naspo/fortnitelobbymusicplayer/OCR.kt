package dev.naspo.fortnitelobbymusicplayer

import net.sourceforge.tess4j.Tesseract
import net.sourceforge.tess4j.TesseractException
import java.awt.image.BufferedImage
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import javax.imageio.ImageIO
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.deleteIfExists
import kotlin.io.path.name

// Logic for working with our OCR, Tesseract.

/** File representing the `tessdata/` directory on disk. */
private val tessDataDir: Path get() = Path(System.getProperty("user.home"), "tessdata")

/**
 * Initialize the OCR to prepare it for use.
 *
 * This will write the OCR data that Tesseract needs to perform its duties to disk.
 * This data is bundled with the application, but Tesseract needs to physically read from disk, hence why we do this.
 *
 * You can call [runOCRCleanup] when done with the OCR to delete this data from disk.
 */
fun initOCR() {
    // Get the tessdata/ bundled with the program from the classpath.
    // Creating an empty object{} to access the classpath (as an object is required, and we are not in one).
    val inputStream: InputStream = object{}.javaClass.classLoader.getResourceAsStream("tessdata")
        ?: error("Could not create InputStream for tessdata from classpath.")

    // Write that to the disk
    inputStream.use {
        Files.copy(it, tessDataDir, StandardCopyOption.REPLACE_EXISTING)
    }
}

/**
 * Delete the temporary OCR data files written to the disk.
 */
fun runOCRCleanup() {
    tessDataDir.deleteIfExists()
}

// TEMP - TEST OCR

fun loadFile(): BufferedImage = object{}.javaClass.classLoader.getResourceAsStream("Fortnite_Lobby_Test.jpg")
    ?.use { ImageIO.read(it) }
    ?: error("Test screenshot file does not exist.")

fun testOCR() {
    logger.info { "before we test OCR here is the path: ${tessDataDir.absolutePathString()}" }
//    val tesseract = Tesseract()
//    tesseract.setDatapath(tessDataDir.name)
//
//    try {
//        val result: String = tesseract.doOCR(loadFile())
//        println(result)
//    } catch (e: TesseractException) {
//        println(e.message)
//    }
}