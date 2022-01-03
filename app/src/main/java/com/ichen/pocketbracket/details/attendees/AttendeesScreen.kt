package com.ichen.pocketbracket.details

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.ichen.pocketbracket.details.attendees.AttendeesViewModel
import com.ichen.pocketbracket.models.Tournament
import com.ichen.pocketbracket.timeline.TournamentsListLoading
import com.ichen.pocketbracket.timeline.TournamentsTimelineViewModel
import com.ichen.pocketbracket.timeline.components.TournamentCard
import com.ichen.pocketbracket.utils.Status
import com.ichen.pocketbracket.utils.openTournamentDetailsScreen

@Composable
fun LazyItemScope.AttendeesScreen(viewModel: AttendeesViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val context = LocalContext.current
    DisposableEffect(key1 = viewModel) { // TODO: make sure this effect is right
        if (viewModel.attendees.value.status != Status.SUCCESS) {
            viewModel.getAttendees(context)
        }
        onDispose {
            viewModel.cleanup()
        }
    }
    if (viewModel.attendees.value.data.isEmpty()) {
        if (viewModel.attendees.value.status == Status.ERROR) {
            Text("Error loading tournaments", color = MaterialTheme.colors.onBackground)
        } else if (viewModel.attendees.value.status == Status.SUCCESS) {
            Text("No tournaments found", color = MaterialTheme.colors.onBackground)
        } else {
            // TODO: Create actual loading indicator
            Text("Loading...", color = MaterialTheme.colors.onBackground)
        }
    } else {
        viewModel.attendees.value.data.forEach { attendee ->
            Text(attendee.tag, color = MaterialTheme.colors.onBackground)
        }
    }
}