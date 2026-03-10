package dev.aleksrychkov.tetromino.presentation.game.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_BLUE
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_GREEN
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_LIGHT_BLUE
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_MAGENTA
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_ORANGE
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_RED
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_YELLOW

@Composable
@NonRestartableComposable
private fun BaseMino(
    modifier: Modifier,
    color: Int,
    grid: List<String>
) {
    Column {
        grid.forEach { row ->
            Row {
                row.forEach { char ->
                    if (char == 'X') {
                        GameMino(
                            modifier = modifier,
                            color = color,
                            strokeWidth = 1.dp,
                        )
                    } else {
                        Spacer(modifier = modifier)
                    }
                }
            }
        }
    }
}

@Composable
@NonRestartableComposable
internal fun TMino(modifier: Modifier) =
    BaseMino(modifier, COLOR_RED, listOf("XXX", " X ", " X ", " X ", " X "))

@Composable
@NonRestartableComposable
internal fun EMino(modifier: Modifier) =
    BaseMino(modifier, COLOR_ORANGE, listOf("XXX", "X  ", "XX ", "X  ", "XXX"))

@Composable
@NonRestartableComposable
internal fun RMino(modifier: Modifier) =
    BaseMino(modifier, COLOR_GREEN, listOf("XX ", "X X", "XX ", "X X", "X X"))

@Composable
@NonRestartableComposable
internal fun PMino(modifier: Modifier) =
    BaseMino(modifier, COLOR_GREEN, listOf("XX ", "X X", "XX ", "X  ", "X  "))

@Composable
@NonRestartableComposable
internal fun GMino(modifier: Modifier) =
    BaseMino(modifier, COLOR_GREEN, listOf(" XX", "X  X", "X   ", "X XX", " XX"))

@Composable
@NonRestartableComposable
internal fun AMino(modifier: Modifier) =
    BaseMino(modifier, COLOR_ORANGE, listOf(" X ", "X X", "XXX", "X X", "X X"))

@Composable
@NonRestartableComposable
internal fun OMino(modifier: Modifier) =
    BaseMino(modifier, COLOR_YELLOW, listOf(" X ", "X X", "X X", "X X", " X "))

@Composable
@NonRestartableComposable
internal fun UMino(modifier: Modifier) =
    BaseMino(modifier, COLOR_BLUE, listOf("X X", "X X", "X X", "X X", " X "))

@Composable
@NonRestartableComposable
internal fun SMino(modifier: Modifier) =
    BaseMino(modifier, COLOR_RED, listOf("XXX", "X  ", "XXX", "  X", "XXX"))

@Composable
@NonRestartableComposable
internal fun IMino(modifier: Modifier) =
    BaseMino(modifier, COLOR_LIGHT_BLUE, listOf("X", "X", "X", "X", "X"))

@Composable
@NonRestartableComposable
internal fun MMino(modifier: Modifier) =
    BaseMino(modifier, COLOR_MAGENTA, listOf("XX XX", "X X X", "X X X", "X   X", "X   X"))

@Composable
@NonRestartableComposable
internal fun NMino(modifier: Modifier) =
    BaseMino(modifier, COLOR_BLUE, listOf("X  X", "XX X", "X XX", "X XX", "X  X"))