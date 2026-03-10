package dev.aleksrychkov.tetromino.presentation.game.modal

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import dev.aleksrychkov.tetromino.R
import dev.aleksrychkov.tetromino.presentation.game.GameUiState
import dev.aleksrychkov.tetromino.presentation.game.composable.AutoSizeText
import dev.aleksrychkov.tetromino.presentation.game.composable.WordGameOver
import dev.aleksrychkov.tetromino.presentation.game.logic.GameDifficulty
import dev.aleksrychkov.tetromino.presentation.game.modal.composables.BtnExit
import dev.aleksrychkov.tetromino.presentation.game.modal.composables.BtnMain
import dev.aleksrychkov.tetromino.presentation.game.modal.composables.DifficultyBtn
import dev.aleksrychkov.tetromino.presentation.theme.AppTheme
import dev.aleksrychkov.tetromino.presentation.theme.Large
import dev.aleksrychkov.tetromino.presentation.theme.Large2X
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun GameOverModal(
    state: GameUiState,
    onRestartClicked: (GameDifficulty) -> Unit,
) {
    if (state.state == GameUiState.Flag.GameOver) {
        Dialog(onDismissRequest = { }) {
            ModalContent {
                WordGameOver(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Large)
                        .padding(top = Large),
                    cellSize = 5.dp,
                )

                Box(modifier = Modifier.wrapContentWidth(unbounded = true)) {
                    AutoSizeText(
                        modifier = Modifier.padding(Large),
                        text = state.score.toString(),
                        minFontSize = 2.sp,
                    )

                    BestScore(
                        modifier = Modifier.align(Alignment.TopEnd),
                        isVisible = state.isBestScore,
                    )
                }

                var difficulty by remember { mutableIntStateOf(state.difficulty.ordinal) }
                DifficultyBtn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Large2X)
                        .padding(horizontal = Large),
                    difficulty = difficulty,
                    onDifficultyChanged = { difficulty = it },
                )

                BtnMain(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Large),
                    text = stringResource(R.string.restart),
                    onClick = { onRestartClicked(GameDifficulty.entries[difficulty]) },
                )

                BtnExit(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Large)
                        .padding(bottom = Large)
                )
            }
        }
    }
}

@Composable
private fun BestScore(
    modifier: Modifier,
    isVisible: Boolean,
) {
    val scale = remember { Animatable(3f) }
    val alphaAnim = remember { Animatable(0f) }
    val angle = remember { Animatable(0f) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(250L)
            launch {
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = DampingRatioMediumBouncy,
                        stiffness = StiffnessVeryLow,
                    ),
                )
            }
            launch {
                angle.animateTo(
                    targetValue = 15f,
                    animationSpec = spring(
                        dampingRatio = DampingRatioMediumBouncy,
                        stiffness = StiffnessVeryLow,
                    ),
                )
            }
            launch {
                alphaAnim.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = DampingRatioMediumBouncy,
                        stiffness = StiffnessVeryLow,
                    ),
                )
            }
        } else {
            scale.snapTo(3f)
            alphaAnim.snapTo(0f)
            angle.snapTo(0f)
        }
    }
    Icon(
        modifier = modifier
            .size(64.dp)
            .padding(Large)
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
                alpha = alphaAnim.value
                rotationZ = angle.value
                translationX = 30f
            },
        painter = painterResource(R.drawable.ic_chess_queen_24),
        contentDescription = "Best Score Stamp",
        tint = Color(0xffe4a730)
    )
}

@Preview
@Composable
private fun GameOverModalPreview() {
    AppTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            GameOverModal(
                state = GameUiState(
                    state = GameUiState.Flag.GameOver,
                    score = 123121,
                    isBestScore = true,
                ),
                onRestartClicked = { _ -> }
            )
        }
    }
}
