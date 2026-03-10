package dev.aleksrychkov.tetromino.presentation.game.logic

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal class GameState(
    val gameGrid: GameGrid,
    val state: Flag,
    val currentPieceColor: Int,
    val currentPieceTurn: Int?,
    val currentPieceX: Int?,
    val currentPieceY: Int?,
    val nextPieceColor: Int,
    val score: Long,
    val level: Int,
    val clearedLines: Int,
    val isGhostEnabled: Boolean,
    val difficulty: GameDifficulty,
) : Parcelable {
    enum class Flag {
        Initial,
        Tick,
        Pause,
        Paused,
        CheckLines,
        RemoveLines,
        CheckGameOver,
        GameOver,
    }
}
