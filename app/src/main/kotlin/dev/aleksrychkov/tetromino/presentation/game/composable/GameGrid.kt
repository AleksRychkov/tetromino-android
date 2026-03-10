package dev.aleksrychkov.tetromino.presentation.game.composable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.aleksrychkov.tetromino.presentation.game.logic.GRID_HEIGHT
import dev.aleksrychkov.tetromino.presentation.game.logic.GRID_WIDTH
import dev.aleksrychkov.tetromino.presentation.game.logic.GameGrid
import dev.aleksrychkov.tetromino.presentation.game.logic.piece.IPiece
import dev.aleksrychkov.tetromino.presentation.theme.AppTheme
import dev.aleksrychkov.tetromino.presentation.theme.ColorBlue
import dev.aleksrychkov.tetromino.presentation.theme.ColorGreen
import dev.aleksrychkov.tetromino.presentation.theme.ColorLightBlue
import dev.aleksrychkov.tetromino.presentation.theme.ColorMagenta
import dev.aleksrychkov.tetromino.presentation.theme.ColorOrange
import dev.aleksrychkov.tetromino.presentation.theme.ColorRed
import dev.aleksrychkov.tetromino.presentation.theme.ColorYellow
import dev.aleksrychkov.tetromino.presentation.theme.Normal
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
internal fun GameGrid(
    modifier: Modifier = Modifier,
    gameGrid: GameGrid,
    tick: Byte,
    cellWidth: Dp,
) {
    // todo: big screens
    ActualGrid(
        modifier = modifier
            .width(cellWidth * GRID_WIDTH)
            .height(cellWidth * GRID_HEIGHT),
        cellWidth = cellWidth,
        gameGrid = gameGrid,
        tick = tick,
    )
}

//@Composable
//internal fun ActualGrid(
//    modifier: Modifier,
//    cellWidth: Dp,
//    gameGrid: GameGrid,
//    tick: Byte,
//) {
//    tick // dummy ticker to invoke recomposition
//    val density = LocalDensity.current
//    val gridColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f)
//
//    val transition = rememberInfiniteTransition(label = "RetroFrame")
//    val offset by transition.animateFloat(
//        initialValue = 0f,
//        targetValue = 500f, // Adjust for speed/distance
//        animationSpec = infiniteRepeatable(
//            animation = tween(10000, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "Offset"
//    )
//    Box(
//        modifier = modifier
//            .drawBehind {
//                drawSolidGrid(
//                    cellWidth = cellWidth,
//                    density = density,
//                    gridColor = gridColor,
//                )
//                drawRetroFrame(density = density, animateOffset = offset)
//            }
//    ) {
//        val grid = gameGrid.visibleGrid()
//        for (y in 0 until GRID_HEIGHT) {
//            for (x in 0 until GRID_WIDTH) {
//                val cellColor = grid[y][x]
//                key(y, x, cellColor) {
//                    if (cellColor != 0) {
//                        GameMino(
//                            modifier = Modifier
//                                .width(cellWidth)
//                                .height(cellWidth)
//                                .offset(
//                                    x = cellWidth * x,
//                                    y = cellWidth * y,
//                                ),
//                            index = x,
//                            color = cellColor,
//                            borderWidth = cellWidth / 8,
//                            strokeWidth = 0.dp,
//                            roundedRadius = cellWidth / 8,
//                        )
//                    }
//                }
//            }
//        }
//    }
//}

@Composable
internal fun ActualGrid(
    modifier: Modifier,
    cellWidth: Dp,
    gameGrid: GameGrid,
    tick: Byte,
) {
    val density = LocalDensity.current
    val gridColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f)

    val transition = rememberInfiniteTransition(label = "RetroFrame")
    val offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 500f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Offset"
    )

    Box(
        modifier = modifier
            .drawBehind {
                drawSolidGrid(
                    cellWidth = cellWidth,
                    density = density,
                    gridColor = gridColor,
                )
                drawRetroFrame(density = density, animateOffset = offset)
            }
    ) {
        val visibleGrid = remember(tick) { gameGrid.visibleGrid() }
        visibleGrid.forEachIndexed { y, row ->
            row.forEachIndexed { x, cellColor ->
                if (cellColor != 0) {
                    key(y, x, cellColor) {
                        GameMino(
                            modifier = Modifier
                                .size(cellWidth)
                                .offset(
                                    x = cellWidth * x,
                                    y = cellWidth * y,
                                ),
                            index = x,
                            color = cellColor,
                            borderWidth = cellWidth / 8,
                            strokeWidth = 0.dp,
                            roundedRadius = cellWidth / 8,
                        )
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawSolidGrid(
    density: Density,
    cellWidth: Dp,
    gridColor: Color,
) {
    val cellWidthPx = with(density) { cellWidth.toPx() }
    if (gridColor != Color.Transparent) {
        for (i in 0..GRID_WIDTH) {
            drawLine(
                color = gridColor,
                start = Offset(cellWidthPx * i, 0f),
                end = Offset(cellWidthPx * i, cellWidthPx * GRID_HEIGHT),
            )
        }
        for (i in 0..GRID_HEIGHT) {
            drawLine(
                color = gridColor,
                start = Offset(0f, cellWidthPx * i),
                end = Offset(cellWidthPx * GRID_WIDTH, cellWidthPx * i),
            )
        }
    }
}

private fun DrawScope.drawRetroFrame(
    density: Density,
    animateOffset: Float,
) {
    val strokeWidthPx = with(density) { 4.dp.toPx() }
    val animatedBrush = Brush.horizontalGradient(
        colors = listOf(
            ColorLightBlue, ColorBlue, ColorYellow, ColorOrange,
            ColorRed, ColorMagenta, ColorGreen, ColorLightBlue // Loop color
        ),
        startX = animateOffset,
        endX = animateOffset + size.width,
        tileMode = TileMode.Repeated
    )

    drawRect(
        brush = animatedBrush,
        topLeft = Offset(-strokeWidthPx, -strokeWidthPx),
        size = size.copy(
            width = size.width + 2 * strokeWidthPx,
            height = size.height + 2 * strokeWidthPx
        ),
        style = Stroke(
            width = with(density) { 2.dp.toPx() }, // Using a fixed dp for the stroke
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
        )
    )
}

@Preview
@Composable
private fun GameGridPreview() {
    AppTheme {
        var gameGridState by remember { mutableStateOf(GameGrid()) }
        val piece = IPiece
        piece.start(gameGridState.grid)
        val scope = rememberCoroutineScope()
        DisposableEffect(scope) {
            val job = scope.launch {
                var index = 0
                while (piece.down(gameGridState.grid)) {
                    if (index >= 5) {
                        piece.right(gameGridState.grid)
                    } else {
                        piece.left(gameGridState.grid)
                    }
                    if (Random.nextBoolean()) piece.rotate(gameGridState.grid)
                    index++
                    delay(1000)
                }
            }
            onDispose { job.cancel() }
        }

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            GameGrid(
                modifier = Modifier.padding(Normal),
                gameGrid = gameGridState,
                tick = 0,
                cellWidth = maxWidth / (GRID_WIDTH + 1),
            )
        }
    }
}
