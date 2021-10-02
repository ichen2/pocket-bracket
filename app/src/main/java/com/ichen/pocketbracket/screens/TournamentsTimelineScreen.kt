package com.ichen.pocketbracket.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ichen.pocketbracket.components.TournamentCardView
import com.ichen.pocketbracket.models.testTournament

val tournaments by mutableStateOf(listOf(testTournament, testTournament, testTournament))
var searchFieldText by mutableStateOf("")

@Composable
fun ColumnScope.TournamentsTimelineScreen() = Column(Modifier.weight(1f)) {
    TournamentsTimelineHeader()
    LazyColumn(Modifier.padding(16.dp)) {
        items(tournaments) { tournament ->
            TournamentCardView(tournament = tournament)
        }
    }
}

@Composable
@Preview
fun TournamentsTimelineScreenPreview() = Column(Modifier.background(Color.White)) {
    TournamentsTimelineScreen()
}

@Composable
fun TournamentsTimelineHeader() = Box(Modifier.padding(16.dp)) {
    Column(
        Modifier
            .clip(MaterialTheme.shapes.small)
            .border(1.dp, MaterialTheme.colors.secondary, shape = MaterialTheme.shapes.small)
            .padding(16.dp)
            .background(MaterialTheme.colors.background)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text("Select Dates", color = MaterialTheme.colors.onBackground)
            }
            Box(
                Modifier
                    .height(12.dp)
                    .width(1.dp)
                    .background(MaterialTheme.colors.secondary)
            )
            Row(
                Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text("Select Location", color = MaterialTheme.colors.onBackground)
            }
        }
        Box(
            Modifier
                .padding(vertical = 8.dp)
                .height(1.dp)
                .fillMaxSize(1f)
                .background(MaterialTheme.colors.secondary)
        )
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
    }
    Text("Clear",
        Modifier
            .align(Alignment.TopEnd)
            .offset(x = 8.dp, y = -8.dp)
            .clip(MaterialTheme.shapes.small)
            .background(Color.Red)
            .padding(horizontal = 16.dp, vertical = 4.dp)
    )
}