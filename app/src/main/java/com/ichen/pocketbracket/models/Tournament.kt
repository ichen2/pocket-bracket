package com.ichen.pocketbracket.models

import java.util.*

data class Tournament(
    val id: Int,
    val startAt: Date,
    val endAt: Date,
    val isOnline: Boolean,
    val isRegistrationOpen: Boolean,
    val name: String,
    val numAttendees: Int,
    val owner: String,
    val state: ActivityState,
)