package dev.naspo.fortnitelobbymusicplayer.image

import dev.naspo.fortnitelobbymusicplayer.logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.sourceforge.tess4j.Tesseract
import net.sourceforge.tess4j.TesseractException
import java.awt.image.BufferedImage
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteRecursively

/**
 * Facade for interacting with our OCR engine, Tesseract.
 *
 * Constructing an instance writes the bundled OCR data to disk so Tesseract can read it from disk.
 * Call [close] when done to delete that data.
 */
class OCREngine: AutoCloseable {
    /**
     * Initializes the OCR to prepare it for use.
     *
     * This will write the OCR data that Tesseract needs to perform its duties to disk.
     * This data is bundled with the application, but Tesseract needs to physically read from disk, hence why we do this.
     */
    init {
        AppPaths.tessDataDir.createDirectories()

        // Get the Tesseract trained data file bundled with the program from the classpath.
        val inputStream: InputStream = javaClass.classLoader.getResourceAsStream(AppPaths.TRAINED_DATA_FILE_RESOURCE_PATH)
            ?: error("Could not create InputStream for ${AppPaths.TRAINED_DATA_FILE_RESOURCE_PATH} from classpath.")

        // Write that to the disk in the tessdata directory.
        inputStream.use {
            Files.copy(
                it,
                AppPaths.tessDataDir.resolve(AppPaths.TRAINED_DATA_FILE_NAME),
                StandardCopyOption.REPLACE_EXISTING)
        }

        logger.info { "OCR initialized." }
    }

    /**
     * Perform OCR operation on the provided image.
     *
     * @return A [Result] object containing either the successful result as a String, or an error.
     */
    suspend fun doOCR(image: BufferedImage): Result<String> = withContext(Dispatchers.Default) {
        try {
            // Since a Tesseract instance on its own is not thread-safe, we need to create a new instance
            // per OCR operation to achieve thread-safety.
            val tesseract: Tesseract = newTesseract()
            Result.success(tesseract.doOCR(image))
        } catch (e: TesseractException) {
            Result.failure(e)
        }
    }

    /**
     * Performs OCR on a list of images and returns the results as a single joint string.
     *
     * @return The joined OCR results of all the images, separated by a space ' ' in the resulting string.
     */
    suspend fun ocrAll(images: List<BufferedImage>): Result<String> = coroutineScope {
        val ocrResults: List<Result<String>> = images.map {
            async { doOCR(it) }
        }.awaitAll()

        val filteredResults: List<String> = ocrResults.map { result ->
            result.getOrElse { return@coroutineScope Result.failure(it) }
        }

        Result.success(filteredResults.joinToString(" "))
    }

    /**
     * Initializes and configures a new [Tesseract] instance for use.
     */
    private fun newTesseract(): Tesseract {
        val instance = Tesseract()
        instance.setDatapath(AppPaths.tessDataDir.absolutePathString())
        return instance
    }

    /**
     * Delete the temporary OCR data files written to the disk.
     */
    @OptIn(ExperimentalPathApi::class)  // deleteRecursively is experimental at the time of writing this.
    override fun close() {
        logger.info { "Running OCR cleanup." }
        AppPaths.appDataDir.deleteRecursively()
    }
}

