package dev.aleksrychkov.tetromino.presentation.game.modal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.aleksrychkov.tetromino.R
import dev.aleksrychkov.tetromino.presentation.game.GameUiState
import dev.aleksrychkov.tetromino.presentation.game.composable.WordTetromino
import dev.aleksrychkov.tetromino.presentation.game.logic.GameDifficulty
import dev.aleksrychkov.tetromino.presentation.game.modal.composables.BtnExit
import dev.aleksrychkov.tetromino.presentation.game.modal.composables.BtnMain
import dev.aleksrychkov.tetromino.presentation.game.modal.composables.DifficultyBtn
import dev.aleksrychkov.tetromino.presentation.theme.AppTheme
import dev.aleksrychkov.tetromino.presentation.theme.Large
import dev.aleksrychkov.tetromino.presentation.theme.Large2X

@Composable
internal fun InitialModal(
    state: GameUiState,
    onStartClicked: (GameDifficulty) -> Unit,
) {
    if (state.state == GameUiState.Flag.Initial) {
        Dialog(onDismissRequest = { }) {
            ModalContent {

                WordTetromino(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Large)
                        .padding(top = Large),
                    cellSize = 5.dp,
                )

                var difficulty by remember(state.difficulty) { mutableIntStateOf(state.difficulty.ordinal) }
                DifficultyBtn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Large2X)
                        .padding(horizontal = Large),
                    difficulty = difficulty,
                    onDifficultyChanged = {
                        difficulty = it
                    },
                )

                BtnMain(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Large),
                    text = stringResource(R.string.start),
                    onClick = { onStartClicked(GameDifficulty.entries[difficulty]) },
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
private fun InitialModalPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            InitialModal(
                state = GameUiState(),
                onStartClicked = { _ -> },
            )
        }
    }
}
