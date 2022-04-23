package com.ichen.pocketbracket.tournaments

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ichen.pocketbracket.components.ErrorSplash
import com.ichen.pocketbracket.timeline.components.TournamentCard
import com.ichen.pocketbracket.timeline.components.TournamentsListLoading
import com.ichen.pocketbracket.utils.SetComposableFunction
import com.ichen.pocketbracket.utils.Status
import com.ichen.pocketbracket.utils.openTournamentDetailsScreen

@Composable
fun ColumnScope.MyTournamentsScreen(
    setDialogComposable: SetComposableFunction,
    viewModel: MyTournamentsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) = Column(Modifier.weight(1f)) {

    val context = LocalContext.current

    DisposableEffect(key1 = viewModel) {
        if(viewModel.tournaments.value.status != Status.SUCCESS) viewModel.getEvents(context = context)
        onDispose {
            viewModel.cleanup()
        }
    }

    Box(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primarySurface)
            .padding(16.dp), contentAlignment = Alignment.Center) {
        Text("My Tournaments", style = MaterialTheme.typography.h4, color = if(isSystemInDarkTheme()) MaterialTheme.colors.onSurface else MaterialTheme.colors.onPrimary)
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .background(MaterialTheme.colors.background)
    ) {
        if (viewModel.tournaments.value.data.isEmpty()) {
            if(viewModel.tournaments.value.status == Status.ERROR) {
                ErrorSplash("Error fetching your tournaments from smash.gg")
            }
            else if (viewModel.tournaments.value.status == Status.SUCCESS) {
                ErrorSplash("No tournaments found", isCritical = false)
            } else {
                TournamentsListLoading(viewModel.tournaments.value.data.size)
            }
        } else {
            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top, modifier = Modifier.fillMaxSize()) {
                itemsIndexed(
                    items = viewModel.tournaments.value.data,
                    key = { _, tournament -> tournament.events?.getOrNull(0)?.id  ?: -1}) { index, tournament ->
                    if (index == viewModel.tournaments.value.data.size - 1) {
                        viewModel.getMoreEvents(context)
                    }
                    TournamentCard(tournament, true) { url ->
                        openTournamentDetailsScreen(context, tournament)
                        /*
                        setDialogComposable {
                            WebView(url) {
                                setDialogComposable(null)
                            }
                        }
                        */
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