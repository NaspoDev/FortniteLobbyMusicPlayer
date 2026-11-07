package dev.naspo.fortnitelobbymusicplayer.image

import dev.naspo.fortnitelobbymusicplayer.gamestatepoller.GameState
import dev.naspo.fortnitelobbymusicplayer.gamestatepoller.GameStateSubscriber
import java.awt.Dimension
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.awt.image.BufferedImage

class ScreenCapturer: GameStateSubscriber {
    private val toolkit = Toolkit.getDefaultToolkit()
    /** Java's [Robot] object that we use to create screen captures. */
    private val robot = Robot()

    /** The size of the main screen as a [Dimension]. */
    private val screenSize: Dimension = toolkit.screenSize

    /** A rectangle representing the full screen. */
    private val fullScreenRectangle = Rectangle(screenSize)

    /**
     * The height of partial screen capture. It is a slice of the entire height of the screen.
     *
     * Partial screen captures only need to be 1/5 of the height of the screen. That is all we need for specific
     * captures, and importantly, it's much more efficient (less for the OCR engine to process).
     */
    private val heightSlice = screenSize.height / 5

    /** A rectangle representing a slim portion of the top of the screen. */
    private val topSliceRectangle = Rectangle(screenSize.width, heightSlice)

    /** A rectangle representing a slim portion of the bottom of the screen. */
    private val bottomSliceRectangle = Rectangle(
        0,
        screenSize.height - heightSlice,
        screenSize.width,
        heightSlice
    )

    /** The current game state. */
    private var gameState: GameState = GameState.UNKNOWN

    /**
     * Captures the relevant portion of the main screen.
     * Where the relevant portion refers to the part of the screen that would trigger a game state change
     * based on the current game state.
     *
     * If it is impossible to determine a single next relevant portion of the screen, then a full screen capture
     * will be performed.
     *
     * Capturing specific portions of the screen over the full thing are preferred as this is much more efficient.
     *
     * ### Example
     * If we're in the lobby, the only other game states that we can look for, other than unknown,
     * are triggered by text at the bottom of the screen, so we should take a screen capture of the bottom.
     */
    fun captureScreen(): BufferedImage {
        // The rectangle of our screen to capture.
        val rectangle: Rectangle = when (gameState) {
            GameState.UNKNOWN -> topSliceRectangle  // looking for lobby as the only next option
            GameState.LOBBY -> bottomSliceRectangle  // looking for shop or locker as the only next option
            GameState.SHOP -> fullScreenRectangle  // next option could be lobby or locker
            GameState.LOCKER -> fullScreenRectangle  // next option could be lobby or shop
        }

        return robot.createScreenCapture(rectangle)
    }

    override fun onStateChange(gameState: GameState) {
        this.gameState = gameState
    }
}