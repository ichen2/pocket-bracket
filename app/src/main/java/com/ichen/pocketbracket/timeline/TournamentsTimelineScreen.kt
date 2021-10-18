package com.ichen.pocketbracket.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.ichen.pocketbracket.components.ShimmerAnimation
import com.ichen.pocketbracket.components.WebView
import com.ichen.pocketbracket.models.*
import com.ichen.pocketbracket.timeline.components.TimelineHeader
import com.ichen.pocketbracket.timeline.components.TournamentCard
import com.ichen.pocketbracket.timeline.components.TournamentCardLoading
import com.ichen.pocketbracket.utils.SetComposableFunction
import com.ichen.pocketbracket.utils.Status
import java.lang.Integer.max

@ExperimentalPermissionsApi
@Composable
fun ColumnScope.TournamentsTimelineScreen(
    clickable: Boolean,
    setDialogComposable: SetComposableFunction,
    viewModel: TournamentsTimelineViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) = Column(
    Modifier
        .weight(1f)
        .fillMaxWidth(1f),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
) {
    val context = LocalContext.current
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
        viewModel.getTournaments(context = context)
    }

    DisposableEffect(key1 = viewModel) {
        if(viewModel.tournaments.value.status != Status.SUCCESS) viewModel.getTournaments(context = context)
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
            if (viewModel.tournaments.value.status == Status.ERROR || viewModel.tournaments.value.status == Status.SUCCESS) {
                Text("No Tournaments Founds", color = MaterialTheme.colors.onBackground)
            } else {
                TournamentsTimelineScreenLoading(viewModel.tournaments.value.data.size)
            }
        } else {
            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                itemsIndexed(
                    items = viewModel.tournaments.value.data,
                    key = { _, tournament -> tournament.id }) { index, tournament ->
                    // ERROR: if no new tournaments, repeatedly tries to fetch new ones anyways
                    if (index == viewModel.tournaments.value.data.size - 1) viewModel.getMoreTournaments(
                        context
                    )
                    TournamentCard(tournament) { url ->
                        setDialogComposable {
                            WebView(url) {
                                setDialogComposable(null)
                            }
                        }
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

@Composable
fun TournamentsTimelineScreenLoading(numItems: Int) = Column(
    Modifier.verticalScroll(
        rememberScrollState()
    )
) {
    for (i in 0..max(numItems, 2)) {
        ShimmerAnimation { brush ->
            TournamentCardLoading(brush = brush)
        }
    }
}

@ExperimentalPermissionsApi
@Composable
@Preview
fun TournamentsTimelineScreenPreview() = Column(Modifier.background(Color.White)) {
    TournamentsTimelineScreen(false, {})
}