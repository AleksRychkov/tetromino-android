package dev.aleksrychkov.tetromino.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.aleksrychkov.tetromino.presentation.game.GameScreen
import dev.aleksrychkov.tetromino.presentation.theme.AppTheme

internal class RootActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                RootContent()
            }
        }
    }

    @Composable
    private fun RootContent() {
        GameScreen(
            modifier = Modifier
                .fillMaxSize()
                .displayCutoutPadding(),
        )
    }
}
