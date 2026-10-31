package dev.naspo.fortnitelobbymusicplayer.gamestatepoller

import dev.naspo.fortnitelobbymusicplayer.image.OCREngine
import dev.naspo.fortnitelobbymusicplayer.image.ScreenCapturer
import dev.naspo.fortnitelobbymusicplayer.logger
import java.awt.image.BufferedImage

/**
 * Responsible for polling the live game state of Fortnite.
 */
object GameStatePoller {

    // The current game state of Fortnite.
    lateinit var currentGameState: GameState

    /**
     * Begin the polling process.
     *
     * This will repeatedly, in a fixed interval, get the live game state of Fortnite and update [currentGameState].
     * From there the required actions will be taken based on the current game state.
     */
    fun beingPolling() {

    }

    fun stopPolling() {

    }

    /**
     * Get the current live game state of Fortnite.
     *
     * This will go through the process of taking a screenshot of the current screen,
     * and using the OCR to analyze it to determine the current game state.
     */
    private fun getCurrentGameState(): GameState? {
        val screenCapture: BufferedImage = ScreenCapturer.captureScreen()
        val ocrResult = OCREngine.doOCR(screenCapture)

        ocrResult.fold(
            onSuccess = { return analyzeOCRResult(it) },
            onFailure = { e ->
                logger.error { "OCR failed! Unable to get current game state." }
                logger.error { e.message }
                return null
            }
        )
    }

    /**
     * Analyzes raw OCR result to determine the game state.
     */
    private fun analyzeOCRResult(ocrResult: String): GameState {

    }
}