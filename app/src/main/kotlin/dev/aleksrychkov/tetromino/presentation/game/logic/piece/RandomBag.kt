package dev.aleksrychkov.tetromino.presentation.game.logic.piece

internal class RandomBag {

    private var index = 0
    private val bag = intArrayOf(0, 1, 2, 3, 4, 5, 6)

    private val factory = listOf(
        IPiece, JPiece, LPiece, OPiece, SPiece, TPiece, ZPiece,
    )

    fun next(): Piece {
        if (index >= bag.size || index == 0) {
            if (index >= bag.size) {
                bag.shuffle()
                index = 0
            } else {
                bag.shuffle()
            }
        }

        val pieceIndex = bag[index]
        index++

        return factory[pieceIndex]
    }

    fun reset(currentPiece: Int? = null, nextPiece: Int? = null) {
        bag.shuffle()
        index = 0
    }
}
