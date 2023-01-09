package com.ichen.pocketbracket.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = lightPurple,
    primaryVariant = darkPurple,
    onPrimary = darkBlack,
    secondary = darkGrey,
    secondaryVariant = lightBlack,
    onSecondary = lightWhite,
    surface = medBlack,
    onSurface = lightWhite,
    background = darkBlack,
    onBackground = lightWhite,
    error = medRed,
    onError = lightWhite,
)

private val LightColorPalette = lightColors(
    primary = medPurple,
    primaryVariant = darkPurple,
    onPrimary = lightWhite,
    secondary = darkGrey,
    secondaryVariant = medWhite,
    onSecondary = darkBlack,
    surface = lightWhite,
    onSurface = darkBlack,
    background = lightWhite,
    onBackground = darkBlack,
    error = medRed,
    onError = lightWhite,
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
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}