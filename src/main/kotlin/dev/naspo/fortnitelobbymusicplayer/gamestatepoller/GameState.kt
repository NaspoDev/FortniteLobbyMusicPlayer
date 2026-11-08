package dev.naspo.fortnitelobbymusicplayer.gamestatepoller

/**
 * A game state that is tracked by this application.
 *
 * A game state is considered active when the screen text contains its [triggerText] and does not contain any of
 * its [forbiddenTriggerText]. How much of the trigger text must match is decided by the code that evaluates the
 * game states.
 *
 * ### Why [forbiddenTriggerText]?
 * During testing of our OCR, it was discovered that it cannot read certain text under certain circumstances.
 * One of these circumstances is the selected tab, as Fortnite puts a white blocky highlight around it. So funny
 * enough, a way to determine the current tab would be if the OCR cannot read it.
 *
 * @property triggerText A list of (lowercase) words or phrases that can act as the trigger for this game
 * state being active.
 * @property forbiddenTriggerText A list of (lowercase) words or phrases that must notably not be present in
 * order for this game state to be considered active.
 */
enum class GameState(
    val triggerText: List<String>,
    val forbiddenTriggerText: List<String>
) {
    /** In the lobby menu. */
    LOBBY(
        listOf("play", "shop", "locker", "passes", "quests", "compete", "career"),
        emptyList()
    ),

    /** In the item shop. */
    SHOP(
        listOf("play", "locker", "passes", "quests", "compete", "career", "support-a-creator"),
        listOf("shop")
    ),

    /** In the locker */
    LOCKER(
        listOf("play", "passes", "quests", "compete", "career", "save loadout", "view all loadouts"),
        listOf("locker")
    ),

    /** The exact state of the game is unknown/we don't care about it.  */
    UNKNOWN(emptyList(), emptyList())
}