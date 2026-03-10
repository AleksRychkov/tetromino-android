package dev.aleksrychkov.tetromino.presentation.game.modal.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.aleksrychkov.tetromino.R
import dev.aleksrychkov.tetromino.presentation.theme.AppTheme
import dev.aleksrychkov.tetromino.presentation.theme.Large
import dev.aleksrychkov.tetromino.presentation.theme.Small

@Composable
internal fun DifficultyBtn(
    modifier: Modifier,
    difficulty: Int,
    onDifficultyChanged: (Int) -> Unit,
) {
    val options = listOf(
        stringResource(R.string.easy),
        stringResource(R.string.medium),
        stringResource(R.string.hard),
    )

    SingleChoiceSegmentedButtonRow(
        modifier = modifier,
    ) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size,
                ),
                border = SegmentedButtonDefaults.borderStroke(
                    MaterialTheme.colorScheme.secondary,
                    width = Small,
                ),
                onClick = { onDifficultyChanged(index) },
                selected = index == difficulty,
                label = { Text(label) },
                icon = {}
            )
        }
    }
}


@Preview
@Composable
private fun DifficultyBtnPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            DifficultyBtn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Large),
                difficulty = 1,
            ) { }
        }
    }
}