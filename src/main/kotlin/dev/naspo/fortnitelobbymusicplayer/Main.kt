package dev.naspo.fortnitelobbymusicplayer

import javazoom.jl.player.Player
import java.io.InputStream

fun main() {
    val mp3FileName = "Fortnite_Classic_Lobby_Music.mp3"
    try {
        val stream: InputStream = object{}.javaClass.classLoader.getResourceAsStream(mp3FileName)
            ?: error("Audio file not found.")

        stream.use {
            val player = Player(it)
            println("Playing...")
            player.play()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}