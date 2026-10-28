package dev.naspo.fortnitelobbymusicplayer

import javazoom.jl.player.Player
import java.io.InputStream

fun playAudio(audioFileName: String) {
    // Getting InputStream of audio file.
    // Creating an empty object{} to access the classpath (as an object is required, and we are not in one).
    val inputStream: InputStream = object{}.javaClass.classLoader.getResourceAsStream(audioFileName)
        ?: error("Audio file $audioFileName not found.")

    // Playing audio...
    inputStream.use {
        val player = Player(it)
        println("Playing $audioFileName.")
        player.play()
    }
}