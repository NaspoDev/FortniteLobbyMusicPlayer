package dev.naspo.fortnitelobbymusicplayer

import dev.naspo.fortnitelobbymusicplayer.audio.AudioPlayer
import dev.naspo.fortnitelobbymusicplayer.audio.AudioTrack
import dev.naspo.fortnitelobbymusicplayer.image.OCREngine
import io.github.oshai.kotlinlogging.KotlinLogging
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.system.exitProcess

const val appName: String = "Fortnite Lobby Music Player"
val logger = KotlinLogging.logger { }

fun main() {
    // App init
    performPlatformCheck()
    registerShutdownHook()
    OCREngine.initOCR()
    logger.info { "$appName initialized!" }

    // TODO: GameStatePoller.beginPolling()

    // testing
    val audioPlayer = AudioPlayer(AudioTrack.FortniteOGClassic)
    audioPlayer.play()
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
        logger.info { "Terminating $appName..." }
        OCREngine.runOCRCleanup()
        logger.info { "$appName terminated. Goodbye!" }
    })
}

// TODO: temporary
fun loadFile(): BufferedImage = object{}.javaClass.classLoader.getResourceAsStream("Fortnite_Lobby_Test.jpg")
    ?.use { ImageIO.read(it) }
    ?: error("Test screenshot file does not exist.")