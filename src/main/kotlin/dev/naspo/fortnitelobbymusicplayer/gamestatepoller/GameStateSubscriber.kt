package dev.naspo.fortnitelobbymusicplayer.gamestatepoller

interface GameStateSubscriber {

    /**
     * Called when the game state changes, providing the updated game state.
     */
    fun onStateChange(gameState: GameState)
}