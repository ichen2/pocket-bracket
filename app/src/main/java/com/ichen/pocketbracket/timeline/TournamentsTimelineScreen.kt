package com.ichen.pocketbracket.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.ichen.pocketbracket.models.*
import com.ichen.pocketbracket.timeline.components.TimelineHeader
import com.ichen.pocketbracket.timeline.components.TournamentCardView
import com.ichen.pocketbracket.utils.SetComposableFunction

val tournaments by mutableStateOf(
    listOf(
        testTournament.copy(id = 0),
        testTournament.copy(id = 1),
        testTournament.copy(id = 2)
    )
)

@ExperimentalPermissionsApi
@Composable
fun ColumnScope.TournamentsTimelineScreen(
    clickable: Boolean,
    setDialogComposable: SetComposableFunction,
    viewModel: TournamentsTimelineViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

) = Column(
    Modifier
        .weight(1f)
        .fillMaxWidth(1f)
        .background(MaterialTheme.colors.primary)
) {
    val tournamentName = remember { mutableStateOf("") }
    val tournamentGames: MutableState<List<Videogame>?> = remember { mutableStateOf(null) }
    val tournamentLocationRadius: MutableState<LocationRadius?> = remember { mutableStateOf(null) }
    val tournamentDateRange: MutableState<DateRange?> = remember { mutableStateOf(null) }
    val tournamentType = remember { mutableStateOf(TournamentType.NO_FILTER) }
    val tournamentPrice = remember { mutableStateOf(TournamentPrice.NO_FILTER) }
    val tournamentRegistrationStatus =
        remember { mutableStateOf(TournamentRegistrationStatus.NO_FILTER) }
    val clearFilters = {
        tournamentName.value = ""
        tournamentGames.value = null
        tournamentLocationRadius.value = null
        tournamentDateRange.value = null
        tournamentType.value = TournamentType.NO_FILTER
        tournamentPrice.value = TournamentPrice.NO_FILTER
        tournamentRegistrationStatus.value = TournamentRegistrationStatus.NO_FILTER
    }

    TimelineHeader(
        tournamentName,
        tournamentGames,
        tournamentLocationRadius,
        tournamentDateRange,
        tournamentType,
        tournamentPrice,
        tournamentRegistrationStatus,
        clearFilters,
        clickable,
        setDialogComposable,
    )
    LazyColumn(Modifier.clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))) {
        itemsIndexed(
            items = tournaments,
            key = { _, tournament -> tournament.id }) { index, tournament ->
            TournamentCardView(tournament = tournament, first = index == 0)
        }
    }
}

@ExperimentalPermissionsApi
@Composable
@Preview
fun TournamentsTimelineScreenPreview() = Column(Modifier.background(Color.White)) {
    TournamentsTimelineScreen(false, {})
}