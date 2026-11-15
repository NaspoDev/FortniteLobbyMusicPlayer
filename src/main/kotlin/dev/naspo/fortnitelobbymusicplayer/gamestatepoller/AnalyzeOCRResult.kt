package dev.naspo.fortnitelobbymusicplayer.gamestatepoller

import dev.naspo.fortnitelobbymusicplayer.logger

// Analyze raw OCR result to determine a game state.

/**
 * Analyzes a raw OCR result to determine the [GameState].
 */
fun analyzeOCRResult(ocrResult: String): GameState {
    val ocrResult = ocrResult.lowercase()
    logger.info {"OCR Result: \n$ocrResult"}  // TODO: remove this when done debugging

    return when {
        passesTriggerTextCheck(GameState.LOBBY, ocrResult) -> GameState.LOBBY
        passesTriggerTextCheck(GameState.CONNECTING_TO_GAME, ocrResult) -> GameState.CONNECTING_TO_GAME
        else -> GameState.UNKNOWN
    }
}

/**
 * Returns true if the provided [GameState]'s trigger text appears the required amount of times
 * in the OCR result.
 *
 * This amount of trigger text that must be present in the OCR result, (the threshold), is
 * determined by the Game State's [GameState.matchThreshold].
 *
 * @param gameState The [GameState] to check.
 * @param ocrResult The raw OCR result.
 */
private fun passesTriggerTextCheck(gameState: GameState, ocrResult: String): Boolean {
    logger.info { "In passesTriggerTextCheck() for $gameState" }
    val temp = gameState.triggerText.count { it in ocrResult } >= gameState.matchThreshold
    logger.info { "Passed: $temp" }
    return temp
}