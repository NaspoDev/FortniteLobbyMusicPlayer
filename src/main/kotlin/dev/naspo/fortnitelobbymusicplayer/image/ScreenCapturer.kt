package dev.naspo.fortnitelobbymusicplayer.image

import java.awt.Dimension
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.io.path.Path

object ScreenCapturer {
    private val toolkit = Toolkit.getDefaultToolkit()
    private val screenSize: Dimension = toolkit.screenSize
    // The height of our screen captures.
    // Screen captures should only capture 1/5 of the height of the screen as that's all we need at any
    // given moment. And importantly, it's much more efficient (less for the OCR engine to process).
    private val heightSlice = screenSize.height / 5
    // A rectangle representing the top slice of the screen.
    private val topSliceRectangle = Rectangle(screenSize.width, heightSlice)
    // A rectangle representing the bottom slice of the screen.
    private val bottomSliceRectangle = Rectangle(
        0,
        screenSize.height - heightSlice,
        screenSize.width,
        heightSlice
    )
    private val robot = Robot()

    /**
     * Captures a full screenshot of the main screen.
     */
    fun captureScreen(): BufferedImage {
        return robot.createScreenCapture(rectangle)
    }
}