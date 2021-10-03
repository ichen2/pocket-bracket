package com.ichen.pocketbracket.tournaments

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ColumnScope.MyTournamentsScreen() = Column(Modifier.weight(1f)) {
    Text("My Tournaments", color = MaterialTheme.colors.primary)
}