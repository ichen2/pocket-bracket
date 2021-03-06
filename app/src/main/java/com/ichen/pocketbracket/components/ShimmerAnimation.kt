package com.ichen.pocketbracket.components

import androidx.compose.animation.core.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

@Composable
fun ShimmerAnimation(shimmerItem: @Composable (brush: Brush) -> Unit) {
    val shimmerColorShades = listOf(
        MaterialTheme.colors.secondaryVariant,
        MaterialTheme.colors.surface,
        MaterialTheme.colors.secondaryVariant
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
