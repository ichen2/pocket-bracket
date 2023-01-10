package com.ichen.pocketbracket.timeline.components

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
    filter: TournamentFilter,
    setFilter: (TournamentFilter) -> Unit,
    clickable: Boolean,
    setDialogComposable: SetComposableFunction,
) = Surface(
    color = MaterialTheme.colors.primarySurface,
    contentColor = if (isSystemInDarkTheme()) MaterialTheme.colors.onSurface else MaterialTheme.colors.onPrimary,
    modifier = Modifier.fillMaxWidth(1f)
) {

    val context = LocalContext.current

    val textColor =
        if (isSystemInDarkTheme()) MaterialTheme.colors.onSurface else MaterialTheme.colors.onPrimary

    Column(Modifier.padding(vertical = 16.dp)) {
        TextField(
            enabled = clickable,
            value = filter.name,
            onValueChange = { newName ->
                setFilter(filter.copy(name = newName))
                // TODO: Implement caching for tournament name queries
            },
            placeholder = {
                Text(
                    "Tournament Name",
                    color = textColor.copy(alpha = .5f)
                )
            },
            modifier = Modifier
                .fillMaxWidth(1f)
                .clip(MaterialTheme.shapes.small)
                .padding(horizontal = 16.dp),
            colors = TextFieldDefaults.textFieldColors(
                textColor = textColor,
                cursorColor = textColor,
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
                text = "Games",
                enabled = (filter.games?.size ?: 0) > 0,
                clickable = clickable
            ) {
                if(filter.games == null) {
                    setDialogComposable {
                        ChooseGamesDialog(
                            onPositiveButtonClick = { videogames ->
                                setFilter(filter.copy(games = videogames))
                                setDialogComposable(null)
                            },
                            onNegativeButtonClick = { setDialogComposable(null) },
                        )
                    }
                } else {
                    setFilter(filter.copy(games = null))
                }
            }
            FilterPill(
                text = "Location",
                enabled = filter.location != null,
                clickable = clickable,
            ) {
                if (filter.location == null) {
                    setDialogComposable {
                        LocationPicker(
                            onPositiveButtonClick = { locationRadius ->
                                setFilter(filter.copy(location = locationRadius))
                                setDialogComposable(null)
                            },
                            onNegativeButtonClick = { setDialogComposable(null) },
                        )

                    }
                } else {
                    setFilter(filter.copy(location = null))
                }
            }
            FilterPill(
                text = filter.dates?.toString() ?: "Dates",
                enabled = filter.dates != null,
                clickable = clickable,
            ) {
                if (filter.dates == null) {
                    initializeDateRangePickerListeners { dates ->
                        setFilter(filter.copy(dates = dates))
                    }
                    showDateRangePicker(context)
                } else {
                    setFilter(filter.copy(dates = null))
                }
            }
            FilterPill(
                text = filter.type.toString(),
                enabled = filter.type != TournamentType.NO_FILTER,
                clickable = clickable,
            ) {
                setFilter(filter.copy(type = getNextEnumValue(filter.type)))
            }
            FilterPill(
                text = filter.price.toString(),
                enabled = filter.price != TournamentPrice.NO_FILTER,
                clickable = clickable,
            ) {
                setFilter(filter.copy(price = getNextEnumValue(filter.price)))
            }
            FilterPill(
                text = filter.registration.toString(),
                enabled = filter.registration != TournamentRegistrationStatus.NO_FILTER,
                clickable = clickable,
            ) {
                setFilter(filter.copy(registration = getNextEnumValue(filter.registration)))
            }
            Text(
                text = "Clear",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.error)
                    .clickable(onClick = { setFilter(TournamentFilter()) })
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                color = MaterialTheme.colors.onError
            )
        }
    }
}

private fun initializeDateRangePickerListeners(
    setDates: (DateRange?) -> Unit,
) {
    dateRangePicker.addOnPositiveButtonClickListener { _ ->
        val timezone = TimeZone.getDefault()
        val startDate = dateRangePicker.selection?.first
        val endDate = dateRangePicker.selection?.second

        /*
            the times picked by the dateRangePicker are in UTC.
            by subtracting the offset from the current timezone to UTC,
            we can convert these times from UTC to the same time in local time.
            ex:
            In central time, timezone.rawOffset is -6 hours, or  -21600000 milliseconds
            If the user picks a startDate of 12am January 1st 2020, that time is 1577836800000
            However, in central time that is 6pm December 31st
            So, to convert the first UTC time to central time, we can add -timezone.rawOffset
            The result is 1577836800000 + 21600000 = 1577858400000,
            which is 12am January 1st 2020 in central time
        */

        setDates(
            if (startDate != null && endDate != null) DateRange(
                Date(startDate - timezone.rawOffset),
                Date(endDate - timezone.rawOffset),
            ) else null
        )
    }
    dateRangePicker.addOnNegativeButtonClickListener {
        setDates(null)
    }
    dateRangePicker.addOnCancelListener {
        setDates(null)
    }
}

const val DATE_RANGE_PICKER_TAG = "dateRangePicker"

private fun showDateRangePicker(context: Context) {
    if (context is AppCompatActivity) {
        if(context.supportFragmentManager.findFragmentByTag(DATE_RANGE_PICKER_TAG) == null) {
            dateRangePicker.show(context.supportFragmentManager, DATE_RANGE_PICKER_TAG)
        }
    } else {
        Toast.makeText(context, "Error displaying date picker", Toast.LENGTH_SHORT).show()
    }
}