package com.ichen.pocketbracket.models

import androidx.compose.runtime.MutableState

data class UserDetails(
    val id: String,
    val url: String,
    val name: String?,
    val tag: String?,
    val location: String?,
    val imageUrls: List<String>,
)