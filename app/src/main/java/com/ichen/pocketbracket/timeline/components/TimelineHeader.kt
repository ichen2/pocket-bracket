package com.ichen.pocketbracket.timeline.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ichen.pocketbracket.models.Videogame
import com.ichen.pocketbracket.utils.SetComposableFunction

@Composable
fun TimelineHeader(selectedGames: MutableState<List<Videogame>?>, dialogDisplayed: Boolean, setDialogComposable: SetComposableFunction, ) = Column {

    var searchFieldText by remember { mutableStateOf("") }

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
    Spacer(Modifier.height(16.dp))
    Row(
        Modifier
            .fillMaxWidth(1f)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FilterPill("Games", selectedGames.value != null, !dialogDisplayed) { // sheet with checkbox list of games
            println("Clicked!!")
            setDialogComposable {
                ChooseGamesDialog(setDialogComposable, selectedGames)
            }
        }
        FilterPill("Location", false, true) { // sheet with location selction

        }
        FilterPill("Dates", false, true) { // sheet with date selection

        }
        FilterPill("Type", false, true) { // dropdown with online, offline, both

        }
        FilterPill("Price", false, true) { // dropdown with free, paid, both

        }
        FilterPill("Registration", false, true) { // dropdown with open, closed, both

        }
        FilterPill("Clear", false, true) {

        }
    }
}