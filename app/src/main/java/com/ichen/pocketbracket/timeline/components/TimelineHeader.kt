package com.ichen.pocketbracket.timeline.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.material.datepicker.MaterialDatePicker
import com.ichen.pocketbracket.models.TournamentPrice
import com.ichen.pocketbracket.models.TournamentRegistrationStatus
import com.ichen.pocketbracket.models.TournamentType
import com.ichen.pocketbracket.models.Videogame
import com.ichen.pocketbracket.utils.LocationRadius
import com.ichen.pocketbracket.utils.SetComposableFunction
import com.ichen.pocketbracket.utils.getCenterAsString
import com.ichen.pocketbracket.utils.getNextEnumValue
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TimelineHeader(
    selectedGames: MutableState<List<Videogame>?>,
    searchFieldText: MutableState<String>,
    tournamentLocationRadius: MutableState<LocationRadius?>,
    tournamentType: MutableState<TournamentType>,
    tournamentPrice: MutableState<TournamentPrice>,
    tournamentRegistrationStatus: MutableState<TournamentRegistrationStatus>,
    tournamentDateRange: MutableState<Pair<Date, Date>?>,
    showDateRangePicker: () -> Unit,
    clickable: Boolean,
    setDialogComposable: SetComposableFunction,
    clearFilters: () -> Unit,
) = Surface(
    color = MaterialTheme.colors.primary,
    contentColor = MaterialTheme.colors.onPrimary,
    modifier = Modifier.fillMaxWidth(1f)
) {

    val formattedDateRange: String? = if (tournamentDateRange.value == null) null else {
        val sdf = SimpleDateFormat("MM/dd")
        val formattedStartDate = sdf.format(tournamentDateRange.value!!.first)
        val formattedEndDate = sdf.format(tournamentDateRange.value!!.second)
        "$formattedStartDate - $formattedEndDate"
    }

    Column(Modifier.padding(vertical = 16.dp)) {
        TextField(
            value = searchFieldText.value,
            onValueChange = { searchFieldText.value = it },
            modifier = Modifier
                .fillMaxWidth(1f)
                .clip(MaterialTheme.shapes.small)
                .padding(horizontal = 16.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
        Spacer(Modifier.height(16.dp))
        Row(
            Modifier
                .fillMaxWidth(1f)
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FilterPill(
                "Games",
                selectedGames.value != null,
                clickable
            ) { // sheet with checkbox list of games
                setDialogComposable {
                    ChooseGamesDialog(setDialogComposable, selectedGames)
                }
            }
            FilterPill(tournamentLocationRadius.value?.getCenterAsString() ?: "Location", tournamentLocationRadius.value != null, clickable) { // sheet with location selction
                setDialogComposable {
                    LocationPicker(
                        onNegativeButtonClick = { setDialogComposable(null) },
                        onPositiveButtonClick = { locationRadius ->
                            tournamentLocationRadius.value = locationRadius
                            setDialogComposable(null)
                        })
                }
            }
            FilterPill(
                formattedDateRange ?: "Dates",
                tournamentDateRange.value != null,
                clickable
            ) { // sheet with date selection
                showDateRangePicker()
            }
            FilterPill(
                tournamentType.value.toString(),
                tournamentType.value != TournamentType.NO_FILTER,
                clickable
            ) { // dropdown with online, offline, both
                tournamentType.value = getNextEnumValue(tournamentType.value)
            }
            FilterPill(
                tournamentPrice.value.toString(),
                tournamentPrice.value != TournamentPrice.NO_FILTER,
                clickable
            ) { // dropdown with online, offline, both
                tournamentPrice.value = getNextEnumValue(tournamentPrice.value)
            }
            FilterPill(
                tournamentRegistrationStatus.value.toString(),
                tournamentRegistrationStatus.value != TournamentRegistrationStatus.NO_FILTER,
                clickable
            ) { // dropdown with online, offline, both
                tournamentRegistrationStatus.value =
                    getNextEnumValue(tournamentRegistrationStatus.value)
            }
            Text(
                text = "Clear",
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.error)
                    .clickable(onClick = clearFilters)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                color = MaterialTheme.colors.onError
            )
        }
    }
}