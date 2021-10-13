package com.ichen.pocketbracket

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.ichen.pocketbracket.profile.MyProfileScreen
import com.ichen.pocketbracket.timeline.TournamentsTimelineScreen
import com.ichen.pocketbracket.tournaments.MyTournamentsScreen
import com.ichen.pocketbracket.ui.theme.PocketBracketTheme
import com.ichen.pocketbracket.ui.theme.medWhite
import com.ichen.pocketbracket.utils.Field
import com.ichen.pocketbracket.utils.Status

enum class CurrentTab {
    TournamentsTimeline,
    MyTournaments,
    MyProfile,
}

class MainActivity : AppCompatActivity() {

    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketBracketTheme {
                val currentTab = remember { mutableStateOf(CurrentTab.TournamentsTimeline) }
                val dialogComposable: MutableState<(@Composable BoxScope.() -> Unit)?> =
                    remember { mutableStateOf(null) }

                Box {
                    // SUS (This navigation kinda sucks, viewModels and jobs stay in memory even when their screen is not being used)
                    Column(Modifier.background(MaterialTheme.colors.background)) {
                        when (currentTab.value) {
                            CurrentTab.TournamentsTimeline -> {
                                TournamentsTimelineScreen(
                                    clickable = dialogComposable.value == null,
                                    setDialogComposable = { dialogComposable.value = it },
                                )
                            }
                            CurrentTab.MyTournaments -> {
                                MyTournamentsScreen()
                            }
                            CurrentTab.MyProfile -> {
                                MyProfileScreen()
                            }
                        }
                        NavigationFooter(currentTab, dialogComposable.value == null)
                    }
                    dialogComposable.value?.invoke(this)
                }
            }
        }
    }
}

@Composable
fun NavigationFooter(currentTab: MutableState<CurrentTab>, clickable: Boolean = true) =
    Column(Modifier.shadow(20.dp)) {
        Row(
            Modifier
                .background(MaterialTheme.colors.surface)
                .fillMaxWidth()
        ) {
            Box(
                Modifier
                    .weight(1f)
                    .clickable(enabled = clickable) {
                        currentTab.value = CurrentTab.TournamentsTimeline
                    }, contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "search",
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(32.dp)
                )
            }
            Box(
                Modifier
                    .weight(1f)
                    .clickable(enabled = clickable) {
                        currentTab.value = CurrentTab.MyTournaments
                    }, contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.EmojiEvents,
                    contentDescription = "search",
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(32.dp)
                )
            }
            Box(
                Modifier
                    .weight(1f)
                    .clickable(enabled = clickable) {
                        currentTab.value = CurrentTab.MyProfile
                    }, contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = "profile",
                    tint = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(32.dp)
                )
            }
        }

    }

@ExperimentalPermissionsApi
@Composable
@Preview
fun TestPreview() {
    val field = remember { mutableStateOf(Field("Hello", Status.NOT_STARTED)) }
    Column {
        Text(field.value.data)
        Text(field.value.status.toString())
        Button(onClick = {
            field.value = field.value.withData("World")
        }) {
            Text("Update value")
        }
        Button(onClick = {
            field.value = field.value.withStatus(Status.SUCCESS)
        }) {
            Text("Update status")
        }
    }
}