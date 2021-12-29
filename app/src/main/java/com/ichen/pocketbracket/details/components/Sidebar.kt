package com.ichen.pocketbracket.details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ichen.pocketbracket.details.CurrentTab
import com.ichen.pocketbracket.models.Tournament

@Composable
fun Sidebar(tournament: Tournament, currentTab: MutableState<CurrentTab>, goBack: () -> Unit, toggle: () -> Unit) = Row(Modifier.fillMaxSize()) {
    Column(Modifier.width(64.dp).fillMaxHeight().background(MaterialTheme.colors.primary).padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Filled.Home, contentDescription = "go home", modifier = Modifier.size(48.dp).clickable { goBack() }, tint = MaterialTheme.colors.onPrimary)
    }
    Column(
        Modifier
            .fillMaxWidth(.8f)
            .fillMaxHeight()
            .background(MaterialTheme.colors.surface)) {

    }
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color.Black.copy(.8f))
            .clickable { toggle() }) {}
}