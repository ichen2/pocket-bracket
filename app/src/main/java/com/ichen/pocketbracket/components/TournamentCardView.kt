package com.ichen.pocketbracket.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.ichen.pocketbracket.models.Tournament
import com.ichen.pocketbracket.models.testTournament
import com.ichen.pocketbracket.ui.theme.PocketBracketTheme
import com.ichen.pocketbracket.utils.combineDates
import com.ichen.pocketbracket.utils.toPrettyString

@Composable
fun TournamentCardView(tournament: Tournament) = Column(
    Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .background(MaterialTheme.colors.surface)
) {
    Box {
        Image(
            painter = rememberImagePainter(tournament.imageUrl),
            contentDescription = "tournament image",
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
        Text(
            tournament.state.toString(),
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colors.primary)
                .padding(4.dp)
        )
    }
    Column(Modifier.padding(4.dp)) {
        Text(
            text = tournament.name,
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h4
        )
        Text(
            text = combineDates(tournament.startAt, tournament.endAt),
            color = MaterialTheme.colors.onSurface
        )
        Text(
            text = if (tournament.isOnline) "Online" else "Offline",
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Preview
@Composable
fun TournamentCardViewPreview() = PocketBracketTheme {
    TournamentCardView(testTournament)
}