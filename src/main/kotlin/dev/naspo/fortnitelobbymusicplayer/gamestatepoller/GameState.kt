package dev.naspo.fortnitelobbymusicplayer.gamestatepoller

/**
 * A game state that is tracked by this application.
 *
 * A game state is considered active by taking its [uniqueText], [supportingText], and [forbiddenText] into account.
 *
 * ### Why [forbiddenText]?
 * During testing of our OCR, it was discovered that it cannot read certain text under certain circumstances.
 * One of these circumstances is the selected tab, as Fortnite puts a white blocky highlight around it. So funny
 * enough, a way to determine the current tab would be if the OCR cannot read it.
 *
 * @property uniqueText A list of (lowercase) worlds or phrases which are unique to that game state (i.e. they
 * don't appear on any other screen).
 * @property supportingText A list of notable (lowercase) words or phrases that can act as supports to trigger
 * this game state being active. These words may appear on other screens, but could help with identification.
 * @property forbiddenText A list of (lowercase) words or phrases that must notably not be present in
 * order for this game state to be considered active.
 */
enum class GameState(
    val uniqueText: List<String>,
    val supportingText: List<String>,
    val forbiddenText: List<String>
) {
    /** In the lobby menu. */
    LOBBY(
        emptyList(),
        listOf("play", "shop", "locker", "passes", "quests", "compete", "career"),
        emptyList()
    ),

    /** In the item shop. */
    SHOP(
        listOf("support-a-creator", "about shop"),
        listOf("play", "locker", "passes", "quests", "compete", "career"),
        listOf("shop")
    ),

    /** In the locker */
    LOCKER(
        listOf("save loadout", "view all loadouts"),
        listOf("play", "shop", "passes", "quests", "compete", "career"),
        listOf("locker")
    ),

    /** The exact state of the game is unknown/we don't care about it.  */
    UNKNOWN(emptyList(), emptyList(), emptyList())
}