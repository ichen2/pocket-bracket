package com.ichen.pocketbracket.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ColumnScope.TournamentsTimelineScreen() = Column(Modifier.weight(1f)) {
    Text("Tournaments Timeline", color = MaterialTheme.colors.primary)
}