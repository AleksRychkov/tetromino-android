package dev.aleksrychkov.tetromino.presentation.game.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun AnimatedCounter(
    modifier: Modifier = Modifier,
    count: Long,
    fontWeight: FontWeight,
) {
    var oldCount by remember { mutableLongStateOf(count) }
    SideEffect {
        oldCount = count
    }
    Row(modifier = modifier) {
        val countString = count.toString()
        val oldCountString = oldCount.toString()
        for (i in countString.indices) {
            val oldChar = oldCountString.getOrNull(i)
            val newChar = countString[i]
            val char = if (oldChar == newChar) {
                oldCountString[i]
            } else {
                countString[i]
            }
            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    slideInVertically { -it } togetherWith slideOutVertically { it }
                }
            ) { char ->
                Text(
                    text = char.toString(),
                    fontWeight = fontWeight,
                    softWrap = false
                )
            }
        }
    }
}