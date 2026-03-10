package dev.aleksrychkov.tetromino.presentation.game

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import dev.aleksrychkov.tetromino.presentation.game.logic.GameDifficulty
import dev.aleksrychkov.tetromino.presentation.game.logic.GameGrid
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize
internal data class GameUiState(
    val level: Int = 0,
    val score: Long = 0,
    val isBestScore: Boolean = false,
    val clearedLines: Int = 0,
    val isGhostEnabled: Boolean = true,
    val tick: Byte = 0,
    val nextPieceColor: Int = 0,
    val isPaused: Boolean = true,
    val state: Flag = Flag.Initial,
    val difficulty: GameDifficulty = GameDifficulty.Easy,
    val gameGrid: GameGrid = GameGrid(),
) : Parcelable {
    enum class Flag {
        Initial,
        Paused,
        Running,
        GameOver,
    }
}
