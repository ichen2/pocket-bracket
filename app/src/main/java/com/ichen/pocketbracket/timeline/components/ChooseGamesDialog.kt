package com.ichen.pocketbracket.timeline.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ichen.pocketbracket.components.ErrorSplash
import com.ichen.pocketbracket.models.Videogame
import com.ichen.pocketbracket.models.VideogameFilter
import com.ichen.pocketbracket.utils.Status

@Composable
fun ChooseGamesDialog(
    onPositiveButtonClick: (List<Videogame>) -> Unit,
    onNegativeButtonClick: () -> Unit,
    viewModel: ChooseGamesViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) = Column(
    Modifier
        .fillMaxSize(1f)
        .background(MaterialTheme.colors.background)
) {
    val context = LocalContext.current

    fun getVideogames(name: String) = run {
        viewModel.getVideogames(
            VideogameFilter(
                perPage = 250,
                page = 1,
                name = name
            ),
            context
        )
    }

    var checkedGames by remember { mutableStateOf(setOf<Int>()) }
    var videogameName by remember { mutableStateOf("") }

    DisposableEffect(key1 = viewModel) {
        if (viewModel.videogames.value.status != Status.SUCCESS) {
            getVideogames(videogameName)
        }
        onDispose {
            viewModel.cleanup()
            videogameName = ""
            checkedGames = setOf()
        }
    }

    Column(
        Modifier
            .background(MaterialTheme.colors.primary)
            .padding(16.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
        ) {
            Icon(
                Icons.Filled.Close,
                "close",
                tint = MaterialTheme.colors.onPrimary,
                modifier = Modifier.clickable {
                    viewModel.cleanup()
                    onNegativeButtonClick()
                })
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = "Choose your game(s)",
                color = MaterialTheme.colors.onPrimary
            )
            Text(
                "Save",
                Modifier.clickable {
                    onPositiveButtonClick(viewModel.videogames.value.data.filter { tournamentGame ->
                        checkedGames.contains(tournamentGame.id)
                    })
                    viewModel.cleanup()
                },
                color = MaterialTheme.colors.onPrimary
            )
        }
        Spacer(Modifier.height(8.dp))
        val textColor =
            if (isSystemInDarkTheme()) MaterialTheme.colors.onPrimary else MaterialTheme.colors.onPrimary
        TextField(
            value = videogameName,
            onValueChange = { value ->
                videogameName = value
                getVideogames(value)
            },
            placeholder = {
                Text(
                    "Name",
                    color = textColor.copy(alpha = .5f)
                )
            },
            modifier = Modifier
                .fillMaxWidth(1f)
                .clip(MaterialTheme.shapes.small),
            colors = TextFieldDefaults.textFieldColors(
                textColor = textColor,
                cursorColor = textColor,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
    }
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { checkedGames = setOf() }
            .background(MaterialTheme.colors.error)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = "Unselect all", color = MaterialTheme.colors.onError)
    }
    if (viewModel.videogames.value.data.isEmpty()) {
        when (viewModel.videogames.value.status) {
            Status.ERROR -> {
                ErrorSplash("Error loading video games")
            }
            Status.SUCCESS -> {
                ErrorSplash("No video games found", isCritical = false)
            }
            else -> {
                VideoGamesListLoading(numItems = 10)
            }
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            itemsIndexed(
                items = viewModel.videogames.value.data,
                key = { _, videogame -> videogame.id }) { index, videogame ->
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
}

@Composable
fun VideogameLoading(brush: Brush) = Row(modifier = Modifier
    .fillMaxWidth()
    .background(brush = brush).padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
    Box(Modifier.fillMaxWidth().height(16.dp).background(MaterialTheme.colors.background))
}
