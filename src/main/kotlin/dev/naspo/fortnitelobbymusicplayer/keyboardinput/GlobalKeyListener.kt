package dev.naspo.fortnitelobbymusicplayer.keyboardinput

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener

class GlobalKeyListener: NativeKeyListener {

    /**
     * The integer key code for the PLAY keyboard input. (Used for playing and pausing music).
     */
    private val PLAY_KEY_CODE = 57378

    override fun nativeKeyReleased(nativeEvent: NativeKeyEvent) {
        if (nativeEvent.keyCode == PLAY_KEY_CODE) {

        }
    }
}