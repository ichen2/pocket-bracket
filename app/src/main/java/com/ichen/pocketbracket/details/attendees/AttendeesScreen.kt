package com.ichen.pocketbracket.details

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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

/*

The current implementation fetches all attendees and then does client side searching.
This works well for 99% of all tournaments, but it can be very slow with a massive tournament like Genesis 8.
Presumably it would also be pretty slow on slower internet connections.

The alternative would be to load attendees as the user scrolls through the list of attendees.
With this approach, searching would have to be done server side by making getParticipants calls.
This would be much slower for smaller tournaments but much faster for larger ones.

TODO: Ideally, I think these two approaches should be combined (check the number of attendees, and use approach A or B depending on how many there are)

 */

@Composable
fun ColumnScope.AttendeesScreen(
    tournamentSlug: String,
    viewModel: AttendeesViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    var searchPhrase by remember { mutableStateOf("") }
    DisposableEffect(key1 = viewModel) { // TODO: make sure this effect is right
        if (viewModel.attendees.value.status == Status.NOT_STARTED) {
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
                AttendeesListLoading(numItems = 10)
            }
        }
    } else {
        TextField(
            value = searchPhrase,
            modifier = Modifier
                .fillMaxWidth(1f)
                .clip(MaterialTheme.shapes.small)
                .padding(horizontal = 8.dp, vertical = 16.dp),
            onValueChange = { newSearchPhrase -> searchPhrase = newSearchPhrase },
            placeholder = { Text("Search") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            itemsIndexed(
                items = viewModel.attendees.value.data.filter { attendee ->
                    attendee.tag.lowercase().contains(searchPhrase.lowercase())
                },
                key = { _, attendee -> attendee.id }) { index, attendee ->
                AttendeeProfile(attendee, tournamentSlug)
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
                        "Could not load additional attendees",
                        color = MaterialTheme.colors.onBackground
                    )
                }
            }
        }
    }
}