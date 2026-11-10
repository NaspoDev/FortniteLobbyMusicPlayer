package dev.naspo.fortnitelobbymusicplayer.gamestatepoller

import dev.naspo.fortnitelobbymusicplayer.logger

// Analyze raw OCR result to determine a game state.

/**
 * There must be at least this many of a [GameState]'s trigger text present in an OCR result to pass
 * the trigger text check.
 */
private const val TRIGGER_TEXT_MATCH_THRESHOLD = 3

/**
 * Analyzes a raw OCR result to determine the [GameState].
 */
fun analyzeOCRResult(ocrResult: String): GameState {
    val ocrResult = ocrResult.lowercase()
//    logger.info {"OCR Result: \n$ocrResult"}  // TODO: remove this when done debugging

    return when {
        passesTriggerTextCheck(GameState.LOBBY, ocrResult) -> GameState.LOBBY
        else -> GameState.UNKNOWN
    }
}

/**
 * Returns true if the provided [GameState]'s supporting text appears at least [TRIGGER_TEXT_MATCH_THRESHOLD]
 * in the OCR result.
 *
 * @param gameState The [GameState] to check.
 * @param ocrResult The raw OCR result.
 */
private fun passesTriggerTextCheck(gameState: GameState, ocrResult: String): Boolean =
    gameState.triggerText.count { it in ocrResult } >= TRIGGER_TEXT_MATCH_THRESHOLD
