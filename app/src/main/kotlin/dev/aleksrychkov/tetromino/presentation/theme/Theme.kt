package dev.aleksrychkov.tetromino.presentation.theme

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppTheme(
    content: @Composable() () -> Unit
) {
    MaterialTheme(
        colorScheme = Scheme,
        typography = AppTypography,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) { content() }
    }
}
