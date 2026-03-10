package dev.aleksrychkov.tetromino.presentation.game.modal.composables

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import dev.aleksrychkov.tetromino.presentation.theme.Small

@Composable
internal fun BtnMain(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit,
) {
    OutlinedButton(
        modifier = modifier,
        border = ButtonDefaults.outlinedButtonBorder(true).copy(
            brush = SolidColor(
                MaterialTheme.colorScheme.primary
            ),
            width = Small,
        ),
        onClick = onClick
    ) {
        Text(text = text)
    }
}
