package dev.naspo.fortnitelobbymusicplayer

import dev.naspo.fortnitelobbymusicplayer.image.doOCR
import dev.naspo.fortnitelobbymusicplayer.image.initOCR
import dev.naspo.fortnitelobbymusicplayer.image.runOCRCleanup
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
    initOCR()
    logger.info { "$appName initialized!" }

    // TODO: Temp
    val result = doOCR(loadFile())

    result.fold(
        onSuccess = { println(it) },
        onFailure = {
            logger.error { "Error performing OCR!" }
            logger.error { result.exceptionOrNull()?.message }
        }
    )
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
        runOCRCleanup()
        logger.info { "$appName terminated. Goodbye!" }
    })
}

// TODO: temporary
fun loadFile(): BufferedImage = object{}.javaClass.classLoader.getResourceAsStream("Fortnite_Lobby_Test.jpg")
    ?.use { ImageIO.read(it) }
    ?: error("Test screenshot file does not exist.")