package com.ichen.pocketbracket.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.ichen.pocketbracket.details.components.Banner
import com.ichen.pocketbracket.details.components.Header
import com.ichen.pocketbracket.models.Tournament

@Composable
fun LazyItemScope.HomeScreen() {
    Column {
        Banner(tournament!!)
    }
}