package com.ichen.pocketbracket.details

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ichen.pocketbracket.details.components.Header
import com.ichen.pocketbracket.details.components.Sidebar
import com.ichen.pocketbracket.models.Tournament
import com.ichen.pocketbracket.ui.theme.PocketBracketTheme

enum class CurrentTab {
    Home,
    Attendees,
    Events,
}

class TournamentDetailsActivity : AppCompatActivity() {
    private var showSidebar by mutableStateOf(false)
    private var currentTab = mutableStateOf(CurrentTab.Home)
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tournament = intent.getParcelableExtra<Tournament>("tournament")
        setContent {
            PocketBracketTheme {
                if (tournament != null) {
                    Box(
                        Modifier
                            .background(MaterialTheme.colors.background)
                            .fillMaxSize()
                    ) {
                        LazyColumn {
                            stickyHeader { Header(tournament, !showSidebar) { showSidebar = !showSidebar } }
                            item {
                                when (currentTab.value) {
                                    CurrentTab.Home -> {
                                        HomeScreen(tournament = tournament, currentTab = currentTab)
                                    }
                                    CurrentTab.Attendees -> {
                                        AttendeesScreen(tournament = tournament)
                                    }
                                    CurrentTab.Events -> {
                                        EventsScreen(tournament = tournament)
                                    }
                                }
                            }
                        }
                        if (showSidebar) {
                            Sidebar(tournament, currentTab, { onBackPressed() }) { showSidebar = !showSidebar }
                        }
                    }
                } else {
                    ErrorScreen()
                }
            }
        }
    }
}