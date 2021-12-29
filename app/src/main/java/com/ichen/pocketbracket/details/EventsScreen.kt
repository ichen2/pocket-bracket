package com.ichen.pocketbracket.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.ichen.pocketbracket.models.Tournament

@Composable
fun LazyItemScope.EventsScreen(tournament: Tournament) = Column {
    Text("Events")
}