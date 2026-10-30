package dev.naspo.fortnitelobbymusicplayer

import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists

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