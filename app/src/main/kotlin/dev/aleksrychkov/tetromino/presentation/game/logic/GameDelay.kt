package dev.aleksrychkov.tetromino.presentation.game.logic

internal class GameDelay(
    var difficulty: GameDifficulty = GameDifficulty.Easy
) {
    companion object {
        private const val FRAME_TIME_MS = 16.6397
    }

    var delay: Long = 0L
        private set

    init {
        reset()
    }

    fun updateDelay(level: Int) {
        val framesPerRow = when (difficulty) {
            GameDifficulty.Easy -> calculateEasy(level)
            GameDifficulty.Medium -> calculateMedium(level)
            else -> calculateHard(level)
        }
        delay = (framesPerRow * FRAME_TIME_MS).toLong()
    }

    private fun calculateHard(level: Int): Int = when {
        level <= 8 -> 48 - (level * 5)
        level == 9 -> 6
        level in 10..12 -> 5
        level in 13..15 -> 4
        level in 16..18 -> 3
        level in 19..28 -> 2
        else -> 1
    }

    private fun calculateMedium(level: Int): Int {
        val base = calculateHard(level / 2)
        return base.coerceAtLeast(3)
    }

    private fun calculateEasy(level: Int): Int {
        val base = 48 - (level * 2)
        return base.coerceAtLeast(6)
    }

    fun reset() {
        updateDelay(0)
    }
}
