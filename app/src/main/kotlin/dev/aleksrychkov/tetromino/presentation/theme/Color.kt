@file:Suppress("MagicNumber")

package dev.aleksrychkov.tetromino.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val primaryBlue = Color(0xFF2196F3)

internal val Scheme = darkColorScheme(
    primary = primaryBlue,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF222223),
    onPrimaryContainer = Color(0xFFFFFFFF),

    secondary = Color(0xFF222223),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF3B4852),
    onSecondaryContainer = Color(0xFFD6E4ED),

    background = Color.Transparent, // use android:window
    onBackground = Color(0xFFFFFFFF),

    surface = Color.Transparent, // use android:window
    onSurface = Color(0xFFFFFFFF),
    surfaceContainer = Color(0xFF191919),
    surfaceContainerHighest = Color(0xFF383838), // Card container color
    surfaceContainerLow = Color(0xFF383838), // Card container color

    surfaceVariant = Color.Transparent,
    onSurfaceVariant = Color(0xFFCAC4D0),

    outline = Color(0xFF323232),
    error = Color(0xFFE58776),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
)

@Stable
internal val ColorRed = Color(0xFFCA543E)

@Stable
internal val ColorMagenta = Color(0xFFC15F87)

@Stable
internal val ColorGreen = Color(0xFF749865)

@Stable
internal val ColorYellow = Color(0xFFDBAB44)

@Stable
internal val ColorOrange = Color(0xFFD39233)

@Stable
internal val ColorBlue = Color(0xFF293B50)

@Stable
internal val ColorLightBlue = Color(0xFF528E91)

@Stable
internal val RetroBrush = Brush.horizontalGradient(
    listOf(
        ColorLightBlue,
        ColorBlue,
        ColorYellow,
        ColorOrange,
        ColorRed,
        ColorMagenta,
        ColorGreen,
    )
)
