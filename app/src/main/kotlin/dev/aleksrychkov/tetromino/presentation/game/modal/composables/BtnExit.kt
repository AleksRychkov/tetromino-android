package dev.aleksrychkov.tetromino.presentation.game.modal.composables

import androidx.activity.compose.LocalActivity
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.aleksrychkov.tetromino.R

@Composable
internal fun BtnExit(
    modifier: Modifier,
) {
    val activity = LocalActivity.current
    TextButton(
        modifier = modifier,
        onClick = {
            activity?.finish()
        }
    ) {
        Text(text = stringResource(R.string.exit))
    }
}
