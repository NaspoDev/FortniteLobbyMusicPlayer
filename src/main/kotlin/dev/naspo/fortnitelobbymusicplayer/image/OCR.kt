package dev.naspo.fortnitelobbymusicplayer.image

import dev.naspo.fortnitelobbymusicplayer.logger
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
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteIfExists

// Logic for working with our OCR, Tesseract.

/** File representing the `tessdata/` directory on disk. */
private val tessDataDir: Path get() = Path(System.getProperty("user.home"), "FortniteLobbyMusicPlayer", "tessdata")
private const val TRAINED_DATA_FILE_NAME = "eng.traineddata"

val tesseract = Tesseract()

/**
 * Initialize the OCR to prepare it for use.
 *
 * This will write the OCR data that Tesseract needs to perform its duties to disk.
 * This data is bundled with the application, but Tesseract needs to physically read from disk, hence why we do this.
 *
 * You can call [runOCRCleanup] when done with the OCR to delete this data from disk.
 */
fun initOCR() {
    tessDataDir.createDirectories()

    // Get the Tesseract trained data file bundled with the program from the classpath.
    // Creating an empty object{} to access the classpath (as an object is required, and we are not in one).
    val inputStream: InputStream = object{}.javaClass.classLoader.getResourceAsStream("tessdata/$TRAINED_DATA_FILE_NAME")
        ?: error("Could not create InputStream for tessdata/$TRAINED_DATA_FILE_NAME from classpath.")

    // Write that to the disk in the tessdata directory.
    inputStream.use {
        Files.copy(it, tessDataDir.resolve(TRAINED_DATA_FILE_NAME), StandardCopyOption.REPLACE_EXISTING)
    }

    tesseract.setDatapath(tessDataDir.absolutePathString())
    logger.info { "OCR initialized." }
}

/**
 * Perform OCR operation on the provided image.
 *
 * @return A [Result] object containing either the successful result as a String, or an error.
 */
fun doOCR(image: BufferedImage): Result<String> {
    return try {
        Result.success(tesseract.doOCR(image))
    } catch (e: TesseractException) {
        Result.failure(e)
    }
}

/**
 * Delete the temporary OCR data files written to the disk.
 */
fun runOCRCleanup() {
    logger.info { "Running OCR cleanup." }
    // TODO: this doesnt work. I think it can only delete the dir if its empty.
    tessDataDir.deleteIfExists()
}