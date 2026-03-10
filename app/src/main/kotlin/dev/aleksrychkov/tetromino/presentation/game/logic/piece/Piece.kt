package dev.aleksrychkov.tetromino.presentation.game.logic.piece

import androidx.compose.runtime.Immutable
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_BLUE
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_GREEN
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_LIGHT_BLUE
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_MAGENTA
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_ORANGE
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_RED
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_YELLOW


@Immutable
internal interface Piece {

    companion object {
        fun from(
            color: Int,
            x: Int? = null,
            y: Int? = null,
            turn: Int? = null,
        ): Piece? {
            return when (color) {
                COLOR_RED -> ZPiece
                COLOR_MAGENTA -> TPiece
                COLOR_GREEN -> SPiece
                COLOR_YELLOW -> OPiece
                COLOR_ORANGE -> LPiece
                COLOR_BLUE -> JPiece
                COLOR_LIGHT_BLUE -> IPiece
                else -> null
            }?.apply {
                x?.let { currentX = it }
                y?.let { currentY = it }
                turn?.let { currentTurn = it }
            }
        }
    }

    val color: Int
    var currentTurn: Int
    var currentX: Int
    var currentY: Int


    fun down(grid: Array<IntArray>): Boolean
    fun left(grid: Array<IntArray>)
    fun right(grid: Array<IntArray>)
    fun start(grid: Array<IntArray>)
    fun rotate(grid: Array<IntArray>) {}
    fun getBodyCoordinates(
        turn: Int = currentTurn,
        x: Int = currentX,
        y: Int = currentY,
    ): List<Pair<Int, Int>>

    fun addGhost(grid: Array<IntArray>)
    fun removeGhost(grid: Array<IntArray>)
}
