package dev.aleksrychkov.tetromino.presentation.game.modal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.aleksrychkov.tetromino.R
import dev.aleksrychkov.tetromino.presentation.game.GameUiState
import dev.aleksrychkov.tetromino.presentation.game.composable.WordPause
import dev.aleksrychkov.tetromino.presentation.game.modal.composables.BtnExit
import dev.aleksrychkov.tetromino.presentation.game.modal.composables.BtnMain
import dev.aleksrychkov.tetromino.presentation.theme.AppTheme
import dev.aleksrychkov.tetromino.presentation.theme.Large
import dev.aleksrychkov.tetromino.presentation.theme.Large2X

@Composable
internal fun PausedModal(
    state: GameUiState,
    onResumeClicked: () -> Unit,
) {
    if (state.state == GameUiState.Flag.Paused) {
        Dialog(onDismissRequest = { }) {
            ModalContent {
                WordPause(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Large)
                        .padding(top = Large),
                    cellSize = 5.dp,
                )

                BtnMain(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Large2X, bottom = Large)
                        .padding(horizontal = Large),
                    text = stringResource(R.string.resume),
                    onClick = onResumeClicked,
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
@Preview
private fun PausedModalPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            PausedModal(
                state = GameUiState(
                    state = GameUiState.Flag.Paused
                ),
                onResumeClicked = {},
            )
        }
    }
}
