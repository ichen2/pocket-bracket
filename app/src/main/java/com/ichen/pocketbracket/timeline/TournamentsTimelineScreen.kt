package com.ichen.pocketbracket.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.ichen.pocketbracket.components.ErrorSplash
import com.ichen.pocketbracket.timeline.components.TimelineHeader
import com.ichen.pocketbracket.timeline.components.TournamentCard
import com.ichen.pocketbracket.timeline.components.TournamentsListLoading
import com.ichen.pocketbracket.utils.SetComposableFunction
import com.ichen.pocketbracket.utils.Status
import com.ichen.pocketbracket.utils.openTournamentDetailsScreen

@ExperimentalPermissionsApi
@Composable
fun ColumnScope.TournamentsTimelineScreen(
    clickable: Boolean,
    setDialogComposable: SetComposableFunction,
    viewModel: TournamentsTimelineViewModel = viewModel(),
    locationProvider: FusedLocationProviderClient,
) = Column(
    modifier = Modifier
        .weight(1f)
        .fillMaxWidth(1f),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
) {
    val context = LocalContext.current

    DisposableEffect(key1 = viewModel) {
        viewModel.onCreated(context)
        onDispose { viewModel.onCleared() }
    }

    TimelineHeader(
        filter = viewModel.filter,
        setFilter = { newFilter ->
            viewModel.filter = newFilter
            viewModel.getTournaments(newFilter = newFilter, context = context)
        },
        clickable = clickable,
        setDialogComposable = setDialogComposable,
        locationProvider = locationProvider,
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .background(MaterialTheme.colors.background)
    ) {
        if (viewModel.tournaments.data.isEmpty()) {
            when (viewModel.tournaments.status) {
                Status.ERROR -> ErrorSplash(message = "Could not fetching tournaments from start.gg", isCritical = true,)
                Status.SUCCESS -> ErrorSplash(message = "No tournaments found")
                else -> TournamentsListLoading()
            }
        } else {
            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top, modifier = Modifier.fillMaxSize()) {
                itemsIndexed(
                    items = viewModel.tournaments.data,
                    key = { _, tournament -> tournament.id },
                ) { index, tournament ->
                    // ERROR: if no new tournaments, repeatedly tries to fetch new ones anyways
                    if (index == viewModel.tournaments.data.size - 1) viewModel.getTournaments(context = context)
                    TournamentCard(tournament, clickable) {
                        openTournamentDetailsScreen(context, tournament)
                    }
                }
                if (viewModel.tournaments.status == Status.LOADING) {
                    item {
                        CircularProgressIndicator(
                            strokeWidth = 4.dp
                        )
                    }
                } else if (viewModel.tournaments.status == Status.ERROR) {
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
