package com.ichen.pocketbracket.timeline.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import coil.size.Scale
import com.ichen.pocketbracket.R
import com.ichen.pocketbracket.components.ShimmerAnimation
import com.ichen.pocketbracket.models.Event
import com.ichen.pocketbracket.models.Tournament
import com.ichen.pocketbracket.models.testTournament1
import com.ichen.pocketbracket.ui.theme.PocketBracketTheme
import com.ichen.pocketbracket.utils.combineDates
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

@Composable
fun TournamentCard(tournament: Tournament, clickable: Boolean, onClick: (String) -> Unit) = Column(
    Modifier
        .fillMaxWidth(1f)
        .background(MaterialTheme.colors.background)
) {

    val eventsListIsExpanded = remember { mutableStateOf(false) }

    Column(Modifier.clickable(enabled = clickable) { onClick(tournament.url) }) {
        Box(Modifier.fillMaxWidth()) {
            if(tournament.secondaryImageUrl != null) {
                // this is kinda jank but it seems to cache the banner image so that it loads faster in the details screen
                Image(
                    painter = rememberImagePainter(data = tournament.secondaryImageUrl),
                    contentDescription = "banner image",
                    modifier = Modifier.size(0.dp),
                )
            }
            if (tournament.primaryImageUrl != null) {
                Image(
                    painter = rememberImagePainter(data = tournament.primaryImageUrl, builder = {
                        size(OriginalSize)
                        scale(Scale.FILL)
                        placeholder(R.drawable.image_unavailable)
                    }),
                    contentDescription = "tournament image",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,
                )
            } else {
                Image(
                    painter = rememberImagePainter(data = R.drawable.image_unavailable, builder = {
                        size(OriginalSize)
                        scale(Scale.FILL)
                    }),
                    contentDescription = "image unavailable",
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillWidth
                )
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
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h4
            )
            if (tournament.startAt != null && tournament.endAt != null) {
                Text(
                    text = combineDates(
                        tournament.startAt,
                        tournament.endAt
                    ),
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.h5
                )
            }
            if (tournament.isOnline != null) {
                Text(
                    text = if (tournament.isOnline) "Online" else "Offline",
                    color = MaterialTheme.colors.onSurface
                )
            }
        }
    }
    if (tournament.events?.size ?: 0 > 0) {
        Column(Modifier.padding(vertical = 16.dp)) {
            for (i in 0..min(
                if (eventsListIsExpanded.value) Int.MAX_VALUE else 1,
                tournament.events?.size ?: 0
            )) {
                if (tournament.events?.getOrNull(i) != null) {
                    if (i != 0) Spacer(Modifier.height(16.dp))
                    EventCardItem(
                        event = tournament.events[i],
                        clickable = clickable,
                        onClick = onClick
                    )
                }
            }
            if (tournament.events?.size ?: 0 > 2) {
                Spacer(Modifier.height(16.dp))
                Text(
                    modifier = Modifier.fillMaxWidth().clickable(enabled = clickable, onClick = {
                        eventsListIsExpanded.value = !eventsListIsExpanded.value
                    }).padding(horizontal = 16.dp, vertical = 4.dp) ,
                    text = "Show ${if (eventsListIsExpanded.value) "less" else "${tournament.events!!.size - 2} more"}",
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}

@Composable
fun TournamentCardLoading(brush: Brush) = Column(
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
fun EventCardItem(event: Event, clickable: Boolean, onClick: (String) -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable(enabled = clickable) { onClick(event.url) }
            .padding(horizontal = 16.dp)) {
        Text(
            text = event.name,
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.h5
        )
        Row {
            if (event.numEntrants != null) {
                Text(
                    text = "${event.numEntrants} ${if (event.numEntrants == 1) "entrant" else "entrants"}",
                    color = MaterialTheme.colors.onSurface,
                )
                Spacer(Modifier.weight(1f))
            }
            Log.d("IVC", "${event.startAt == null}")
            if (event.startAt != null) {
                Text(
                    text = SimpleDateFormat(
                        "h:mm a",
                        Locale.getDefault()
                    ).format(event.startAt),
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.h5,
                )
            }
        }
    }
}


@Preview
@Composable
fun TournamentCardPreview() = PocketBracketTheme {
    TournamentCard(testTournament1, true, {})
}

@Preview
@Composable
fun TournamentCardLoadingPreview() = PocketBracketTheme {
    ShimmerAnimation { brush ->
        TournamentCardLoading(brush)
    }
}