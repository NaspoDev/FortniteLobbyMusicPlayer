package dev.naspo.fortnitelobbymusicplayer.gamestatepoller

/**
 * A game state that is tracked by this application.
 *
 * @property triggerText A list of words or phrases that act as the trigger for this game state being active.
 */
enum class GameState(
    val triggerText: List<String>
) {
    /** In the lobby menu. */
    LOBBY(listOf("play", "shop", "locker")),

    /** In the item shop. */
    SHOP(listOf("support-a-creator", "about shop")),

    /** In the locker */
    LOCKER(listOf("save loadout", "view all loadouts")),

    /** The exact state of the game is unknown/we don't care about it.  */
    UNKNOWN(emptyList())
}