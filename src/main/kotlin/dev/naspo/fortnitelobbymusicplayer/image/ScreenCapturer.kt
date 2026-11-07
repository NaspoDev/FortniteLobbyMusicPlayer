package dev.naspo.fortnitelobbymusicplayer.image

import dev.naspo.fortnitelobbymusicplayer.gamestatepoller.GameState
import dev.naspo.fortnitelobbymusicplayer.gamestatepoller.GameStateSubscriber
import java.awt.Dimension
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.awt.image.BufferedImage

/**
 * Responsible for creating screen captures.
 */
object ScreenCapturer {
    private val toolkit = Toolkit.getDefaultToolkit()
    /** Java's [Robot] object that we use to create screen captures. */
    private val robot = Robot()

    /** The size of the main screen as a [Dimension]. */
    private val screenSize: Dimension = toolkit.screenSize

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

    /**
     * Captures the relevant portions of the main screen.
     * Where relevant portions refer to the parts of the screen that would trigger a game state change.
     *
     * So this would be a slim portion of the top and bottom of the screen.
     *
     * @return A list of [BufferedImage].
     */
    fun captureScreen(): List<BufferedImage> {
        return listOf(
            robot.createScreenCapture(topSliceRectangle),
            robot.createScreenCapture(bottomSliceRectangle)
        )
    }
}