package com.ichen.pocketbracket.models

data class Attendee(
    val id: String,
    val tag: String,
    val prefix: String? = null,
    val imageUrl: String? = null,
    val events: List<Event>? = null,
)