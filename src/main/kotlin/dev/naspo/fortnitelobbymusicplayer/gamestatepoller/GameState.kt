package dev.naspo.fortnitelobbymusicplayer.gamestatepoller

/**
 * A game state that is tracked by this application.
 *
 * (Currently only the lobby is taken into account).
 *
 * @property triggerText A list of (lowercase) words or phrases that act as triggers for a game state being active.
 * These would be text that appears on screen when the game state is active.
 */
enum class GameState(
    val triggerText: List<String>
) {
    /** In the lobby. */
    LOBBY(
        listOf("play", "shop", "locker", "passes", "quests", "compete", "career", "chat", "emote", "back")
    ),

    /** The exact state of the game is unknown/we don't care about it.  */
    UNKNOWN(emptyList())
}