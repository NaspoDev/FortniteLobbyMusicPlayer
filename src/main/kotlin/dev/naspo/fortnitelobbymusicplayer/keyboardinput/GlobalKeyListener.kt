package dev.naspo.fortnitelobbymusicplayer.keyboardinput

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import dev.naspo.fortnitelobbymusicplayer.audio.AudioPlayer

class GlobalKeyListener(
    val audioPlayer: AudioPlayer
): NativeKeyListener {

    /**
     * The integer key code for the PLAY keyboard input. (Used for playing and pausing music).
     */
    private val playKeyCode = 57378

    override fun nativeKeyReleased(nativeEvent: NativeKeyEvent) {
        // If the PLAY key was released, toggle audio playback.
        if (nativeEvent.keyCode == playKeyCode) audioPlayer.togglePlay()
    }
}