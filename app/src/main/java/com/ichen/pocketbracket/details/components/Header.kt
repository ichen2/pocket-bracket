package com.ichen.pocketbracket.details.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import coil.size.Scale
import com.ichen.pocketbracket.R
import com.ichen.pocketbracket.models.Tournament

@OptIn(ExperimentalCoilApi::class)
@Composable
fun Header(tournament: Tournament, clickable: Boolean, toggle: () -> Unit) = Row(
    modifier = Modifier.fillMaxWidth().background(MaterialTheme.colors.primarySurface).padding(8.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp),
) {
    val contentColor = if (isSystemInDarkTheme()) MaterialTheme.colors.onSurface else MaterialTheme.colors.onPrimary
    Icon(
        imageVector = Icons.Filled.Menu,
        contentDescription = "menu",
        modifier = Modifier.size(36.dp).clickable(enabled = clickable) { toggle() },
        tint = contentColor,
    )
    if (tournament.primaryImageUrl != null) {
        Image(
            painter = rememberImagePainter(data = tournament.primaryImageUrl, builder = {
                size(OriginalSize)
                scale(Scale.FILL)
                placeholder(R.drawable.image_unavailable)
                crossfade(true)
            }),
            contentDescription = "tournament image",
            modifier = Modifier.size(40.dp),
            contentScale = ContentScale.Crop,
        )
    }
    Text(text = tournament.name, style = MaterialTheme.typography.h5, color = contentColor)
}