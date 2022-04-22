package com.ichen.pocketbracket.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.ichen.pocketbracket.components.ErrorSplash
import com.ichen.pocketbracket.details.components.EventItem
import com.ichen.pocketbracket.models.Event
import com.ichen.pocketbracket.models.Tournament

@Composable
fun ColumnScope.EventsScreen(events: List<Event>?, tournamentSlug: String) = Column {
    if(!events.isNullOrEmpty()) {
        events.sortedBy { event -> event.startAt }.forEach { event ->
            EventItem(event, tournamentSlug)
        }
    } else {
        ErrorSplash("No events found", isCritical = false)
    }
}