package com.ichen.pocketbracket.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.ichen.pocketbracket.details.components.EventItem
import com.ichen.pocketbracket.models.Event
import com.ichen.pocketbracket.models.Tournament

@Composable
fun LazyItemScope.EventsScreen(events: List<Event>?) = Column {
    if(events != null) {
        events.sortedBy { event -> event.startAt }.forEach { event ->
            EventItem(event)
        }
    } else {
        Text("No events found", color = MaterialTheme.colors.onBackground)
    }
}