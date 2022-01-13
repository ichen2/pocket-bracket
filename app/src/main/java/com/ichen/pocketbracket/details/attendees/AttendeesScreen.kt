package com.ichen.pocketbracket.details

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ichen.pocketbracket.details.attendees.AttendeesViewModel
import com.ichen.pocketbracket.details.components.AttendeeProfile
import com.ichen.pocketbracket.details.components.AttendeesListLoading
import com.ichen.pocketbracket.models.Tournament
import com.ichen.pocketbracket.timeline.TournamentsTimelineViewModel
import com.ichen.pocketbracket.timeline.components.TournamentCard
import com.ichen.pocketbracket.utils.Status
import com.ichen.pocketbracket.utils.openTournamentDetailsScreen

@Composable
fun ColumnScope.AttendeesScreen(
    tournamentSlug: String,
    viewModel: AttendeesViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
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
        when (viewModel.attendees.value.status) {
            Status.ERROR -> {
                Text("Error loading attendees", color = MaterialTheme.colors.onBackground)
            }
            Status.SUCCESS -> {
                Text("No attendees found", color = MaterialTheme.colors.onBackground)
            }
            else -> {
                AttendeesListLoading()
            }
        }
    } else {
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            itemsIndexed(
                items = viewModel.attendees.value.data,
                key = { _, attendee -> attendee.id }) { index, attendee ->
                AttendeeProfile(attendee, tournamentSlug)
                if (index == viewModel.attendees.value.data.size - 1) viewModel.getAttendees(
                    context
                )
            }
            if (viewModel.attendees.value.status == Status.LOADING) {
                item {
                    CircularProgressIndicator(
                        strokeWidth = 4.dp
                    )
                }
            } else if (viewModel.attendees.value.status == Status.ERROR) {
                item {
                    Text(
                        "Could not load additional tournaments",
                        color = MaterialTheme.colors.onBackground
                    )
                }
            }
        }
    }
}