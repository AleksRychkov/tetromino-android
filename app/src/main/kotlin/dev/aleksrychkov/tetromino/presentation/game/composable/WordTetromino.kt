package dev.aleksrychkov.tetromino.presentation.game.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import dev.aleksrychkov.tetromino.presentation.theme.AppTheme
import dev.aleksrychkov.tetromino.presentation.theme.Large
import dev.aleksrychkov.tetromino.presentation.theme.Medium

@Composable
internal fun WordTetromino(
    modifier: Modifier,
    cellSize: Dp,
) {
    val size = Modifier.size(cellSize)
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        TMino(modifier = size)
        Spacer(modifier = size)
        EMino(modifier = size)
        Spacer(modifier = size)
        TMino(modifier = size)
        Spacer(modifier = size)
        RMino(modifier = size)
        Spacer(modifier = size)
        OMino(modifier = size)
        Spacer(modifier = size)
        MMino(modifier = size)
        Spacer(modifier = size)
        IMino(modifier = size)
        Spacer(modifier = size)
        NMino(modifier = size)
        Spacer(modifier = size)
        OMino(modifier = size)
    }
}

@Composable
@Preview
private fun WordTetrominoPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            WordTetromino(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Large),
                cellSize = Medium,
            )
        }
    }
}
