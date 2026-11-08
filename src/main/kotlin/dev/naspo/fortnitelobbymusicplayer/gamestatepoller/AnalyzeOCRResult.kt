package dev.naspo.fortnitelobbymusicplayer.gamestatepoller

import dev.naspo.fortnitelobbymusicplayer.logger

// Analyze raw OCR result to determine a game state.

/**
 * There must be at least this many of a [GameState]'s trigger words present in an OCR result to pass
 * the trigger word check.
 */
private const val TRIGGER_TEXT_MATCH_THRESHOLD = 3

/**
 * Analyzes raw OCR result to determine the game state.
 */
fun analyzeOCRResult(ocrResult: String): GameState {
    val ocrResult = ocrResult.lowercase()
    logger.info {"OCR Result: \n$ocrResult"}

    // Note: Lobby should be checked last as SHOP and LOCKER are "substates" of LOBBY.
    return when {
        canConsiderGameStateActive(GameState.SHOP, ocrResult) -> GameState.SHOP
        canConsiderGameStateActive(GameState.LOCKER, ocrResult) -> GameState.LOCKER
        canConsiderGameStateActive(GameState.LOBBY, ocrResult) -> GameState.LOBBY
        else -> GameState.UNKNOWN
    }
}

/**
 * Returns true if the provided [GameState] can be considered active.
 *
 * @param gameState The [GameState] to check.
 * @param ocrResult The raw OCR result.
 */
private fun canConsiderGameStateActive(gameState: GameState, ocrResult: String): Boolean {
    // TODO: This is temporary edge-case logic for SHOP game state check. Move it elsewhere
    if (gameState == GameState.SHOP) {
        if (!passesTriggerTextCheck(gameState, ocrResult)) return false

        if (passesForbiddenTriggerTextCheck(gameState, ocrResult)) {
            return true
        } else if ("about shop" in ocrResult || "support-a-creator" in ocrResult || "aboutshop" in ocrResult) {
            return true
        } else {
            return false
        }
    } else {
        // Regular check logic
        return passesTriggerTextCheck(gameState, ocrResult) && passesForbiddenTriggerTextCheck(gameState, ocrResult)
    }
}

/**
 * Returns true if the provided [GameState]'s trigger text appears at least [TRIGGER_TEXT_MATCH_THRESHOLD]
 * in the OCR result.
 *
 * @param gameState The [GameState] to check.
 * @param ocrResult The raw OCR result.
 */
private fun passesTriggerTextCheck(gameState: GameState, ocrResult: String): Boolean =
    gameState.triggerText.count { it in ocrResult } >= TRIGGER_TEXT_MATCH_THRESHOLD


/**
 * Returns true if none of the provided [GameState]'s forbidden trigger text appears in the OCR result.
 *
 * @param gameState The [GameState] to check.
 * @param ocrResult The raw OCR result.
 */
private fun passesForbiddenTriggerTextCheck(gameState: GameState, ocrResult: String): Boolean =
    gameState.forbiddenTriggerText.none { it in ocrResult }