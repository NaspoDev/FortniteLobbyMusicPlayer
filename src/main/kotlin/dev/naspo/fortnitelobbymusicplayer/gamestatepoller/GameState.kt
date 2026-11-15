package dev.naspo.fortnitelobbymusicplayer.gamestatepoller

/**
 * A game state that is tracked by this application.
 *
 * @property triggerText A list of (lowercase) words or phrases that act as triggers for a game state being active.
 * This would be text that appears on screen when the game state is active.
 * @property matchThreshold The amount of a GameState's [triggerText] that is required to be present on the screen
 * in order for this Game State of be considered active.
 */
enum class GameState(
    val triggerText: List<String>,
    val matchThreshold: Int
) {
    /** In the lobby. */
    LOBBY(
        listOf(
            // Main lobby screen
            "play", "shop", "locker", "passes", "quests", "compete", "career", "chat", "emote", "back",
            // Shop
            "support-a-creator", "about shop",
            // Game mode selection grid
            "custom key", "go to lobby", "back to top"
        ),
        3
    ),

    /** Connecting to a game. */
    CONNECTING_TO_GAME(listOf("creator -", "connecting"), 1),

    /** The exact state of the game is unknown/we don't care about it.  */
    UNKNOWN(emptyList(), 0)
}