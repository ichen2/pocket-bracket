package com.ichen.pocketbracket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
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
import com.ichen.pocketbracket.auth.AuthActivity
import com.ichen.pocketbracket.profile.MyProfileScreen
import com.ichen.pocketbracket.timeline.TournamentsTimelineScreen
import com.ichen.pocketbracket.tournaments.MyTournamentsScreen
import com.ichen.pocketbracket.ui.theme.PocketBracketTheme
import com.ichen.pocketbracket.ui.theme.medWhite
import com.ichen.pocketbracket.utils.*

enum class CurrentTab {
    TournamentsTimeline,
    MyTournaments,
    MyProfile,
}

var apiKey: String? = null

class MainActivity : AppCompatActivity() {
    val currentTab = mutableStateOf(CurrentTab.TournamentsTimeline)
    val dialogComposable: MutableState<(@Composable BoxScope.() -> Unit)?> =
        mutableStateOf(null)

    @ExperimentalPermissionsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prevTab = savedInstanceState?.get(CURRENT_TAB_STORAGE_KEY)
        if (prevTab != null && prevTab is CurrentTab) currentTab.value = prevTab
        val prevKey = savedInstanceState?.getString(API_KEY_STORAGE_KEY)
        val savedKey = this.getSharedPreferences(
            SHARED_PREFERENCES_KEY,
            Context.MODE_PRIVATE
        ).getString(API_KEY_STORAGE_KEY, null)
        val sentKey = intent.extras?.get(API_KEY_STORAGE_KEY)
        val userIsAuthenticated =
            prevKey != null || savedKey != null || (sentKey != null && sentKey is String)
        if (sentKey != null && sentKey is String) {
            apiKey = sentKey
        } else if (prevKey != null) {
            apiKey = prevKey
        } else if (savedKey != null) {
            apiKey = savedKey
        } else {
            startActivity(Intent(this, AuthActivity::class.java))
        }
        saveApiKeyToStorage()
        setContent {
            PocketBracketTheme {
                if (userIsAuthenticated) {
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
                                    MyTournamentsScreen(setDialogComposable = {
                                        dialogComposable.value = it
                                    })
                                }
                                CurrentTab.MyProfile -> {
                                    MyProfileScreen(setDialogComposable = {
                                        dialogComposable.value = it
                                    })
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

    override fun onStop() {
        super.onStop()
        saveApiKeyToStorage()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (apiKey != null) outState.putString(API_KEY_STORAGE_KEY, apiKey)
        outState.putSerializable(CURRENT_TAB_STORAGE_KEY, currentTab.value)
    }

    override fun onBackPressed() {
        if (dialogComposable.value != null) {
            dialogComposable.value = null
        } else {
            super.onBackPressed()
        }
    }


    private fun saveApiKeyToStorage() {
        println("Saving api key to storage!!!")
        if (apiKey != null) {
            getSharedPreferences(
                SHARED_PREFERENCES_KEY,
                Context.MODE_PRIVATE
            ).edit().putString(API_KEY_STORAGE_KEY, apiKey).apply()
        } else {
            getSharedPreferences(
                SHARED_PREFERENCES_KEY,
                Context.MODE_PRIVATE
            ).edit().remove(API_KEY_STORAGE_KEY).apply()
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
}