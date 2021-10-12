package com.ichen.pocketbracket.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.ichen.pocketbracket.ui.theme.lightBlack
import com.ichen.pocketbracket.ui.theme.lightWhite
import com.ichen.pocketbracket.ui.theme.medBlack
import com.ichen.pocketbracket.ui.theme.medWhite

@Composable
fun ShimmerAnimation(shimmerItem: @Composable (brush: Brush) -> Unit) {
    val shimmerColorShades = if (isSystemInDarkTheme()) listOf(
        medBlack,
        lightBlack,
        medBlack,
    ) else listOf(
        medWhite,
        lightWhite,
        medWhite,
    )

    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        )
    )
    val brush = Brush.linearGradient(
        colors = shimmerColorShades,
        start = Offset(10f, 10f),
        end = Offset(translateAnim, translateAnim)
    )
    shimmerItem(brush = brush)
}
