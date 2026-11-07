package dev.naspo.fortnitelobbymusicplayer.gamestatepoller

import dev.naspo.fortnitelobbymusicplayer.image.OCREngine
import dev.naspo.fortnitelobbymusicplayer.image.ScreenCapturer
import dev.naspo.fortnitelobbymusicplayer.logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.awt.image.BufferedImage
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Responsible for polling the live game state of Fortnite.
 */
class GameStatePoller(
    private val screenCapturer: ScreenCapturer
) {

    private var currentGameState: GameState? = null
    private val gameStateSubscribers = mutableListOf<GameStateSubscriber>()
    private val scope = CoroutineScope(Dispatchers.Default)
    private var pollingJob: Job? = null
    /** How often the game state should be polled. */
    private val pollingRate: Duration = 1.seconds

    /**
     * Subscribes a list of [GameStateSubscriber] to receive game state updates via [GameStateSubscriber.onStateChange].
     */
    fun subscribe(subscribers: List<GameStateSubscriber>) {
        subscribers.forEach { gameStateSubscribers.add(it) }
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
                    logger.info { "Game state has changed to $state." }
                    currentGameState = state
                    broadcastGameStateChange(state)
                }
                delay(pollingRate)
            }
        }
    }

    /**
     * Suspends until the polling job is finished.
     */
    suspend fun awaitPolling() {
        pollingJob?.join()
    }

    /**
     * Stops the polling process.
     */
    fun stopPolling() {
        pollingJob?.cancel()
    }

    /**
     * Stops the polling process and suspends until the process finishes cleaning up.
     */
    suspend fun stopPollingAndWait() {
        pollingJob?.cancelAndJoin()
    }

    /**
     * Get the current live game state of Fortnite.
     *
     * This will go through the process of taking a screenshot of the current screen,
     * and using the OCR to analyze it to determine the current game state.
     */
    private fun getCurrentGameState(): GameState? {
        val screenCapture: BufferedImage = screenCapturer.captureScreen()
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
    internal fun analyzeOCRResult(ocrResult: String): GameState {
        val text = ocrResult.lowercase()

        fun hasAll(vararg words: String) = words.all {it in text}

        // If it doesn't have any of the main navbar lobby text, then return UNKNOWN.
        if (!hasAll("play", "shop", "locker")) return GameState.UNKNOWN

        // Otherwise we are in the lobby, but check if we are in a specific subscreen, like shop or locker.
        return when {
            hasAll("support-a-creator", "about shop") -> GameState.SHOP
            hasAll("save loadout", "view all loadouts") -> GameState.LOCKER
            // If its none of the specific lobby subscreens, then return LOBBY.
            else -> GameState.LOBBY
        }
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