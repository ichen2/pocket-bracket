package com.ichen.pocketbracket.details.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ichen.pocketbracket.models.Event
import com.ichen.pocketbracket.utils.SITE_ENDPOINT
import com.ichen.pocketbracket.utils.openBrowser
import com.ichen.pocketbracket.utils.toPrettyString

@Composable
fun EventItem(event: Event) {
    val context = LocalContext.current
    Column(
        Modifier
            .fillMaxWidth()
            .clickable { openBrowser(context, event.url) }
            .padding(8.dp)) {
        if (event.startAt != null) Text(
            event.startAt.toPrettyString(),
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.body1
        )
        Text(
            event.name,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h5
        )
        if (event.videogame != null) Text(
            event.videogame.displayName,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.body2
        )
    }
}