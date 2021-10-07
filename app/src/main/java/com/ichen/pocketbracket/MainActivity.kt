package com.ichen.pocketbracket

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.material.datepicker.MaterialDatePicker
import com.ichen.pocketbracket.profile.MyProfileScreen
import com.ichen.pocketbracket.timeline.TournamentsTimelineScreen
import com.ichen.pocketbracket.timeline.components.LocationPicker
import com.ichen.pocketbracket.tournaments.MyTournamentsScreen
import com.ichen.pocketbracket.ui.theme.PocketBracketTheme
import java.util.*

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
            modifier = Modifier
                .size(90.dp)
                .clickable(enabled = clickable) {
                    currentTab.value = CurrentTab.TournamentsTimeline
                },
            contentDescription = "Timeline",
        )
        Image(
            painter = ColorPainter(MaterialTheme.colors.onBackground),
            modifier = Modifier
                .size(90.dp)
                .clickable(enabled = clickable) {
                    currentTab.value = CurrentTab.MyTournaments
                },
            contentDescription = "Timeline",
        )
        Image(
            painter = ColorPainter(MaterialTheme.colors.onBackground),
            modifier = Modifier
                .size(90.dp)
                .clickable(enabled = clickable) {
                    currentTab.value = CurrentTab.MyProfile
                },
            contentDescription = "Timeline",
        )
    }
}

@ExperimentalPermissionsApi
@Composable
@Preview
fun TestPreview() {
    Column {
        val locationPermissionState = rememberMultiplePermissionsState(permissions = listOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION))
        if(!locationPermissionState.allPermissionsGranted) {
            Button(onClick = { locationPermissionState.launchMultiplePermissionRequest() }) {
                Text("Request Permissions")
            }
        } else {
            Text("Permissions Granted!")
        }
    }
}