package com.ichen.pocketbracket.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ColumnScope.MyProfileScreen() = Column(Modifier.weight(1f)) {
    Text("My Profile", color = MaterialTheme.colors.primary)
}