package dev.naspo.fortnitelobbymusicplayer.audio

import dev.naspo.fortnitelobbymusicplayer.gamestatepoller.GameState
import dev.naspo.fortnitelobbymusicplayer.gamestatepoller.GameStateSubscriber
import dev.naspo.fortnitelobbymusicplayer.logger
import java.io.InputStream
import java.net.URL

/**
 * Handles playing audio.
 *
 * @property audioTrack The [AudioTrack] associated with this AudioPlayer.
 */
class AudioPlayer(private val audioTrack: AudioTrack) : GameStateSubscriber {
    // Get audio file resource from classpath.
    private val resource: URL = javaClass.classLoader.getResource(audioTrack.fileName)
        ?: error("Audio file ${audioTrack.fileName} not found.")

    // MediaPlayer
    private val media = Media(resource.toExternalForm())
    private val mediaPlayer = MediaPlayer(media)

    /**
     * Play the audio track.
     * If previously paused, it will resume where it left off.
     */
    fun play() {
        logger.info { "Playing ${audioTrack.readableName}..." }
        mediaPlayer.play()  // play() is non-blocking
    }

    /**
     * Pause the audio track.
     */
    fun pause() {
        mediaPlayer.pause()
    }

    /**
     * Stops playing the audio track.
     * This operation resets playback to the start of the audio track.
     */
    fun stop() {
        mediaPlayer.stop()
    }

    override fun onStateChange(gameState: GameState) {
        when (gameState) {
            GameState.LOBBY -> play()
            GameState.SHOP -> pause()
            GameState.UNKNOWN -> stop()
        }
    }
}

