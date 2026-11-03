package dev.naspo.fortnitelobbymusicplayer

import dev.naspo.fortnitelobbymusicplayer.audio.AudioPlayer
import dev.naspo.fortnitelobbymusicplayer.audio.AudioTrack
import dev.naspo.fortnitelobbymusicplayer.gamestatepoller.GameStatePoller
import dev.naspo.fortnitelobbymusicplayer.image.OCREngine
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.system.exitProcess

const val appName: String = "Fortnite Lobby Music Player"
val logger = KotlinLogging.logger { }

suspend fun main() {
    // App init
    performPlatformCheck()
    registerShutdownHook()
    OCREngine.initOCR()
    // Register GameStatePoller subscribers.
    GameStatePoller.subscribe(AudioPlayer(AudioTrack.FortniteOGClassic))

    logger.info { "$appName initialized!" }

    // Start
    GameStatePoller.beginPolling()
    GameStatePoller.awaitPolling()
}

// If not on Windows, terminate the program.
private fun performPlatformCheck() {
    fun isOnWindows() = System.getProperty("os.name").lowercase().startsWith("win")

    if (!isOnWindows()) {
        logger.error { "Fortnite Lobby Music Player must be running on Windows." }
        exitProcess(1)
    }
}

/**
 * Register's a shutdown hook to execute logic upon program shutdown.
 */
private fun registerShutdownHook() {
    Runtime.getRuntime().addShutdownHook(Thread {
        runBlocking {
            logger.info { "Terminating $appName..." }
            GameStatePoller.stopPollingAndWait()
            OCREngine.runOCRCleanup()
            logger.info { "$appName terminated. Goodbye!" }
        }
    })
}