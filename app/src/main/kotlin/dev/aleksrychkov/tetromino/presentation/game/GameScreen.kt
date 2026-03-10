package dev.aleksrychkov.tetromino.presentation.game

import android.view.KeyEvent.ACTION_DOWN
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.aleksrychkov.tetromino.presentation.game.composable.GameControls
import dev.aleksrychkov.tetromino.presentation.game.composable.GameGrid
import dev.aleksrychkov.tetromino.presentation.game.composable.GameInfo
import dev.aleksrychkov.tetromino.presentation.game.composable.ShakeConfig
import dev.aleksrychkov.tetromino.presentation.game.composable.rememberShakeController
import dev.aleksrychkov.tetromino.presentation.game.composable.shake
import dev.aleksrychkov.tetromino.presentation.game.logic.COLOR_LIGHT_BLUE
import dev.aleksrychkov.tetromino.presentation.game.logic.GRID_HEIGHT
import dev.aleksrychkov.tetromino.presentation.game.logic.GRID_WIDTH
import dev.aleksrychkov.tetromino.presentation.game.modal.GameOverModal
import dev.aleksrychkov.tetromino.presentation.game.modal.InitialModal
import dev.aleksrychkov.tetromino.presentation.game.modal.PausedModal
import dev.aleksrychkov.tetromino.presentation.theme.AppTheme
import dev.aleksrychkov.tetromino.presentation.theme.Large
import dev.aleksrychkov.tetromino.presentation.theme.Normal

@Composable
internal fun GameScreen(
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = viewModel(factory = GameViewModelFactory.Factory),
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                gameViewModel.onResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                gameViewModel.onPause()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Content(
        modifier = modifier,
        gameViewModel = gameViewModel,
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    gameViewModel: GameViewModel,
) {
    val state by gameViewModel.state.collectAsStateWithLifecycle()
    val requester = remember { FocusRequester() }

    Content(
        modifier = modifier
            .onKeyEvent { event: KeyEvent ->
                if (event.nativeKeyEvent.action != ACTION_DOWN) return@onKeyEvent false
                when (event.key) {
                    Key.DirectionUp -> {
                        gameViewModel.onUp()
                        true
                    }

                    Key.DirectionDown -> {
                        gameViewModel.onDown()
                        true
                    }

                    Key.DirectionLeft -> {
                        gameViewModel.onLeft()
                        true
                    }

                    Key.DirectionRight -> {
                        gameViewModel.onRight()
                        true
                    }

                    else -> false
                }
            }
            .focusRequester(requester)
            .focusable(),
        state = state,
        onRotateClicked = gameViewModel::onRotateRight,
        onDropClicked = gameViewModel::onDrop,
        onPauseClicked = gameViewModel::onPauseResumeClicked,
        onLeftDown = gameViewModel::onLeftDown,
        onLeftReleased = gameViewModel::onLeftReleased,
        onRightDown = gameViewModel::onRightDown,
        onRightReleased = gameViewModel::onRightRelease,
        onDownDown = gameViewModel::onDownDown,
        onDownReleased = gameViewModel::onDownRelease,
    )
    LaunchedEffect(Unit) {
        requester.requestFocus()
    }

    InitialModal(
        state = state,
        onStartClicked = gameViewModel::onStartClicked,
    )

    PausedModal(
        state = state,
        onResumeClicked = gameViewModel::onPauseResumeClicked,
    )

    GameOverModal(
        state = state,
        onRestartClicked = gameViewModel::onRestart,
    )
}

@Composable
private fun Content(
    modifier: Modifier,
    state: GameUiState,
    onRotateClicked: () -> Unit = {},
    onDropClicked: () -> Unit = {},
    onPauseClicked: () -> Unit = {},
    onLeftDown: () -> Unit = {},
    onLeftReleased: () -> Unit = {},
    onRightDown: () -> Unit = {},
    onRightReleased: () -> Unit = {},
    onDownDown: () -> Unit = {},
    onDownReleased: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
    ) {
        val shakeController = rememberShakeController()

        GameInfo(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Normal),
            state = state,
            onPauseClicked = onPauseClicked,
        )

        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .shake(shakeController)
                .padding(Large),
            contentAlignment = Alignment.Center,
        ) {
            var cellWidth by remember { mutableStateOf(0.dp) }
            LaunchedEffect(Unit) {
                val cellWidthFromWidth = maxWidth / GRID_WIDTH
                val cellWidthFromHeight = maxHeight / GRID_HEIGHT
                cellWidth = minOf(cellWidthFromWidth, cellWidthFromHeight)
            }
            val starPadding = (maxWidth - (cellWidth * GRID_WIDTH)) / 2
            GameGrid(
                modifier = Modifier
                    .padding(start = starPadding),
                gameGrid = state.gameGrid,
                tick = state.tick,
                cellWidth = cellWidth,
            )
        }

        GameControls(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Normal)
                .navigationBarsPadding(),
            onRotateClicked = onRotateClicked,
            onDropClicked = {
                shakeController.shake(
                    ShakeConfig(
                        iterations = 2,
                        intensity = 20_000f,
                        translateY = 8f,
                    )
                )
                onDropClicked()
            },
            onLeftDown = onLeftDown,
            onLeftReleased = onLeftReleased,
            onRightDown = onRightDown,
            onRightReleased = onRightReleased,
            onDownDown = onDownDown,
            onDownReleased = onDownReleased,
        )
    }
}

@Preview
@Composable
private fun ContentPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Content(
                modifier = Modifier.fillMaxSize(),
                state = GameUiState(
                    nextPieceColor = COLOR_LIGHT_BLUE,
                ),
            )
        }
    }
}
