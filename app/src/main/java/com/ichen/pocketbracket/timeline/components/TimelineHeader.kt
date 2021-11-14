package com.ichen.pocketbracket.timeline.components

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.material.datepicker.MaterialDatePicker
import com.ichen.pocketbracket.models.*
import com.ichen.pocketbracket.timeline.TournamentsTimelineViewModel
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
    viewModel: TournamentsTimelineViewModel,
) = Surface(
    color = MaterialTheme.colors.primarySurface,
    contentColor = if(isSystemInDarkTheme()) MaterialTheme.colors.onSurface else MaterialTheme.colors.onPrimary,
    modifier = Modifier.fillMaxWidth(1f)
) {

    val context = LocalContext.current
    fun getTournaments() = run {
        viewModel.getTournaments(
            TournamentFilter(
                name = tournamentName.value,
                games = tournamentGames.value,
                location = tournamentLocationRadius.value,
                dates = tournamentDateRange.value,
                type = tournamentType.value,
                price = tournamentPrice.value,
                registration = tournamentRegistrationStatus.value
            ),
            context
        )
    }

    Column(Modifier.padding(vertical = 16.dp)) {
        TextField(
            enabled = clickable,
            value = tournamentName.value,
            onValueChange = { value ->
                tournamentName.value = value
                // TODO: Implement caching for tournament name queries
                getTournaments()
            },
            placeholder = { Text("Tournament Name", color = MaterialTheme.colors.onSurface.copy(alpha = .5f)) },
            modifier = Modifier
                .fillMaxWidth(1f)
                .clip(MaterialTheme.shapes.small)
                .padding(horizontal = 16.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = MaterialTheme.colors.onSurface,
                cursorColor = MaterialTheme.colors.onSurface,
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
            ) {
                setDialogComposable {
                    ChooseGamesDialog(setDialogComposable, tournamentGames) {
                        getTournaments()
                    }
                }
            }
            FilterPill(
                "Location",
                tournamentLocationRadius.value != null,
                clickable
            ) {
                setDialogComposable {
                    LocationPicker(
                        onNegativeButtonClick = { setDialogComposable(null) },
                        onPositiveButtonClick = { locationRadius ->
                            tournamentLocationRadius.value = locationRadius
                            setDialogComposable(null)
                            getTournaments()
                        })
                }
            }
            FilterPill(
                tournamentDateRange.value?.toString() ?: "Dates",
                tournamentDateRange.value != null,
                clickable
            ) {
                initializeDateRangePickerListeners(tournamentDateRange) {
                    getTournaments()
                }
                showDateRangePicker(context)
            }
            FilterPill(
                tournamentType.value.toString(),
                tournamentType.value != TournamentType.NO_FILTER,
                clickable
            ) {
                tournamentType.value = getNextEnumValue(tournamentType.value)
                getTournaments()
            }
            FilterPill(
                tournamentPrice.value.toString(),
                tournamentPrice.value != TournamentPrice.NO_FILTER,
                clickable
            ) {
                tournamentPrice.value = getNextEnumValue(tournamentPrice.value)
                getTournaments()
            }
            FilterPill(
                tournamentRegistrationStatus.value.toString(),
                tournamentRegistrationStatus.value != TournamentRegistrationStatus.NO_FILTER,
                clickable
            ) {
                tournamentRegistrationStatus.value =
                    getNextEnumValue(tournamentRegistrationStatus.value)
                getTournaments()
            }
            Text(
                text = "Clear",
                fontWeight = FontWeight.SemiBold,
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

private fun initializeDateRangePickerListeners(tournamentDateRange: MutableState<DateRange?>, onPositiveButtonClick: () -> Unit) {
    dateRangePicker.addOnPositiveButtonClickListener { _ ->
        val startDate = dateRangePicker.selection?.first
        val endDate = dateRangePicker.selection?.second
        tournamentDateRange.value = if (startDate != null && endDate != null) DateRange(
            Date(startDate),
            Date(endDate)
        ) else null
        onPositiveButtonClick()
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