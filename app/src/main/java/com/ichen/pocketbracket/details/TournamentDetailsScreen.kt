package com.ichen.pocketbracket.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import coil.size.Scale
import com.ichen.pocketbracket.R
import com.ichen.pocketbracket.models.Tournament
import com.ichen.pocketbracket.models.testTournament

@Composable
fun TournamentDetailsScreen(tournament: Tournament) {
    Column(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = rememberImagePainter(data = tournament.secondaryImageUrl, builder = {
                size(OriginalSize)
                scale(Scale.FILL)
                placeholder(R.drawable.image_unavailable)
            }),
            contentDescription = "tournament image",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth,
        )
        Text(tournament.name)
    }
}

@Preview
@Composable
fun TournamentDetailsScreenPreview() {
    TournamentDetailsScreen(testTournament)
}