package dev.naspo.fortnitelobbymusicplayer.image

import dev.naspo.fortnitelobbymusicplayer.logger
import net.sourceforge.tess4j.Tesseract
import net.sourceforge.tess4j.TesseractException
import java.awt.image.BufferedImage
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteRecursively

// Logic for working with our OCR, Tesseract.

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
    AppPaths.tessDataDir.createDirectories()

    // Get the Tesseract trained data file bundled with the program from the classpath.
    // Creating an empty object{} to access the classpath (as an object is required, and we are not in one).
    val inputStream: InputStream = object{}.javaClass.classLoader.getResourceAsStream(AppPaths.TRAINED_DATA_FILE_RESOURCE_PATH)
        ?: error("Could not create InputStream for ${AppPaths.TRAINED_DATA_FILE_RESOURCE_PATH} from classpath.")

    // Write that to the disk in the tessdata directory.
    inputStream.use {
        Files.copy(
            it,
            AppPaths.tessDataDir.resolve(AppPaths.TRAINED_DATA_FILE_NAME),
            StandardCopyOption.REPLACE_EXISTING)
    }

    tesseract.setDatapath(AppPaths.tessDataDir.absolutePathString())
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
@OptIn(ExperimentalPathApi::class)  // deleteRecursively is experimental at the time of writing this.
fun runOCRCleanup() {
    logger.info { "Running OCR cleanup." }
    AppPaths.appDataDir.deleteRecursively()
}