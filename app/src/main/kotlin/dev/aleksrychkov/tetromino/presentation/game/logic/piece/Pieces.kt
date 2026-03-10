package dev.aleksrychkov.tetromino.presentation.game.logic.piece

import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_BLUE
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_GREEN
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_LIGHT_BLUE
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_MAGENTA
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_ORANGE
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_RED
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_YELLOW

internal object OPiece : BasePiece() {
    override val maxTurns = 1
    override val color = COLOR_YELLOW

    // The O-Piece doesn't need wall kicks since it never changes shape
    override val wallKickOffsets = listOf(0 to 0)

    override fun getBodyCoordinates(turn: Int, x: Int, y: Int) = listOf(
        y to x, y to x + 1,
        y + 1 to x, y + 1 to x + 1
    )
}

internal object JPiece : BasePiece() {
    override val maxTurns = 4
    override val color = COLOR_BLUE
    override fun getBodyCoordinates(turn: Int, x: Int, y: Int) = when (turn) {
        0 -> listOf(y to x - 1, y + 1 to x - 1, y + 1 to x, y + 1 to x + 1)
        1 -> listOf(y to x, y to x + 1, y + 1 to x, y + 2 to x)
        2 -> listOf(y to x - 1, y to x, y to x + 1, y + 1 to x + 1)
        3 -> listOf(y to x, y + 1 to x, y + 2 to x, y + 2 to x - 1)
        else -> emptyList()
    }
}

internal object LPiece : BasePiece() {
    override val maxTurns = 4
    override val color = COLOR_ORANGE
    override fun getBodyCoordinates(turn: Int, x: Int, y: Int) = when (turn) {
        0 -> listOf(y to x + 1, y + 1 to x - 1, y + 1 to x, y + 1 to x + 1)
        1 -> listOf(y to x, y + 1 to x, y + 2 to x, y + 2 to x + 1)
        2 -> listOf(y to x - 1, y to x, y to x + 1, y + 1 to x - 1)
        3 -> listOf(y to x - 1, y to x, y + 1 to x, y + 2 to x)
        else -> emptyList()
    }
}

internal object SPiece : BasePiece() {
    override val maxTurns = 4
    override val color = COLOR_GREEN
    override fun getBodyCoordinates(turn: Int, x: Int, y: Int) = when (turn) {
        0, 2 -> listOf(y to x, y to x + 1, y + 1 to x - 1, y + 1 to x)
        1, 3 -> listOf(y to x - 1, y + 1 to x - 1, y + 1 to x, y + 2 to x)
        else -> emptyList()
    }
}

internal object TPiece : BasePiece() {
    override val maxTurns = 4
    override val color = COLOR_MAGENTA
    override fun getBodyCoordinates(turn: Int, x: Int, y: Int) = when (turn) {
        0 -> listOf(y to x, y + 1 to x - 1, y + 1 to x, y + 1 to x + 1)
        1 -> listOf(y to x - 1, y + 1 to x - 1, y + 2 to x - 1, y + 1 to x)
        2 -> listOf(y to x - 1, y to x, y to x + 1, y + 1 to x)
        3 -> listOf(y to x, y + 1 to x, y + 2 to x, y + 1 to x - 1)
        else -> emptyList()
    }
}

internal object ZPiece : BasePiece() {
    override val maxTurns = 4
    override val color = COLOR_RED
    override fun getBodyCoordinates(turn: Int, x: Int, y: Int) = when (turn) {
        0, 2 -> listOf(y to x - 1, y to x, y + 1 to x, y + 1 to x + 1)
        1, 3 -> listOf(y to x, y + 1 to x - 1, y + 1 to x, y + 2 to x - 1)
        else -> emptyList()
    }
}

internal object IPiece : BasePiece() {
    override val maxTurns = 2
    override val color = COLOR_LIGHT_BLUE

    // I-Piece needs a custom wall kick for better feel
    override val wallKickOffsets = listOf(0 to 0, -1 to 0, 1 to 0, -2 to 0, 2 to 0, 0 to -1)

    override fun getBodyCoordinates(turn: Int, x: Int, y: Int) = when (turn) {
        0 -> listOf(y to x, y to x + 1, y to x + 2, y to x + 3)
        1 -> listOf(y - 1 to x + 2, y to x + 2, y + 1 to x + 2, y + 2 to x + 2)
        else -> emptyList()
    }
}
