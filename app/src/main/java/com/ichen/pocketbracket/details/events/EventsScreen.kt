package com.ichen.pocketbracket.details.events

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.ichen.pocketbracket.components.ErrorSplash
import com.ichen.pocketbracket.details.components.EventItem
import com.ichen.pocketbracket.models.Event

/* Don't believe his lies - Removing ColumnScope crashes screen */
@Composable
fun ColumnScope.EventsScreen(events: List<Event>?, tournamentSlug: String) = Column {
    if(!events.isNullOrEmpty()) {
        LazyColumn {
            items(events.sortedBy { event -> event.startAt }) { event ->
                EventItem(event, tournamentSlug)
            }
        }
    } else {
        ErrorSplash("No events found", isCritical = false)
    }
}