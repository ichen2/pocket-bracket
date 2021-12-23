package com.ichen.pocketbracket.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ichen.pocketbracket.models.Tournament
import com.ichen.pocketbracket.models.testTournament

@Composable
fun TournamentDetailsScreen(tournament: Tournament) {
    Column(modifier = Modifier.fillMaxSize()) {

    }
}

@Preview
@Composable
fun TournamentDetailsScreenPreview() {
    TournamentDetailsScreen(testTournament)
}