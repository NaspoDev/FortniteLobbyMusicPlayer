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
    // The current game state.
    private var gameState: GameState = GameState.UNKNOWN

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
     * If the music is paused, it will play, as long as the current [GameState] is [GameState.LOBBY].
     */
    fun togglePlay() {
        if (clip.isRunning) {
            pause()
        } else {
            if (gameState == GameState.LOBBY) play()
        }
    }

    /**
     * Play the audio track.
     * If previously paused, it will resume where it left off.
     */
    private fun play() {
        logger.info { "Playing ${audioTrack.readableName}..." }
        clip.loop(Clip.LOOP_CONTINUOUSLY)
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

        /*
        flush() clears the buffer.
        Since we will reset the frame position back to 0 in our stop process, we call flush() to ensure a truly
        clean start when play() is called again.

        Before calling flush(), when audio was played again after stopping it and setting framePosition to 0,
        it didn't quite sound like it was playing from the very beginning, it sounded like it skipped a frame or two.
        Calling flush solved this issue.
         */
        clip.flush()

        // Reset the audio track to play from the start.
        clip.framePosition = 0
    }

    override fun onStateChange(gameState: GameState) {
        this.gameState = gameState
        when (gameState) {
            GameState.LOBBY -> play()
            GameState.UNKNOWN -> stop()
        }
    }
}

