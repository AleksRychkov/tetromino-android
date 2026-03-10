package dev.aleksrychkov.tetromino.presentation.game.logic

import androidx.compose.ui.graphics.Color
import dev.aleksrychkov.tetromino.presentation.theme.ColorBlue
import dev.aleksrychkov.tetromino.presentation.theme.ColorGreen
import dev.aleksrychkov.tetromino.presentation.theme.ColorLightBlue
import dev.aleksrychkov.tetromino.presentation.theme.ColorMagenta
import dev.aleksrychkov.tetromino.presentation.theme.ColorOrange
import dev.aleksrychkov.tetromino.presentation.theme.ColorRed
import dev.aleksrychkov.tetromino.presentation.theme.ColorYellow
import kotlin.random.Random

internal const val COLOR_RED = 1
internal const val COLOR_MAGENTA = 2
internal const val COLOR_GREEN = 4
internal const val COLOR_YELLOW = 8
internal const val COLOR_ORANGE = 16
internal const val COLOR_BLUE = 32
internal const val COLOR_LIGHT_BLUE = 64
internal const val COLOR_GHOST = 128

internal fun Int.toColor(): Color {
    val value = if (this < 0) -this else this
    return when {
        value and COLOR_RED != 0 -> ColorRed
        value and COLOR_MAGENTA != 0 -> ColorMagenta
        value and COLOR_GREEN != 0 -> ColorGreen
        value and COLOR_YELLOW != 0 -> ColorYellow
        value and COLOR_ORANGE != 0 -> ColorOrange
        value and COLOR_BLUE != 0 -> ColorBlue
        value and COLOR_LIGHT_BLUE != 0 -> ColorLightBlue
        value and COLOR_GHOST != 0 -> Color(0x40000000)
        else -> Color.Transparent
    }
}

internal fun randomColorInt(): Int {
    val bitPosition = Random.nextInt(7)
    return 1 shl bitPosition
}
