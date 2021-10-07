package com.ichen.pocketbracket.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = lightPurple,
    primaryVariant = darkPurple,
    secondary = lightTeal,
    surface = medBlack,
    background = darkBlack,
)

private val LightColorPalette = lightColors(
    primary = medPurple,
    primaryVariant = darkPurple,
    secondary = lightTeal,
    surface = lightWhite,
    background = lightWhite,

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun PocketBracketTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    val outlineColor = if (darkTheme) medWhite else medBlack
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}