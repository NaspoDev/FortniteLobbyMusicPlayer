package dev.naspo.fortnitelobbymusicplayer.gamestatepoller

import dev.naspo.fortnitelobbymusicplayer.image.OCREngine
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.test.assertEquals

class GameStatePollerTests {

    companion object {
        @JvmStatic
        @AfterAll
        fun afterAll() {
            OCREngine.runOCRCleanup()
        }
    }

    @BeforeEach
    fun setUp() {
        OCREngine.initOCR()
    }

    @Test
    fun `test analyzeOCRResult LOBBY`() {
        // Load image
        val lockerImage: BufferedImage = loadImage("Fortnite_Lobby.jpg")

        // Do OCR
        val ocrResult: String = OCREngine.doOCR(lockerImage).getOrThrow()

        // Analyze results
        assertEquals(GameState.LOBBY, GameStatePoller.analyzeOCRResult(ocrResult))
    }

    // Utility method to load an image from the classpath. Used to load test images.
    private fun loadImage(fileName: String): BufferedImage = object{}.javaClass.classLoader.getResourceAsStream(fileName)
        ?.use { ImageIO.read(it) }
        ?: error("Test screenshot file does not exist.")
}