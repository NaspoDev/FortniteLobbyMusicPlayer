package dev.naspo.fortnitelobbymusicplayer.image

import java.awt.Dimension
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.awt.image.BufferedImage

object ScreenCapturer {
    private val toolkit = Toolkit.getDefaultToolkit()
    private val screenSize: Dimension = toolkit.screenSize
    private val rectangle = Rectangle(screenSize)
    private val robot = Robot()

    /**
     * Captures a full screenshot of the main screen.
     */
    fun captureScreen(): BufferedImage {
        return robot.createScreenCapture(rectangle)
    }
}