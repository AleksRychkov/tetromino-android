package dev.aleksrychkov.tetromino.presentation.game.logic

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import kotlinx.parcelize.Parcelize

/*
          0 1 2 3 4 5 6 7 8 9
     |0   . . . . . . . . . . | Invisible
     |1   . . . . . . . . . . | Invisible
     |2   . . . . . . . . . . | Invisible
     |3   . . . . . . . . . . | Invisible
      4   . . . . . . . . . .
      5   . . . . . . . . . .
      6   . . . . . . . . . .
      7   . . . . . . . . . .
      8   . . . . . . . . . .
      9   . . . . . . . . . .
      10  . . . . . . . . . .
      11  . . . . . . . . . .
      12  . . . . . . . . . .
      13  . . . . . . . . . .
      14  . . . . . . . . . .
      15  . . . . . . . . . .
      16  . . . . . . . . . .
      17  . . . . . . . . . .
      18  . . . . . . . . . .
      19  . . . . . . . . . .
      20  . . . . . . . . . .
      21  . . . . . . . . . .
      22  . . . . . . . . . .
      23  . . . . . . . . . .

 */
@Immutable
@Parcelize
internal data class GameGrid(
    val grid: Array<IntArray> = Array(GRID_HEIGHT + INVISIBLE_HEIGHT) { IntArray(GRID_WIDTH) },
) : Parcelable {
    companion object {
        const val INVISIBLE_HEIGHT = 4
    }

    fun visibleGrid(): Array<IntArray> {
        return grid.drop(INVISIBLE_HEIGHT).toTypedArray()
    }

    fun checkLines(): Int {
        var linesToRemove = 0
        var y = grid.lastIndex

        while (y >= INVISIBLE_HEIGHT) {
            if (grid[y].all { it != 0 }) {
                for (x in 0 until grid[y].size) {
                    grid[y][x] *= -1
                }
                linesToRemove++
            }
            y--
        }
        return linesToRemove
    }

    fun removeLines() {
        val newGrid = grid.map { it.copyOf() }.toTypedArray()
        var removedLines = 0
        var y = newGrid.lastIndex

        while (y >= INVISIBLE_HEIGHT) {
            if (newGrid[y].all { it != 0 }) {
                removedLines++
                for (top in y downTo 1) {
                    newGrid[top] = newGrid[top - 1]
                }
                newGrid[0] = IntArray(GRID_WIDTH)
            } else {
                y--
            }
        }
        for (i in 0 until grid.size) {
            for (j in 0 until grid[0].size) {
                grid[i][j] = newGrid[i][j]
            }
        }
    }

    fun checkGameOver(): Boolean {
        return grid[INVISIBLE_HEIGHT].any { it > 0 }
    }

    fun fillWithColor(y: Int, color: () -> Int = { randomColorInt() }) {
        for (x in 0 until grid[y].size) {
            grid[y][x] = color()
        }
    }

    fun restore(from: Array<IntArray>) {
        for (y in 0 until grid.size) {
            for (x in 0 until grid[0].size) {
                grid[y][x] = from[y][x]
            }
        }
    }

    fun reset() {
        for (y in 0 until grid.size) {
            for (x in 0 until grid[0].size) {
                grid[y][x] = 0
            }
        }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (i in 0 until grid.size) {
            for (j in 0 until grid[0].size) {
                sb.append(grid[i][j].toString().padStart(3, ' '))
            }
            sb.append("\n")
        }
        return sb.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameGrid

        return grid.contentDeepEquals(other.grid)
    }

    override fun hashCode(): Int {
        return grid.contentDeepHashCode()
    }
}
