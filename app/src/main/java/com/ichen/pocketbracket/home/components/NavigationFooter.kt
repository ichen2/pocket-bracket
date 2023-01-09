package com.ichen.pocketbracket.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.ichen.pocketbracket.home.CurrentTab

@Composable
fun NavigationFooter(currentTab: MutableState<CurrentTab>, clickable: Boolean = true) =
    Column(Modifier.shadow(20.dp)) {
        Row(
            Modifier
                .background(MaterialTheme.colors.surface)
                .fillMaxWidth()
        ) {
            FooterItem(Icons.Filled.Search, "search", currentTab.value == CurrentTab.TournamentsTimeline, clickable) { currentTab.value = CurrentTab.TournamentsTimeline}
            FooterItem(Icons.Filled.EmojiEvents, "tournaments", currentTab.value == CurrentTab.MyTournaments, clickable) { currentTab.value = CurrentTab.MyTournaments}
            FooterItem(Icons.Filled.Person, "profile", currentTab.value == CurrentTab.MyProfile, clickable) { currentTab.value = CurrentTab.MyProfile}
        }

    }

@Composable
fun RowScope.FooterItem(icon: ImageVector, contentDescription: String, selected: Boolean, clickable: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .weight(1f)
            .clickable(enabled = clickable) {
                onClick()
            }, contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.secondary,
            modifier = Modifier
                .padding(16.dp)
                .size(32.dp)
        )
    }
}