package com.ichen.pocketbracket.timeline.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FilterPill(text: String, enabled: Boolean = false, onClick: () -> Unit) {
    Text(
        text = text,
        modifier = Modifier
            .clip(CircleShape)
            .background(if(enabled) MaterialTheme.colors.primary else MaterialTheme.colors.surface)
            .then(if(!enabled) Modifier.border(1.dp, Color.White, shape = CircleShape) else Modifier)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = if(enabled) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
    )
}