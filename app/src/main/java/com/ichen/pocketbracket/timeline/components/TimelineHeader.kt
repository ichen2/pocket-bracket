package com.ichen.pocketbracket.timeline.components

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.material.datepicker.MaterialDatePicker
import com.ichen.pocketbracket.models.*
import com.ichen.pocketbracket.utils.SetComposableFunction
import com.ichen.pocketbracket.utils.getNextEnumValue
import java.util.*

private val dateRangePicker =
    MaterialDatePicker.Builder.dateRangePicker()
        .setTitleText("Select dates")
        .build()

@ExperimentalPermissionsApi
@Composable
fun TimelineHeader(
    tournamentName: MutableState<String>,
    tournamentGames: MutableState<List<Videogame>?>,
    tournamentLocationRadius: MutableState<LocationRadius?>,
    tournamentDateRange: MutableState<DateRange?>,
    tournamentType: MutableState<TournamentType>,
    tournamentPrice: MutableState<TournamentPrice>,
    tournamentRegistrationStatus: MutableState<TournamentRegistrationStatus>,
    clearFilters: () -> Unit,
    clickable: Boolean,
    setDialogComposable: SetComposableFunction,
) = Surface(
    color = MaterialTheme.colors.primarySurface,
    contentColor = MaterialTheme.colors.onPrimary,
    modifier = Modifier.fillMaxWidth(1f)
) {

    val context = LocalContext.current

    Column(Modifier.padding(vertical = 16.dp)) {
        TextField(
            value = tournamentName.value,
            onValueChange = { tournamentName.value = it },
            modifier = Modifier
                .fillMaxWidth(1f)
                .clip(MaterialTheme.shapes.small)
                .padding(horizontal = 16.dp),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.onPrimary,
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
                tournamentGames.value != null,
                clickable
            ) { // sheet with checkbox list of games
                setDialogComposable {
                    ChooseGamesDialog(setDialogComposable, tournamentGames)
                }
            }
            FilterPill(
                "Location",
                tournamentLocationRadius.value != null,
                clickable
            ) { // sheet with location selction
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
                tournamentDateRange.value?.toString() ?: "Dates",
                tournamentDateRange.value != null,
                clickable
            ) { // sheet with date selection
                initializeDateRangePickerListeners(tournamentDateRange)
                showDateRangePicker(context)
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

private fun initializeDateRangePickerListeners(tournamentDateRange: MutableState<DateRange?>) {
    dateRangePicker.addOnPositiveButtonClickListener { _ ->
        val startDate = dateRangePicker.selection?.first
        val endDate = dateRangePicker.selection?.second
        tournamentDateRange.value = if (startDate != null && endDate != null) DateRange(
            Date(startDate),
            Date(endDate)
        ) else null
    }
    dateRangePicker.addOnNegativeButtonClickListener {
        tournamentDateRange.value = null
    }
    dateRangePicker.addOnCancelListener {
        tournamentDateRange.value = null
    }
}

private fun showDateRangePicker(context: Context) {
    if (context is AppCompatActivity) {
        dateRangePicker.show(context.supportFragmentManager, null)
    } else {
        Toast.makeText(context, "Error displaying date picker", Toast.LENGTH_SHORT).show()
    }
}