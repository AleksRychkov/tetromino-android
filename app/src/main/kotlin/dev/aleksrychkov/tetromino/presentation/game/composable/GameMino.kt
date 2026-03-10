package dev.aleksrychkov.tetromino.presentation.game.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_GHOST
import dev.aleksrychkov.tetromino.presentation.game.logic.GRID_WIDTH
import dev.aleksrychkov.tetromino.presentation.game.logic.REMOVE_LINE_ANIMATION_DURATION_MILLIS
import dev.aleksrychkov.tetromino.presentation.game.logic.toColor
import dev.aleksrychkov.tetromino.presentation.theme.AppTheme
import dev.aleksrychkov.tetromino.presentation.theme.Small
import dev.aleksrychkov.tetromino.presentation.theme.Tinny

@Composable
internal fun GameMino(
    modifier: Modifier = Modifier,
    index: Int = 0,
    color: Int = 1,
    borderWidth: Dp = 0.dp,
    strokeWidth: Dp = 0.dp,
    roundedRadius: Dp = 0.dp,
) {
    val scale = remember { Animatable(1f) }
    val durationMillis = remember { REMOVE_LINE_ANIMATION_DURATION_MILLIS / 5 }

    LaunchedEffect(color) {
        val step = if (index <= (GRID_WIDTH / 2 - 1)) {
            (GRID_WIDTH / 2 - 1) - index
        } else {
            index - (GRID_WIDTH / 2)
        }
        if (color < 0) {
            scale.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    delayMillis = step * durationMillis,
                    durationMillis = durationMillis,
                    easing = FastOutLinearInEasing,
                )
            )
        } else {
            scale.snapTo(1f)
        }
    }
    Box(
        modifier
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
                alpha = scale.value
            }
    ) {
        if (color < COLOR_GHOST) {
            DefaultMino(
                color = color.toColor(),
                borderWidth = borderWidth,
                strokeWidth = strokeWidth,
                roundedRadius = roundedRadius,
            )
        } else {
            DefaultMino(
                color = Color.Transparent,
                borderWidth = 0.dp,
                strokeWidth = 2.dp,
                roundedRadius = roundedRadius,
            )
        }
    }
}

@Composable
private fun BoxScope.DefaultMino(
    color: Color,
    borderWidth: Dp,
    strokeWidth: Dp,
    roundedRadius: Dp,
) {
    val density = LocalDensity.current
    val borderWidthPx = with(density) { borderWidth.toPx() }
    val strokeColor = MaterialTheme.colorScheme.onBackground
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(roundedRadius))
    ) {
        drawRect(color = color)

        // top
        val path = Path()
        path.moveTo(0f, 0f)
        path.lineTo(borderWidthPx, borderWidthPx)
        path.lineTo(size.width - borderWidthPx, borderWidthPx)
        path.lineTo(size.width, 0f)
        path.close()
        drawPath(path, Color.White.copy(alpha = 0.25f))

        // bottom
        path.reset()
        path.moveTo(0f, size.height)
        path.lineTo(borderWidthPx, size.height - borderWidthPx)
        path.lineTo(size.width - borderWidthPx, size.height - borderWidthPx)
        path.lineTo(size.width, size.height)
        path.close()
        drawPath(path, Color.Black.copy(alpha = 0.25f))

        // right
        path.reset()
        path.moveTo(size.width, 0f)
        path.lineTo(size.width - borderWidthPx, borderWidthPx)
        path.lineTo(size.width - borderWidthPx, size.height - borderWidthPx)
        path.lineTo(size.width, size.height)
        path.close()
        drawPath(path, Color.Black.copy(alpha = 0.15f))

        // left
        path.reset()
        path.moveTo(0f, 0f)
        path.lineTo(borderWidthPx, borderWidthPx)
        path.lineTo(borderWidthPx, size.height - borderWidthPx)
        path.lineTo(0f, size.height)
        path.close()
        drawPath(path, Color.White.copy(alpha = 0.15f))

        val radius = with(density) { roundedRadius.toPx() }
        val strokeWidthPx = with(density) { strokeWidth.toPx() }
        if (strokeWidthPx > 0) {
            drawRoundRect(
                color = strokeColor,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round),
                cornerRadius = CornerRadius(radius, radius)
            )
        }
    }
}

@Preview
@Composable
private fun GameMinoPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            GameMino(
                modifier = Modifier
                    .width(30.dp)
                    .aspectRatio(1f),
                borderWidth = Small,
                strokeWidth = 0.dp,
                roundedRadius = Tinny,
            )
        }
    }
}
