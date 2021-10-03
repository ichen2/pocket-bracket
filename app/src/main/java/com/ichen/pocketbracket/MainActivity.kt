package com.ichen.pocketbracket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ichen.pocketbracket.profile.MyProfileScreen
import com.ichen.pocketbracket.tournaments.MyTournamentsScreen
import com.ichen.pocketbracket.timeline.TournamentsTimelineScreen
import com.ichen.pocketbracket.ui.theme.PocketBracketTheme

enum class CurrentTab {
    TournamentsTimeline,
    MyTournaments,
    MyProfile,
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketBracketTheme {
                val currentTab = remember { mutableStateOf(CurrentTab.TournamentsTimeline) }
                val dialogComposable : MutableState<(@Composable BoxScope.()->Unit)?> = remember { mutableStateOf(null)}
                Box {
                    Column(Modifier.background(MaterialTheme.colors.background)) {
                        when (currentTab.value) {
                            CurrentTab.TournamentsTimeline -> {
                                TournamentsTimelineScreen(clickable = dialogComposable.value == null) {
                                    dialogComposable.value = it
                                }
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
fun NavigationFooter(currentTab: MutableState<CurrentTab>, clickable: Boolean = true) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(MaterialTheme.colors.background),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = ColorPainter(MaterialTheme.colors.onBackground),
            modifier = Modifier.size(90.dp).clickable(enabled = clickable) {
                currentTab.value = CurrentTab.TournamentsTimeline
            },
            contentDescription = "Timeline"
        )
        Image(
            painter = ColorPainter(MaterialTheme.colors.onBackground),
            modifier = Modifier.size(90.dp).clickable(enabled = clickable) {
                currentTab.value = CurrentTab.MyTournaments
            },
            contentDescription = "Timeline"
        )
        Image(
            painter = ColorPainter(MaterialTheme.colors.onBackground),
            modifier = Modifier.size(90.dp).clickable(enabled = clickable) {
                currentTab.value = CurrentTab.MyProfile
            },
            contentDescription = "Timeline"
        )
    }
}