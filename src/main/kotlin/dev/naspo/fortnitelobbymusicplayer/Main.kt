package dev.naspo.fortnitelobbymusicplayer

import dev.naspo.fortnitelobbymusicplayer.audio.AudioPlayer
import dev.naspo.fortnitelobbymusicplayer.audio.AudioTrack
import dev.naspo.fortnitelobbymusicplayer.gamestatepoller.GameStatePoller
import dev.naspo.fortnitelobbymusicplayer.image.OCREngine
import dev.naspo.fortnitelobbymusicplayer.image.ScreenCapturer
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.system.exitProcess

const val appName: String = "Fortnite Lobby Music Player"
val logger = KotlinLogging.logger { }

suspend fun main() {
    performPlatformCheck()

    val ocrEngine = OCREngine()
    val screenCapturer = ScreenCapturer()
    val gameStatePoller = GameStatePoller(screenCapturer)

    registerShutdownHook(ocrEngine, gameStatePoller)

    // Register GameStatePoller subscribers.
    gameStatePoller.subscribe(listOf(
        screenCapturer,
        AudioPlayer(AudioTrack.FortniteOGClassic)
    ))

    logger.info { "$appName initialized!" }

    // Start
    gameStatePoller.beginPolling()
    gameStatePoller.awaitPolling()
}

// If not on Windows, terminate the program.
private fun performPlatformCheck() {
    fun isOnWindows() = System.getProperty("os.name").lowercase().startsWith("win")

    if (!isOnWindows()) {
        logger.error { "$appName must be running on Windows." }
        exitProcess(1)
    }
}

/**
 * Register's a shutdown hook to execute logic upon program shutdown.
 */
private fun registerShutdownHook(ocrEngine: OCREngine, gameStatePoller: GameStatePoller) {
    Runtime.getRuntime().addShutdownHook(Thread {
        runBlocking {
            logger.info { "Terminating $appName..." }
            gameStatePoller.stopPollingAndWait()
            ocrEngine.close()
            logger.info { "$appName terminated. Goodbye!" }
        }
    })
}