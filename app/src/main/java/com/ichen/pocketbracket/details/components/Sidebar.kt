package com.ichen.pocketbracket.details.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import coil.size.Scale
import com.ichen.pocketbracket.R
import com.ichen.pocketbracket.details.CurrentTab
import com.ichen.pocketbracket.models.DateRange
import com.ichen.pocketbracket.models.Tournament
import com.ichen.pocketbracket.utils.*

@Composable
fun Sidebar(
    tournament: Tournament,
    currentTab: MutableState<CurrentTab>,
    goBack: () -> Unit,
    toggle: () -> Unit
) = Row(Modifier.fillMaxSize()) {
    val context = LocalContext.current
    Column(
        Modifier
            .width(64.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colors.primary)
            .padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Filled.ChevronLeft,
            contentDescription = "go back",
            modifier = Modifier
                .size(48.dp)
                .clickable { goBack() },
            tint = MaterialTheme.colors.onPrimary
        )
    }
    Column(
        Modifier
            .fillMaxWidth(.8f)
            .fillMaxHeight()
            .background(MaterialTheme.colors.surface)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(64.dp), contentAlignment = Alignment.CenterStart
        ) {
            Image(
                painter = rememberImagePainter(data = tournament.secondaryImageUrl, builder = {
                    size(OriginalSize)
                    scale(Scale.FILL)
                    placeholder(R.drawable.image_unavailable)
                    crossfade(true)
                }),
                contentDescription = "sidebar banner image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = .8f))
            )
            Text(
                style = MaterialTheme.typography.h5,
                color = Color.White,
                text = tournament.name,
                modifier = Modifier.padding(16.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column(Modifier.padding(8.dp)) {
            Button(modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(4.dp),
                onClick = { openBrowser(context, "${SITE_ENDPOINT}/${tournament.slug}/register") },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
            ) {
                Text("Register")
            }
            Spacer(Modifier.height(16.dp))
            if (tournament.startAt != null && tournament.endAt != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Event,
                        "date icon",
                        Modifier.size(16.dp),
                        tint = MaterialTheme.colors.primary
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = DateRange(
                            tournament.startAt,
                            tournament.endAt
                        ).toString(), style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
            val mergedAddress =
                mergeAddress(tournament.city, tournament.addrState, tournament.countryCode)
            if (mergedAddress != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.LocationOn,
                        "location icon",
                        Modifier.size(16.dp),
                        tint = MaterialTheme.colors.primary
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = mergedAddress,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onSurface
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            SidebarItem(Icons.Filled.Home, "Home", currentTab.value == CurrentTab.Home) {
                toggle()
                currentTab.value = CurrentTab.Home
            }
            SidebarItem(
                Icons.Filled.Person,
                "Attendees",
                currentTab.value == CurrentTab.Attendees
            ) {
                toggle()
                currentTab.value = CurrentTab.Attendees
            }
            SidebarItem(
                Icons.Filled.SportsEsports,
                "Events",
                currentTab.value == CurrentTab.Events
            ) {
                toggle()
                currentTab.value = CurrentTab.Events
            }
            Spacer(Modifier.height(16.dp))
            if (tournament.events?.isNotEmpty() == true) {
                tournament.events.forEach { event ->
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .clickable { openBrowser(context, event.url) }
                            .padding(8.dp)) {
                        Text(event.name, color = MaterialTheme.colors.onSurface)
                        if (event.startAt != null) {
                            Text(
                                event.startAt.toPrettyString(),
                                color = MaterialTheme.colors.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = Color.Black.copy(.5f))
            .clickable { toggle() }) {}
}

@Composable
fun ColumnScope.SidebarItem(
    icon: ImageVector,
    name: String,
    selected: Boolean,
    onClick: () -> Unit
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.surface)
        .clickable { onClick() },
    verticalAlignment = Alignment.CenterVertically
) {
    Icon(
        imageVector = icon,
        contentDescription = name,
        modifier = Modifier.size(32.dp),
        tint = MaterialTheme.colors.primary
    )
    Spacer(Modifier.width(8.dp))
    Text(
        name,
        style = if (selected) MaterialTheme.typography.h5 else MaterialTheme.typography.body1,
        color = MaterialTheme.colors.onSurface
    )
}