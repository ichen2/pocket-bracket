package com.ichen.pocketbracket.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ichen.pocketbracket.models.Videogame
import com.ichen.pocketbracket.models.testTournament
import com.ichen.pocketbracket.timeline.components.TimelineHeader
import com.ichen.pocketbracket.timeline.components.TournamentCardView
import com.ichen.pocketbracket.utils.SetComposableFunction

val tournaments by mutableStateOf(listOf(testTournament, testTournament, testTournament))


@Composable
fun ColumnScope.TournamentsTimelineScreen(dialogDisplayed: Boolean, setDialogComposable: SetComposableFunction) = Column(
    Modifier
        .weight(1f)
        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 0.dp)
) {
    val selectedGames : MutableState<List<Videogame>?> = remember { mutableStateOf(null) }
    TimelineHeader(selectedGames, dialogDisplayed, setDialogComposable)
    Spacer(Modifier.height(16.dp))
    LazyColumn {
        items(tournaments) { tournament ->
            TournamentCardView(tournament = tournament)
        }
    }
}

@Composable
@Preview
fun TournamentsTimelineScreenPreview() = Column(Modifier.background(Color.White)) {
    TournamentsTimelineScreen(false) {}
}