package dev.aleksrychkov.tetromino.presentation.game.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.aleksrychkov.tetromino.R
import dev.aleksrychkov.tetromino.presentation.game.GameUiState
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_BLUE
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_GREEN
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_LIGHT_BLUE
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_MAGENTA
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_ORANGE
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_RED
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_YELLOW
import dev.aleksrychkov.tetromino.presentation.theme.AppTheme
import dev.aleksrychkov.tetromino.presentation.theme.Normal

@Composable
internal fun GameInfo(
    modifier: Modifier,
    state: GameUiState,
    onPauseClicked: () -> Unit,
) {
    val cellWidth = 18.dp
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .defaultMinSize(minHeight = cellWidth * 3)
            .padding(vertical = Normal),
    ) {
        Column(
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxHeight()
                .clickable {
                    onPauseClicked()
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_pause_circle_24),
                contentDescription = "pause",
                colorFilter = ColorFilter.tint(
                    color = MaterialTheme.colorScheme.primary,
                ),
            )
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = Normal),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Level",
                    style = MaterialTheme.typography.labelMedium,
                )
                AnimatedCounter(
                    count = state.level.toLong(),
                    fontWeight = FontWeight.Bold,
                )
            }
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Score",
                    style = MaterialTheme.typography.labelMedium,
                )
                AnimatedCounter(
                    count = state.score,
                    fontWeight = FontWeight.Bold,
                )
            }
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Lines",
                    style = MaterialTheme.typography.labelMedium,
                )
                AnimatedCounter(
                    count = state.clearedLines.toLong(),
                    fontWeight = FontWeight.Bold,
                )
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Next",
                    style = MaterialTheme.typography.labelMedium,
                )
                NextPiece(
                    modifier = Modifier
                        .width(cellWidth * 4)
                        .height(cellWidth * 2),
                    nextPieceColor = state.nextPieceColor,
                    cellWidth = cellWidth,
                )
            }
        }
    }
}

@Composable
private fun NextPiece(
    modifier: Modifier,
    nextPieceColor: Int = 1,
    cellWidth: Dp,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
    ) {
        when (nextPieceColor) {
            COLOR_LIGHT_BLUE -> IPiece(cellWidth)
            COLOR_RED -> ZPiece(cellWidth)
            COLOR_MAGENTA -> TPiece(cellWidth)
            COLOR_GREEN -> SPiece(cellWidth)
            COLOR_YELLOW -> OPiece(cellWidth)
            COLOR_ORANGE -> LPiece(cellWidth)
            COLOR_BLUE -> JPiece(cellWidth)
        }
    }
}

@Composable
private fun ColumnScope.IPiece(
    cellWidth: Dp
) {
    Row {
        repeat(4) {
            GameMino(
                modifier = Modifier
                    .width(cellWidth)
                    .height(cellWidth),
                color = COLOR_LIGHT_BLUE,
                borderWidth = cellWidth / 8,
                strokeWidth = 0.dp,
                roundedRadius = cellWidth / 8,
            )
        }
    }
}

@Composable
private fun ColumnScope.JPiece(
    cellWidth: Dp
) {
    Row {
        Spacer(modifier = Modifier.width(cellWidth / 2))

        GameMino(
            modifier = Modifier
                .width(cellWidth)
                .height(cellWidth),
            color = COLOR_BLUE,
            borderWidth = cellWidth / 8,
            strokeWidth = 0.dp,
            roundedRadius = cellWidth / 8,
        )
    }
    Row {
        Spacer(modifier = Modifier.width(cellWidth / 2))

        repeat(3) {
            GameMino(
                modifier = Modifier
                    .width(cellWidth)
                    .height(cellWidth),
                color = COLOR_BLUE,
                borderWidth = cellWidth / 8,
                strokeWidth = 0.dp,
                roundedRadius = cellWidth / 8,
            )
        }
    }
}

@Composable
private fun ColumnScope.LPiece(
    cellWidth: Dp
) {
    Row {
        Spacer(modifier = Modifier.width(cellWidth / 2))
        Spacer(modifier = Modifier.width(cellWidth))
        Spacer(modifier = Modifier.width(cellWidth))
        GameMino(
            modifier = Modifier
                .width(cellWidth)
                .height(cellWidth),
            color = COLOR_ORANGE,
            borderWidth = cellWidth / 8,
            strokeWidth = 0.dp,
            roundedRadius = cellWidth / 8,
        )
    }
    Row {
        Spacer(modifier = Modifier.width(cellWidth / 2))
        repeat(3) {
            GameMino(
                modifier = Modifier
                    .width(cellWidth)
                    .height(cellWidth),
                color = COLOR_ORANGE,
                borderWidth = cellWidth / 8,
                strokeWidth = 0.dp,
                roundedRadius = cellWidth / 8,
            )
        }
    }
}

@Composable
private fun ColumnScope.OPiece(
    cellWidth: Dp
) {
    Row {
        Spacer(modifier = Modifier.width(cellWidth))
        repeat(2) {
            GameMino(
                modifier = Modifier
                    .width(cellWidth)
                    .height(cellWidth),
                color = COLOR_YELLOW,
                borderWidth = cellWidth / 8,
                strokeWidth = 0.dp,
                roundedRadius = cellWidth / 8,
            )
        }
    }
    Row {
        Spacer(modifier = Modifier.width(cellWidth))
        repeat(2) {
            GameMino(
                modifier = Modifier
                    .width(cellWidth)
                    .height(cellWidth),
                color = COLOR_YELLOW,
                borderWidth = cellWidth / 8,
                strokeWidth = 0.dp,
                roundedRadius = cellWidth / 8,
            )
        }
    }
}

@Composable
private fun ColumnScope.SPiece(
    cellWidth: Dp
) {
    Row {
        Spacer(modifier = Modifier.width(cellWidth / 2))
        Spacer(modifier = Modifier.width(cellWidth))
        repeat(2) {
            GameMino(
                modifier = Modifier
                    .width(cellWidth)
                    .height(cellWidth),
                color = COLOR_GREEN,
                borderWidth = cellWidth / 8,
                strokeWidth = 0.dp,
                roundedRadius = cellWidth / 8,
            )
        }
    }
    Row {
        Spacer(modifier = Modifier.width(cellWidth / 2))
        repeat(2) {
            GameMino(
                modifier = Modifier
                    .width(cellWidth)
                    .height(cellWidth),
                color = COLOR_GREEN,
                borderWidth = cellWidth / 8,
                strokeWidth = 0.dp,
                roundedRadius = cellWidth / 8,
            )
        }
    }
}

@Composable
private fun ColumnScope.ZPiece(
    cellWidth: Dp
) {
    Row {
        Spacer(modifier = Modifier.width(cellWidth / 2))
        repeat(2) {
            GameMino(
                modifier = Modifier
                    .width(cellWidth)
                    .height(cellWidth),
                color = COLOR_RED,
                borderWidth = cellWidth / 8,
                strokeWidth = 0.dp,
                roundedRadius = cellWidth / 8,
            )
        }

    }
    Row {
        Spacer(modifier = Modifier.width(cellWidth / 2))
        Spacer(modifier = Modifier.width(cellWidth))
        repeat(2) {
            GameMino(
                modifier = Modifier
                    .width(cellWidth)
                    .height(cellWidth),
                color = COLOR_RED,
                borderWidth = cellWidth / 8,
                strokeWidth = 0.dp,
                roundedRadius = cellWidth / 8,
            )
        }
    }
}

@Composable
private fun ColumnScope.TPiece(
    cellWidth: Dp
) {
    Row {
        Spacer(modifier = Modifier.width(cellWidth / 2))
        repeat(3) {
            GameMino(
                modifier = Modifier
                    .width(cellWidth)
                    .height(cellWidth),
                color = COLOR_MAGENTA,
                borderWidth = cellWidth / 8,
                strokeWidth = 0.dp,
                roundedRadius = cellWidth / 8,
            )
        }

    }
    Row {
        Spacer(modifier = Modifier.width(cellWidth / 2))
        Spacer(modifier = Modifier.width(cellWidth))
        GameMino(
            modifier = Modifier
                .width(cellWidth)
                .height(cellWidth),
            color = COLOR_MAGENTA,
            borderWidth = cellWidth / 8,
            strokeWidth = 0.dp,
            roundedRadius = cellWidth / 8,
        )
    }
}

@Composable
@Preview
private fun GameInfoPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            GameInfo(
                modifier = Modifier.fillMaxWidth(),
                state = GameUiState(
                    nextPieceColor = COLOR_RED,
                ),
                onPauseClicked = {},
            )
        }
    }
}
