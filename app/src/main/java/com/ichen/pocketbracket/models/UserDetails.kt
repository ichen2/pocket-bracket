package com.ichen.pocketbracket.models

import androidx.compose.runtime.MutableState

data class UserDetails(
    val id: String,
    val name: String?,
    val location: String?,
    val imageUrls: List<String>,
)