package com.ichen.pocketbracket.details.attendees

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ichen.pocketbracket.components.ErrorSplash
import com.ichen.pocketbracket.details.components.AttendeeProfile
import com.ichen.pocketbracket.details.components.AttendeesListLoading
import com.ichen.pocketbracket.utils.Status

/*

The current implementation fetches all attendees and then does client side searching.
This works well for 99% of all tournaments, but it can be very slow with a massive tournament like Genesis 8.
Presumably it would also be pretty slow on slower internet connections.

The alternative would be to load attendees as the user scrolls through the list of attendees.
With this approach, searching would have to be done server side by making getParticipants calls.
This would be much slower for smaller tournaments but much faster for larger ones.

TODO: Ideally, I think these two approaches should be combined (check the number of attendees, and use approach A or B depending on how many there are)

 */

/* Don't believe his lies - Removing ColumnScope crashes screen */
@Composable
fun ColumnScope.AttendeesScreen(
    tournamentSlug: String,
    viewModel: AttendeesViewModel = viewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var searchPhrase by remember { mutableStateOf("") }
    DisposableEffect(key1 = viewModel) { // TODO: make sure this effect is right
        if (viewModel.attendees.value.status == Status.NOT_STARTED) {
            viewModel.getAttendees(context)
        }
        onDispose {
            viewModel.cleanup()
        }
    }
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
            unfocusedIndicatorColor = Color.Transparent,
            textColor = MaterialTheme.colors.onSurface,
            backgroundColor = MaterialTheme.colors.surface,
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
    )
    val filterData = viewModel.attendees.value.data.filter { attendee ->
        attendee.tag.lowercase().contains(searchPhrase.lowercase())
    }
    if (filterData.isEmpty()) {
        when (viewModel.attendees.value.status) {
            Status.ERROR -> {
                ErrorSplash(
                    message = "Error loading attendees",
                    isCritical = true,
                )
            }
            Status.SUCCESS -> {
                ErrorSplash("No attendees found")
            }
            else -> {
                AttendeesListLoading(numItems = 10)
            }
        }
    } else {
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
            itemsIndexed(
                items = filterData,
                key = { _, attendee -> attendee.id })
            { _, attendee ->
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
                        text = "Could not load additional attendees",
                        color = MaterialTheme.colors.onBackground
                    )
                }
            }
        }
    }
}