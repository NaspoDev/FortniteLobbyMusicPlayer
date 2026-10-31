package dev.naspo.fortnitelobbymusicplayer.gamestatepoller

/**
 * A game state that is tracked by this application.
 */
enum class GameState {
    /** In the lobby menu. */
    LOBBY(),

    /** In the item shop. */
    SHOP(),

    /** In a game, i.e. playing the game. */
    IN_GAME()
}