package dev.aleksrychkov.tetromino.presentation.game.logic

internal class GameLevel {
    var level: Int = 0
        private set

    var clearedLines: Int = 0
        private set

    fun updateLevel(removedLines: Int) {
        clearedLines += removedLines

        val target = level * 10 + 10
        if (clearedLines >= target) {
            level++
        }
    }

    fun reset(level: Int = 0, clearedLines: Int = 0) {
        this.level = level
        this.clearedLines = clearedLines
    }
}
