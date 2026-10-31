package dev.naspo.fortnitelobbymusicplayer.audio

import javazoom.jl.player.Player
import java.io.InputStream

/**
 * Play an audio track.
 */
fun playAudio(audioTrack: AudioTrack) {
    val fileName: String = audioTrack.fileName
    // Getting InputStream of audio file.
    // Creating an empty object{} to access the classpath (as an object is required, and we are not in one).
    val inputStream: InputStream = object{}.javaClass.classLoader.getResourceAsStream(fileName)
        ?: error("Audio file $fileName not found.")

    // Playing audio...
    inputStream.use {
        val player = Player(it)
        println("Playing ${audioTrack.readableName}...")
        player.play()
    }
}