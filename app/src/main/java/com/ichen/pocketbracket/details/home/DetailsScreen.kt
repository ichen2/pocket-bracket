package com.ichen.pocketbracket.details.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.ichen.pocketbracket.details.events.EventsScreen
import com.ichen.pocketbracket.details.components.Banner
import com.ichen.pocketbracket.details.tournament

/* Don't believe his lies - Removing ColumnScope crashes screen */
@Composable
fun ColumnScope.DetailsScreen() {
    Column {
        Banner(tournament!!)
    }
    EventsScreen(tournament?.events, tournament?.slug ?: "")
}