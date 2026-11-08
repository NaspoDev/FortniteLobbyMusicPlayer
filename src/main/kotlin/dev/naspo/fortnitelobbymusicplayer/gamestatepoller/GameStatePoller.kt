package dev.naspo.fortnitelobbymusicplayer.gamestatepoller

import dev.naspo.fortnitelobbymusicplayer.image.OCREngine
import dev.naspo.fortnitelobbymusicplayer.image.ScreenCapturer
import dev.naspo.fortnitelobbymusicplayer.logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
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
    private val ocrEngine: OCREngine
) {

    private var currentGameState: GameState? = null
    private val gameStateSubscribers = mutableListOf<GameStateSubscriber>()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
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
    private suspend fun getCurrentGameState(): GameState? {
        val screenCaptures: List<BufferedImage> = ScreenCapturer.captureScreen()
        val ocrResult = ocrEngine.ocrAll(screenCaptures)

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
     * Broadcast that the game state has changed to all [gameStateSubscribers].
     */
    private fun broadcastGameStateChange(gameState: GameState) {
        gameStateSubscribers.forEach {
            it.onStateChange(gameState)
        }
    }
}