package dev.aleksrychkov.tetromino.presentation.game.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import dev.aleksrychkov.tetromino.presentation.theme.AppTheme
import dev.aleksrychkov.tetromino.presentation.theme.Large
import dev.aleksrychkov.tetromino.presentation.theme.Medium

@Composable
internal fun WordGameOver(
    modifier: Modifier,
    cellSize: Dp,
) {
    val size = Modifier.size(cellSize)
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Center,
        ) {
            GMino(modifier = size)
            Spacer(modifier = size)
            AMino(modifier = size)
            Spacer(modifier = size)
            MMino(modifier = size)
            Spacer(modifier = size)
            EMino(modifier = size)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = cellSize),
            horizontalArrangement = Arrangement.Center,
        ) {
            OMino(modifier = size)
            Spacer(modifier = size)
            UMino(modifier = size)
            Spacer(modifier = size)
            EMino(modifier = size)
            Spacer(modifier = size)
            RMino(modifier = size)
        }
    }
}

@Composable
@Preview
private fun WordGameOverPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            WordGameOver(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Large),
                cellSize = Medium,
            )
        }
    }
}
