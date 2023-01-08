package com.ichen.pocketbracket.details.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.ichen.pocketbracket.details.events.EventsScreen
import com.ichen.pocketbracket.details.components.Banner
import com.ichen.pocketbracket.details.tournament

@Composable
fun ColumnScope.HomeScreen() {
    Column {
        Banner(tournament!!)
    }
    EventsScreen(tournament?.events, tournament?.slug ?: "")
}