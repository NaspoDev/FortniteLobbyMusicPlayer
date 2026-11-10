package dev.naspo.fortnitelobbymusicplayer.audio

import dev.naspo.fortnitelobbymusicplayer.gamestatepoller.GameState
import dev.naspo.fortnitelobbymusicplayer.gamestatepoller.GameStateSubscriber
import dev.naspo.fortnitelobbymusicplayer.logger
import java.net.URL
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

/**
 * Handles playing audio.
 *
 * @property audioTrack The [AudioTrack] associated with this AudioPlayer.
 */
class AudioPlayer(private val audioTrack: AudioTrack) : GameStateSubscriber {
    // Using Java's built-in audio playing system.
    // Using Clip, which loads the entire audio file into memory
    private val clip: Clip = AudioSystem.getClip()

    init {
        // Get audio file resource from classpath.
        val resource: URL = javaClass.classLoader.getResource(audioTrack.fileName)
            ?: error("Audio file ${audioTrack.fileName} not found.")

        // Setup Clip with AudioInputStream
        val audioStream = AudioSystem.getAudioInputStream(resource)
        clip.open(audioStream)
    }

    /**
     * Plays or pauses music playback.
     *
     * If the music is already playing, it will pause.
     * If the music is paused, it will play.
     */
    fun togglePlay() = if (clip.isRunning) pause() else play()

    /**
     * Play the audio track.
     * If previously paused, it will resume where it left off.
     */
    private fun play() {
        logger.info { "Playing ${audioTrack.readableName}..." }
        clip.start()  // start() is non-blocking
    }

    /**
     * Pause the audio track.
     */
    private fun pause() {
        clip.stop()
    }

    /**
     * Stops playing the audio track.
     * This operation resets playback to the start of the audio track.
     */
    private fun stop() {
        clip.stop()
        clip.framePosition = 0
    }

    override fun onStateChange(gameState: GameState) {
        when (gameState) {
            GameState.LOBBY -> play()
            GameState.UNKNOWN -> stop()
        }
    }
}

