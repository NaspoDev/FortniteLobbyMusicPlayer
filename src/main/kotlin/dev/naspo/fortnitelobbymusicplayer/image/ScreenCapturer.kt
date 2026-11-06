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
    // Capture only the top 1/5 of the screen as that's all we need, and it's much more efficient. (Less
    // for the OCR engine to process).
    private val rectangle = Rectangle(Dimension(screenSize.width, screenSize.height / 5))
    private val robot = Robot()

    /**
     * Captures a full screenshot of the main screen.
     */
    fun captureScreen(): BufferedImage {
        return robot.createScreenCapture(rectangle)
    }
}