package dev.aleksrychkov.tetromino.presentation.game.logic.piece

import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_GHOST
import kotlin.random.Random

abstract class BasePiece : Piece {
    abstract val maxTurns: Int
    abstract override val color: Int

    override var currentX = 4
    override var currentY = 0
    override var currentTurn = 0

    // Shared wall kick logic for all pieces
    protected open val wallKickOffsets = listOf(
        0 to 0,   // Normal
        -1 to 0,  // Left
        1 to 0,   // Right
        0 to -1,  // Up
        -1 to -1, // Left-Up
        1 to -1,  // Right-Up
        0 to 1    // Down
    )

    override fun start(grid: Array<IntArray>) {
        currentTurn = Random.nextInt(maxTurns)
        currentX = 4
        currentY = if (currentTurn % 2 == 0) 2 else 1
        draw(grid)
    }

    override fun down(grid: Array<IntArray>): Boolean {
        return move(grid, currentY + 1, currentX, currentTurn).also { success ->
            if (success) currentY++
        }
    }

    override fun left(grid: Array<IntArray>) {
        if (move(grid, currentY, currentX - 1, currentTurn)) currentX--
    }

    override fun right(grid: Array<IntArray>) {
        if (move(grid, currentY, currentX + 1, currentTurn)) currentX++
    }

    override fun rotate(grid: Array<IntArray>) {
        val nextTurn = (currentTurn + 1) % maxTurns
        for ((offsetX, offsetY) in wallKickOffsets) {
            if (move(grid, currentY + offsetY, currentX + offsetX, nextTurn)) {
                currentX += offsetX
                currentY += offsetY
                currentTurn = nextTurn
                return
            }
        }
    }

    override fun addGhost(grid: Array<IntArray>) {
        val currentBody = getBodyCoordinates(currentTurn, currentX, currentY)
        var ghostBody = currentBody

        while (true) {
            val nextBody = ghostBody.map { (y, x) -> (y + 1) to x }
            val isValid = nextBody.all { (y, x) ->
                y in grid.indices &&
                        x in grid[0].indices &&
                        (grid[y][x] == 0 || (y to x) in currentBody) // Ignore current piece blocks
            }

            if (!isValid) break
            ghostBody = nextBody
        }

        ghostBody.forEach { (y, x) ->
            if (grid[y][x] == 0) {
                grid[y][x] = COLOR_GHOST
            }
        }
    }

    override fun removeGhost(grid: Array<IntArray>) {
        grid.indices.forEach { y ->
            grid[y].indices.forEach { x ->
                if (grid[y][x] == COLOR_GHOST) {
                    grid[y][x] = 0
                }
            }
        }
    }

    private fun move(grid: Array<IntArray>, nextY: Int, nextX: Int, nextTurn: Int): Boolean {
        val currentBody = getBodyCoordinates(currentTurn, currentX, currentY)
        val nextBody = getBodyCoordinates(nextTurn, nextX, nextY)

        val isValid = nextBody.all { (y, x) ->
            y in grid.indices && x in grid[0].indices &&
                    (grid[y][x] == 0 || (y to x) in currentBody)
        }

        if (!isValid) return false

        currentBody.forEach { (y, x) -> grid[y][x] = 0 }
        nextBody.forEach { (y, x) -> grid[y][x] = color }
        return true
    }

    private fun draw(grid: Array<IntArray>) {
        getBodyCoordinates().forEach { (y, x) ->
            if (y in grid.indices && x in grid[0].indices) grid[y][x] = color
        }
    }
}
