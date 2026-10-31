package dev.naspo.fortnitelobbymusicplayer.image

import java.nio.file.Path
import kotlin.io.path.Path

/**
 * Object containing file paths used across the app.
 */
object AppPaths {
    private const val APP_DIRECTORY_NAME = "FortniteLobbyMusicPlayer"
    private val userHome: String get() = System.getProperty("user.home")

    /** The app's data directory (FortniteLobbyMusicPlayer/). Every file for the app is written in here. */
    val appDataDir: Path get() = Path(userHome, APP_DIRECTORY_NAME)

    /** Tesseract data directory. */
    val tessDataDir: Path get() = appDataDir.resolve("tessdata")

    /** The file name of the Tesseract trained data file. */
    const val TRAINED_DATA_FILE_NAME: String = "eng.traineddata"

    /** Classpath location of the Tesseract trained data file bundled in the app. */
    const val TRAINED_DATA_FILE_RESOURCE_PATH = "tessdata/$TRAINED_DATA_FILE_NAME"
}