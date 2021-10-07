package com.ichen.pocketbracket.timeline.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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

@Composable
fun ChooseGamesDialog(
    setDialogComposable: SetComposableFunction,
    tournamentGames: MutableState<(List<Videogame>?)>
) = Box(
    Modifier
        .fillMaxSize(1f)
        .background(Color.Black.copy(.5f))
) {

    val checkedGames = videogamesList.map { videogame ->
        remember {
            mutableStateOf(
                tournamentGames.value != null && tournamentGames.value!!.contains(
                    videogame
                )
            )
        }
    }

    Column(
        Modifier
            .clip(
                RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .background(MaterialTheme.colors.surface)
            .padding(16.dp)
            .align(Alignment.BottomCenter),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row {
            Text(
                text = "Games",
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.h4
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "Close",
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.clickable {
                    tournamentGames.value = videogamesList.filterIndexed { index, _ ->
                        checkedGames[index].value
                    }.ifEmpty { null }
                    setDialogComposable(null)
                })
        }
        videogamesList.forEachIndexed { index, videogame ->
            val toggleCheckbox = { checkedGames[index].value = !checkedGames[index].value }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checkedGames[index].value,
                    onCheckedChange = { toggleCheckbox() })
                Spacer(Modifier.width(16.dp))
                Text(
                    text = videogame.displayName,
                    modifier = Modifier.clickable { toggleCheckbox() },
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.h4
                )
            }
        }
    }
}