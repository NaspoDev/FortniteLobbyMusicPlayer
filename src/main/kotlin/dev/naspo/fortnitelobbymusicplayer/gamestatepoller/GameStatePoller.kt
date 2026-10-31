package dev.naspo.fortnitelobbymusicplayer.gamestatepoller

import dev.naspo.fortnitelobbymusicplayer.image.OCREngine
import dev.naspo.fortnitelobbymusicplayer.image.ScreenCapturer
import dev.naspo.fortnitelobbymusicplayer.logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.awt.image.BufferedImage
import kotlin.time.Duration.Companion.seconds

/**
 * Responsible for polling the live game state of Fortnite.
 */
object GameStatePoller {

    private var currentGameState: GameState? = null
    private var gameStateSubscribers = mutableListOf<GameStateSubscriber>()
    private val scope = CoroutineScope(Dispatchers.Default)
    private var pollingJob: Job? = null

    /**
     * Subscribes a [GameStateSubscriber] to receive game state updates via [GameStateSubscriber.onStateChange].
     */
    fun subscribe(subscriber: GameStateSubscriber) {
        gameStateSubscribers.add(subscriber)
    }

    /**
     * Begin the game state polling process.
     *
     * This will repeatedly, in a fixed interval, get the live game state of Fortnite.
     * When the game state changes, a [GameStateSubscriber.onStateChange] event will be sent out
     * to all subscribed [GameStateSubscriber]s.
     *
     * ([GameStateSubscriber]s can subscribe with [GameStatePoller.subscribe]).
     */
    fun beginPolling() {
        if (pollingJob?.isActive == true) return

        // Polling work is done in one coroutine. The logic sits inside a loop and will repeat as long
        // as the job is active. At the end of each loop iteration, a delay is applied so that this action
        // is only performed as often as necessary.
        pollingJob = scope.launch {
            while (isActive) {
                val state = getCurrentGameState()
                if (state != null && state != currentGameState) {
                    currentGameState = state
                    broadcastGameStateChange(state)
                }
                delay(3.seconds)
            }
        }
    }

    fun stopPolling() {
        pollingJob?.cancel()
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
        val ocrResult = ocrResult.lowercase()
        var gameState = GameState.UNKNOWN

        // Lobby check
        if (ocrResult.contains("play")
            && ocrResult.contains("shop")
            && ocrResult.contains("locker")) {
            gameState = GameState.LOBBY

            // Check if we are in the shop.
            if (ocrResult.contains("support-a-creator") && ocrResult.contains("about shop")) {
                gameState = GameState.SHOP
            }
        } else {
            gameState = GameState.UNKNOWN
        }

        return gameState
    }

    /**
     * Broadcast that the game state has changed to all [gameStateSubscribers].
     */
    private fun broadcastGameStateChange(gameState: GameState) {
        gameStateSubscribers.forEach {
            it.onStateChange(gameState)
        }
    }
}