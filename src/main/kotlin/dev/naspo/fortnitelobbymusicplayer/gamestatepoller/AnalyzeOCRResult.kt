package dev.naspo.fortnitelobbymusicplayer.gamestatepoller

import dev.naspo.fortnitelobbymusicplayer.logger

// Analyze raw OCR result to determine a game state.

/**
 * There must be at least this many of a [GameState]'s supporting words present in an OCR result to pass
 * the supporting text check.
 */
private const val SUPPORTING_TEXT_MATCH_THRESHOLD = 3

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
    if (hasUniqueText(gameState, ocrResult)) return true
    return passesSupportingTextCheck(gameState, ocrResult) && passesForbiddenTextCheck(gameState, ocrResult)
}

/**
 * Returns true if at least of the entries in [GameState.uniqueText] appears in the [ocrResult].
 *
 * @param gameState The [GameState] to check.
 * @param ocrResult The raw OCR result.
 */
private fun hasUniqueText(gameState: GameState, ocrResult: String): Boolean =
    gameState.uniqueText.any { it in ocrResult }

/**
 * Returns true if the provided [GameState]'s supporting text appears at least [SUPPORTING_TEXT_MATCH_THRESHOLD]
 * in the OCR result.
 *
 * @param gameState The [GameState] to check.
 * @param ocrResult The raw OCR result.
 */
private fun passesSupportingTextCheck(gameState: GameState, ocrResult: String): Boolean =
    gameState.supportingText.count { it in ocrResult } >= SUPPORTING_TEXT_MATCH_THRESHOLD


/**
 * Returns true if none of the provided [GameState]'s forbidden text appears in the OCR result.
 *
 * @param gameState The [GameState] to check.
 * @param ocrResult The raw OCR result.
 */
private fun passesForbiddenTextCheck(gameState: GameState, ocrResult: String): Boolean =
    gameState.forbiddenText.none { it in ocrResult }