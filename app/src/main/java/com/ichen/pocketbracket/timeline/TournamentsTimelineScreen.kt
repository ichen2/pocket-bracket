package com.ichen.pocketbracket.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.ichen.pocketbracket.components.ShimmerAnimation
import com.ichen.pocketbracket.models.*
import com.ichen.pocketbracket.timeline.components.TimelineHeader
import com.ichen.pocketbracket.timeline.components.TournamentCardView
import com.ichen.pocketbracket.timeline.components.TournamentCardViewLoading
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
        when (viewModel.tournaments.value.status) {
            Status.SUCCESS -> {
                LazyColumn {
                    items(
                        items = viewModel.tournaments.value.data,
                        key = { tournament -> tournament.id }) { tournament ->
                        TournamentCardView(tournament)
                    }
                }
            }
            Status.NOT_STARTED -> {
                // SUS
                viewModel.getTournaments(context = context)
                TournamentsTimelineScreenLoading(viewModel.tournaments.value.data.size)
            }
            Status.LOADING -> {
                TournamentsTimelineScreenLoading(viewModel.tournaments.value.data.size)
            }
            else -> {
                Text("No Tournaments Founds", color = MaterialTheme.colors.onBackground)
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
            TournamentCardViewLoading(brush = brush)
        }
    }
}

@ExperimentalPermissionsApi
@Composable
@Preview
fun TournamentsTimelineScreenPreview() = Column(Modifier.background(Color.White)) {
    TournamentsTimelineScreen(false, {})
}