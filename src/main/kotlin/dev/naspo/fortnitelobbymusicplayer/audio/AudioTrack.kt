package dev.naspo.fortnitelobbymusicplayer.audio

/**
 * An available audio track, bundled with the application.
 *
 * @property readableName - Human readable name of the audio track.
 * @property fileName - The name of the audio file, including the file extension.
 */
enum class AudioTrack(val readableName: String, val fileName: String) {
    FortniteOGClassic("Fortnite OG Classic", "Fortnite_OG_Classic.mp3")
}