package com.ichen.pocketbracket.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ichen.pocketbracket.components.TournamentCardView
import com.ichen.pocketbracket.models.Videogame
import com.ichen.pocketbracket.models.testTournament
import com.ichen.pocketbracket.utils.SetComposableFunction

val tournaments by mutableStateOf(listOf(testTournament, testTournament, testTournament))

@Composable
fun ColumnScope.TournamentsTimelineScreen(setDialogComposable: SetComposableFunction) = Column(
    Modifier
        .weight(1f)
        .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 0.dp)
) {
    TournamentsTimelineHeader(setDialogComposable)
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
    TournamentsTimelineScreen {}
}

@Composable
fun TournamentsTimelineHeader(setDialogComposable: SetComposableFunction) = Column {

    var searchFieldText by remember { mutableStateOf("") }
    var selectedGames : List<Videogame>? by remember { mutableStateOf(null) }

    TextField(
        value = searchFieldText,
        onValueChange = { searchFieldText = it },
        modifier = Modifier
            .fillMaxWidth(1f)
            .clip(MaterialTheme.shapes.small),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )
    Spacer(Modifier.height(8.dp))
    Row(
        Modifier
            .fillMaxWidth(1f)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FilterPill("Games", selectedGames != null) { // sheet with checkbox list of games
            setDialogComposable {
                Column(
                    Modifier
                        .fillMaxWidth(1f)
                        .background(MaterialTheme.colors.surface)
                        .clip(
                            RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                        .align(Alignment.BottomCenter)
                ) {
                    Text("Games")
                    Button(onClick = {
                        selectedGames = listOf()
                        setDialogComposable(null)
                    }) {
                        Text("Click me")
                    }
                }
            }
        }
        FilterPill("Location") { // sheet with location selction

        }
        FilterPill("Dates") { // sheet with date selection

        }
        FilterPill("Type") { // dropdown with online, offline, both

        }
        FilterPill("Price") { // dropdown with free, paid, both

        }
        FilterPill("Registration") { // dropdown with open, closed, both

        }
        FilterPill("Clear") {

        }
    }
}

@Composable
fun FilterPill(text: String, enabled: Boolean = false, onClick: () -> Unit) {
    Text(
        text = text,
        modifier = Modifier
            .clip(CircleShape)
            .background(if(enabled) MaterialTheme.colors.primary else MaterialTheme.colors.surface)
            .then(if(!enabled) Modifier.border(1.dp, Color.White, shape = CircleShape) else Modifier)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        color = if(enabled) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onSurface
    )
}