package com.ichen.pocketbracket.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import com.ichen.pocketbracket.details.components.Banner

@Composable
fun ColumnScope.HomeScreen() {
    Column {
        Banner(tournament!!)
    }
}