package com.ichen.pocketbracket.timeline.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ichen.pocketbracket.models.Videogame
import com.ichen.pocketbracket.models.videogamesList
import com.ichen.pocketbracket.utils.SetComposableFunction
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun ChooseGamesDialog(
    setDialogComposable: SetComposableFunction,
    tournamentGames: List<Videogame>,
    onPositiveButtonClick: (List<Videogame>) -> Unit,
    onNegativeButtonClick: () -> Unit,
) = Column(
    Modifier
        .fillMaxSize(1f)
        .background(MaterialTheme.colors.background)
) {

    var checkedGames by remember { mutableStateOf(setOf<Int>()) }

    Row(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Icon(
            Icons.Filled.Close,
            "close",
            tint = MaterialTheme.colors.onPrimary,
            modifier = Modifier.clickable { onNegativeButtonClick() })
        Spacer(Modifier.weight(1f))
        Text(
            "Save",
            Modifier.clickable {
                onPositiveButtonClick(videogamesList.filter { tournamentGame ->
                    checkedGames.contains(tournamentGame.id)
                })
            },
            color = MaterialTheme.colors.onPrimary
        )
    }
    LazyColumn(modifier = Modifier.fillMaxHeight()) {
        items(items = videogamesList, key = { videogame -> videogame.id }) { videogame ->
            val isSelected = checkedGames.contains(videogame.id)
            val toggleSelect = {
                if (checkedGames.contains(videogame.id)) checkedGames =
                    checkedGames.filter { checkedGameId ->
                        checkedGameId != videogame.id
                    }.toSet()
                else checkedGames = (checkedGames + videogame.id).toSet()
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { toggleSelect() }
                    .background(if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.background)
                    .padding(16.dp)
            ) {
                Text(
                    text = videogame.displayName,
                    color = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onBackground,
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}