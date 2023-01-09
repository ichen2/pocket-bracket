package com.ichen.pocketbracket.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.ichen.pocketbracket.components.ErrorSplash
import com.ichen.pocketbracket.models.*
import com.ichen.pocketbracket.timeline.components.TimelineHeader
import com.ichen.pocketbracket.timeline.components.TournamentCard
import com.ichen.pocketbracket.timeline.components.TournamentsListLoading
import com.ichen.pocketbracket.utils.SetComposableFunction
import com.ichen.pocketbracket.utils.Status
import com.ichen.pocketbracket.utils.openTournamentDetailsScreen
import java.util.*

val today = Date()

@ExperimentalPermissionsApi
@Composable
fun ColumnScope.TournamentsTimelineScreen(
    clickable: Boolean,
    setDialogComposable: SetComposableFunction,
    viewModel: TournamentsTimelineViewModel = viewModel()
) = Column(
    Modifier
        .weight(1f)
        .fillMaxWidth(1f),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
) {
    val context = LocalContext.current
    val tournamentName = remember { mutableStateOf("") }
    val tournamentGames: MutableState<List<Videogame>?> = remember { mutableStateOf(null) }
    val tournamentLocationRadius: MutableState<LocationRadius?> = remember { mutableStateOf(null) }
    val tournamentDateRange: MutableState<DateRange?> = remember { mutableStateOf(null) }//DateRange(today, Date(today.time + SECONDS_IN_DAY * 14 * 1000))) }
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
        viewModel.getTournaments(context = context)
    }

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

    DisposableEffect(key1 = viewModel) {
        if (viewModel.tournaments.value.status != Status.SUCCESS) {
            getTournaments()
        }
        onDispose {
            viewModel.cleanup()
        }
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
        viewModel,
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .background(MaterialTheme.colors.background)
    ) {
        if (viewModel.tournaments.value.data.isEmpty()) {
            if (viewModel.tournaments.value.status == Status.ERROR) {
                ErrorSplash("Error fetching tournaments from start.gg")
            } else if (viewModel.tournaments.value.status == Status.SUCCESS) {
                ErrorSplash("No tournaments found", isCritical = false)
            } else {
                TournamentsListLoading()
            }
        } else {
            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top, modifier = Modifier.fillMaxSize()) {
                itemsIndexed(
                    items = viewModel.tournaments.value.data,
                    key = { _, tournament -> tournament.id }) { index, tournament ->
                    // ERROR: if no new tournaments, repeatedly tries to fetch new ones anyways
                    if (index == viewModel.tournaments.value.data.size - 1) viewModel.getMoreTournaments(
                        context
                    )
                    TournamentCard(tournament, clickable) { url ->
                        openTournamentDetailsScreen(context, tournament)
                    }
                }
                if (viewModel.tournaments.value.status == Status.LOADING) {
                    item {
                        CircularProgressIndicator(
                            strokeWidth = 4.dp
                        )
                    }
                } else if (viewModel.tournaments.value.status == Status.ERROR) {
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
}

@ExperimentalPermissionsApi
@Composable
@Preview
fun TournamentsTimelineScreenPreview() = Column(Modifier.background(Color.White)) {
    TournamentsTimelineScreen(false, {})
}