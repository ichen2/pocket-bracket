package com.ichen.pocketbracket.timeline.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import coil.size.Scale
import com.ichen.pocketbracket.models.Event
import com.ichen.pocketbracket.models.Tournament
import com.ichen.pocketbracket.models.testTournament
import com.ichen.pocketbracket.ui.theme.PocketBracketTheme
import com.ichen.pocketbracket.utils.combineDates
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TournamentCardView(tournament: Tournament, first: Boolean = false) = Column(
    Modifier
        .fillMaxWidth(1f)
        .then(if(first) Modifier.clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)) else Modifier)
        .background(MaterialTheme.colors.surface)) {
    Box(Modifier.fillMaxWidth()) {
        Image(
            painter = rememberImagePainter(data = tournament.imageUrl, builder = {
                size(OriginalSize)
                scale(Scale.FILL)
            }),
            contentDescription = "tournament image",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth,
        )
        Text(
            tournament.state.toString(),
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colors.primary)
                .padding(4.dp)
        )
    }
    Column(Modifier.padding(16.dp)) {
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
        Spacer(Modifier.height(4.dp))
        for (event in tournament.events) {
            EventCardItemView(event = event)
        }
    }
}

@Composable
fun EventCardItemView(event: Event) {
    Row {
        Text(
            text = event.name,
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.h5
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = SimpleDateFormat("h:mm a", Locale.getDefault()).format(event.startAt),
            color = MaterialTheme.colors.secondary,
            style = MaterialTheme.typography.h5,
        )
    }
    Text(
        text = "${event.numEntrants} ${if (event.numEntrants == 1) "entrant" else "entrants"}",
        color = MaterialTheme.colors.onSurface,
    )
}

@Preview
@Composable
fun TournamentCardViewPreview() = PocketBracketTheme {
    TournamentCardView(testTournament)
}