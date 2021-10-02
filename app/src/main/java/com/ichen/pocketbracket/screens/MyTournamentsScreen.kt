package com.ichen.pocketbracket.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ichen.pocketbracket.components.TournamentCardView
import com.ichen.pocketbracket.models.testTournament

@Composable
fun ColumnScope.MyTournamentsScreen() = Column(Modifier.weight(1f)) {
    Text("My Tournaments", color = MaterialTheme.colors.primary)
}