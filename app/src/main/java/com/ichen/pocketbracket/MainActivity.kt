package com.ichen.pocketbracket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.ichen.pocketbracket.screens.MyProfileScreen
import com.ichen.pocketbracket.screens.MyTournamentsScreen
import com.ichen.pocketbracket.screens.TournamentsTimelineScreen
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
                val currentTab = remember { mutableStateOf(CurrentTab.MyProfile) }
                Column(Modifier.background(MaterialTheme.colors.background)) {
                    when (currentTab.value) {
                        CurrentTab.TournamentsTimeline -> {
                            TournamentsTimelineScreen()
                        }
                        CurrentTab.MyTournaments -> {
                            MyTournamentsScreen()
                        }
                        CurrentTab.MyProfile -> {
                            MyProfileScreen()
                        }
                    }
                    NavigationFooter(currentTab)
                }
            }
        }
    }
}

@Composable
fun NavigationFooter(currentTab: MutableState<CurrentTab>) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(MaterialTheme.colors.background),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            painter = ColorPainter(Color.White),
            modifier = Modifier.size(90.dp).clickable {
                currentTab.value = CurrentTab.TournamentsTimeline
            },
            contentDescription = "Timeline"
        )
        Image(
            painter = ColorPainter(Color.White),
            modifier = Modifier.size(90.dp).clickable {
                currentTab.value = CurrentTab.MyTournaments
            },
            contentDescription = "Timeline"
        )
        Image(
            painter = ColorPainter(Color.White),
            modifier = Modifier.size(90.dp).clickable {
                currentTab.value = CurrentTab.MyProfile
            },
            contentDescription = "Timeline"
        )
    }
}