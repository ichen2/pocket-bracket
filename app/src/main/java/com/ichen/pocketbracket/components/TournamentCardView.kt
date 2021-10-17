package com.ichen.pocketbracket.timeline.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import coil.size.Scale
import com.ichen.pocketbracket.R
import com.ichen.pocketbracket.components.ShimmerAnimation
import com.ichen.pocketbracket.models.Event
import com.ichen.pocketbracket.models.Tournament
import com.ichen.pocketbracket.models.testTournament
import com.ichen.pocketbracket.ui.theme.*
import com.ichen.pocketbracket.utils.combineDates
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

@Composable
fun TournamentCardView(tournament: Tournament, onClick: (String) -> Unit) = Column(
    Modifier
        .fillMaxWidth(1f)
        .background(MaterialTheme.colors.background)
) {

    val eventsListIsExpanded = remember { mutableStateOf(false) }

    Box(Modifier.fillMaxWidth().clickable { onClick(tournament.url)}) {
        if (tournament.imageUrl != null) {
            Image(
                painter = rememberImagePainter(data = tournament.imageUrl, builder = {
                    size(OriginalSize)
                    scale(Scale.FILL)
                    placeholder(R.drawable.image_unavailable)
                }),
                contentDescription = "tournament image",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
            )
        } else {
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colors.secondaryVariant),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = "image unavailable",
                    modifier = Modifier.size(100.dp),
                    tint = Color.Black
                )
                Spacer(Modifier.height(16.dp))
                Text("Image unavailable", color = Color.Black)
            }
        }
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
        if (tournament.startAt != null && tournament.endAt != null) {
            Text(
                text = combineDates(
                    tournament.startAt,
                    tournament.endAt
                ),
                color = MaterialTheme.colors.onSurface
            )
        }
        if (tournament.isOnline != null) {
            Text(
                text = if (tournament.isOnline) "Online" else "Offline",
                color = MaterialTheme.colors.onSurface
            )
        }
        Spacer(Modifier.height(16.dp))
        for (i in 0..min(
            if (eventsListIsExpanded.value) Int.MAX_VALUE else 1,
            tournament.events?.size ?: 0
        )) {
            if (i != 0) Spacer(Modifier.height(16.dp))
            if (tournament.events?.getOrNull(i) != null) EventCardItemView(event = tournament.events[0])
        }
        if (tournament.events?.size ?: 0 > 2) {
            Spacer(Modifier.height(16.dp))
            Text(
                modifier = Modifier.clickable {
                    eventsListIsExpanded.value = !eventsListIsExpanded.value
                },
                text = "Show ${if (eventsListIsExpanded.value) "less" else "${tournament.events!!.size - 2} more"}",
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
fun TournamentCardViewLoading(brush: Brush) = Column(
    modifier = Modifier
        .fillMaxWidth(1f)
        .background(brush = brush)
) {
    Spacer(Modifier.height(200.dp))
    Box(
        Modifier
            .height(16.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
    )
    LoadingCardItem(height = 48.dp)
    Box(
        Modifier
            .height(16.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
    )
    for (i in 0..2) {
        LoadingCardItem(height = 16.dp)
        Box(
            Modifier
                .height(16.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
        )
    }
}

@Composable
fun LoadingCardItem(height: Dp, padding: Dp = 16.dp) {
    Row(Modifier.height(height)) {
        Box(
            Modifier
                .fillMaxHeight()
                .width(padding)
                .background(MaterialTheme.colors.background)
        )
        Spacer(Modifier.weight(1f))
        Box(
            Modifier
                .fillMaxHeight()
                .width(padding)
                .background(MaterialTheme.colors.background)
        )
    }
}

@Composable
fun EventCardItemView(event: Event) {
    Column(Modifier.fillMaxWidth()) {
        Text(
            text = event.name,
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.h5
        )
        Row {
            if (event.startAt != null) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = SimpleDateFormat(
                        "h:mm a",
                        Locale.getDefault()
                    ).format(event.startAt),
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.h5,
                )
            }
            if (event.numEntrants != null) {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    text = "${event.numEntrants} ${if (event.numEntrants == 1) "entrant" else "entrants"}",
                    color = MaterialTheme.colors.onSurface,
                )
            }
        }
    }
}


@Preview
@Composable
fun TournamentCardViewPreview() = PocketBracketTheme {
    TournamentCardView(testTournament, {})
}

@Preview
@Composable
fun TournamentCardViewLoadingPreview() = PocketBracketTheme {
    ShimmerAnimation { brush ->
        TournamentCardViewLoading(brush)
    }
}