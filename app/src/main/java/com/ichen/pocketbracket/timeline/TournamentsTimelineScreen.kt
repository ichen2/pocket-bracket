package com.ichen.pocketbracket.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ichen.pocketbracket.models.*
import com.ichen.pocketbracket.timeline.components.TimelineHeader
import com.ichen.pocketbracket.timeline.components.TournamentCardView
import com.ichen.pocketbracket.utils.LocationRadius
import com.ichen.pocketbracket.utils.SetComposableFunction
import java.util.*

val tournaments by mutableStateOf(
    listOf(
        testTournament.copy(id = 0),
        testTournament.copy(id = 1),
        testTournament.copy(id = 2)
    )
)


@Composable
fun ColumnScope.TournamentsTimelineScreen(
    clickable: Boolean,
    setDialogComposable: SetComposableFunction,
    tournamentDateRange: MutableState<Pair<Date, Date>?>,
    showDateRangePicker: () -> Unit,
    viewModel: TournamentsTimelineViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

) = Column(
    Modifier
        .weight(1f)
        .fillMaxWidth(1f)
        .background(MaterialTheme.colors.primary)
) {
    val searchFieldText = remember { mutableStateOf("") }
    val selectedGames: MutableState<List<Videogame>?> = remember { mutableStateOf(null) }
    val tournamentLocationRadius: MutableState<LocationRadius?> = remember { mutableStateOf(null) }
    val tournamentType = remember { mutableStateOf(TournamentType.NO_FILTER) }
    val tournamentPrice = remember { mutableStateOf(TournamentPrice.NO_FILTER) }
    val tournamentRegistrationStatus =
        remember { mutableStateOf(TournamentRegistrationStatus.NO_FILTER) }
    val clearFilters = {
        selectedGames.value = null
        searchFieldText.value = ""
        tournamentType.value = TournamentType.NO_FILTER
        tournamentPrice.value = TournamentPrice.NO_FILTER
        tournamentLocationRadius.value = null
        tournamentRegistrationStatus.value = TournamentRegistrationStatus.NO_FILTER
        tournamentDateRange.value = null
    }

    TimelineHeader(
        selectedGames,
        searchFieldText,
        tournamentLocationRadius,
        tournamentType,
        tournamentPrice,
        tournamentRegistrationStatus,
        tournamentDateRange,
        showDateRangePicker,
        clickable,
        setDialogComposable,
        clearFilters
    )
    LazyColumn(Modifier.clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))) {
        itemsIndexed(
            items = tournaments,
            key = { _, tournament -> tournament.id }) { index, tournament ->
            TournamentCardView(tournament = tournament, first = index == 0)
        }
    }
}

@Composable
@Preview
fun TournamentsTimelineScreenPreview() = Column(Modifier.background(Color.White)) {
    TournamentsTimelineScreen(false, {}, remember { mutableStateOf(null) }, {})
}