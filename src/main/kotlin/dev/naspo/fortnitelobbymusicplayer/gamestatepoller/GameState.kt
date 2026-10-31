package dev.naspo.fortnitelobbymusicplayer.gamestatepoller

/**
 * A game state that is tracked by this application.
 */
enum class GameState {
    /** In the lobby menu. */
    LOBBY(),

    /** In the item shop. */
    SHOP(),

    /** The exact state of the game is unknown/we don't care about it.  */
    UNKNOWN()
}