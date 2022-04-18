package com.ichen.pocketbracket.details

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ichen.pocketbracket.components.ErrorSplash
import com.ichen.pocketbracket.details.components.Header
import com.ichen.pocketbracket.details.components.Sidebar
import com.ichen.pocketbracket.models.Tournament
import com.ichen.pocketbracket.ui.theme.PocketBracketTheme
import com.ichen.pocketbracket.utils.API_KEY_STORAGE_KEY
import com.ichen.pocketbracket.utils.SHARED_PREFERENCES_KEY

enum class CurrentTab {
    Home,
    Attendees,
    Events,
}

var apiKey: String? = null
var tournament: Tournament? = null

class TournamentDetailsActivity : AppCompatActivity() {
    private var showSidebar by mutableStateOf(false)
    private var currentTab = mutableStateOf(CurrentTab.Home)
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tournament = intent.getParcelableExtra("tournament")
        apiKey = this.getSharedPreferences(
            SHARED_PREFERENCES_KEY,
            Context.MODE_PRIVATE
        ).getString(API_KEY_STORAGE_KEY, null)
        setContent {
            PocketBracketTheme {
                if (tournament != null && apiKey != null) {
                    Box(
                        Modifier
                            .background(MaterialTheme.colors.background)
                            .fillMaxSize()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Header(tournament!!, !showSidebar) { showSidebar = !showSidebar }
                                when (currentTab.value) {
                                    CurrentTab.Home -> {
                                        HomeScreen()
                                    }
                                    CurrentTab.Attendees -> {
                                        AttendeesScreen(tournament?.slug ?: "") // pass empty string if no slug so browser will redirect to 404
                                    }
                                    CurrentTab.Events -> {
                                        EventsScreen(tournament?.events, tournament?.slug ?: "")
                                    }
                                }
                        }
                        if (showSidebar) {
                            Sidebar(tournament!!, currentTab, { onBackPressed() }) { showSidebar = !showSidebar }
                        }
                    }
                } else {
                    ErrorSplash("Error fetching tournament details from smash.gg")
                }
            }
        }
    }
}