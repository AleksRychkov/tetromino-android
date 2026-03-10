package dev.aleksrychkov.tetromino.presentation.game.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import dev.aleksrychkov.tetromino.R
import dev.aleksrychkov.tetromino.presentation.theme.AppTheme
import dev.aleksrychkov.tetromino.presentation.theme.HalfNormal
import dev.aleksrychkov.tetromino.presentation.theme.Normal
import dev.aleksrychkov.tetromino.presentation.theme.Small
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun GameControls(
    modifier: Modifier,
    onRotateClicked: () -> Unit = {},
    onDropClicked: () -> Unit = {},
    onLeftDown: () -> Unit = {},
    onLeftReleased: () -> Unit = {},
    onRightDown: () -> Unit = {},
    onRightReleased: () -> Unit = {},
    onDownDown: () -> Unit = {},
    onDownReleased: () -> Unit = {},
) {

    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        BtnPointerInput(
            painter = painterResource(id = R.drawable.ic_rotate_24),
            contentDescription = "rotate",
            onDown = onRotateClicked,
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.Center,
        ) {
            BtnPointerInput(
                painter = painterResource(id = R.drawable.ic_arrow_circle_left_24),
                contentDescription = "left",
                onDown = onLeftDown,
                onUpOrCanceled = onLeftReleased,
            )

            BtnPointerInput(
                modifier = Modifier.padding(horizontal = Normal),
                painter = painterResource(id = R.drawable.ic_arrow_circle_down_24),
                contentDescription = "down",
                onDown = onDownDown,
                onUpOrCanceled = onDownReleased,
            )

            BtnPointerInput(
                painter = painterResource(id = R.drawable.ic_arrow_circle_right_24),
                contentDescription = "right",
                onDown = onRightDown,
                onUpOrCanceled = onRightReleased,
            )
        }

        BtnPointerInput(
            painter = painterResource(id = R.drawable.ic_arrow_drop_down_circle_24),
            contentDescription = "drop",
            onDown = onDropClicked,
        )
    }
}

@Composable
private fun BtnPointerInput(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String = "",
    onDown: () -> Unit,
    onUpOrCanceled: () -> Unit = {},
) {
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
            .aspectRatio(1f)
            .pointerInput(contentDescription) {
                coroutineScope {
                    awaitEachGesture {
                        awaitFirstDown()
                        onDown()
                        scope.launch {
                            scale.animateTo(
                                targetValue = 0.75f,
                                animationSpec = tween()
                            )
                        }
                        waitForUpOrCancellation()
                        onUpOrCanceled()
                        scope.launch {
                            scale.snapTo(1f)
                        }
                    }
                }
            }
            .border(
                width = Small,
                shape = RoundedCornerShape(Normal),
                color = MaterialTheme.colorScheme.primary,
            )
            .padding(HalfNormal)
            .clip(RoundedCornerShape(Normal)),
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .padding(Normal),
            painter = painter,
            contentDescription = contentDescription,
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.primary,
            )
        )
    }
}

@Preview
@Composable
private fun GameControlsPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            GameControls(modifier = Modifier.fillMaxWidth())
        }
    }
}
