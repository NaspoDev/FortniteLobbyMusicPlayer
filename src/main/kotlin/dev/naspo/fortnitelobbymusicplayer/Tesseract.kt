package dev.naspo.fortnitelobbymusicplayer

import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

/** File representing the `tessdata/` directory on disk. */
private val tessDataDir: File get() = File(System.getProperty("user.home"), "tessdata")
val test: Path

fun extractTessData() {
    // Get the tessdata/ bundled with the program from the classpath.
    val inputStream: InputStream = object{}.javaClass.classLoader.getResourceAsStream("tessdata")
        ?: error("Could not create InputStream for tessdata from classpath.")

    inputStream.use {
        Files.copy(it, tessDataDir.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }
}

private fun writeTessData() {
    // Create the tessdata/ dir
    createTessDataDir()

    // Write the tesseract data into tessdata/

}

// Creates the tessdata/ dir in the user home. (Overwrites it if already exists).
private fun createTessDataDir() {

    if (tessDataDir.exists()) {
        tessDataDir.delete()
    }
    tessDataDir.mkdirs()
}