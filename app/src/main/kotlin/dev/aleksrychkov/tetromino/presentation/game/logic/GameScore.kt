package dev.aleksrychkov.tetromino.presentation.game.logic

internal class GameScore {
    var score: Long = 0
        private set

    fun updateScore(removedLines: Int, levelAfterClear: Int) {
        val basePoints = when (removedLines) {
            1 -> 40
            2 -> 100
            3 -> 300
            4 -> 1200
            else -> 0
        }
        score += basePoints * (levelAfterClear + 1)
    }

    fun reset(score: Long = 0) {
        this.score = score
    }
}