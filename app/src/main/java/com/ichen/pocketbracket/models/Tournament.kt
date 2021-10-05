package com.ichen.pocketbracket.models

import java.util.*

data class Tournament(
    val id: Int,
    val countryCode: String,
    val startAt: Date,
    val endAt: Date,
    val isOnline: Boolean,
    val isRegistrationOpen: Boolean,
    val name: String,
    val numAttendees: Int,
    val owner: String,
    val state: ActivityState,
    val imageUrl: String?,
    val events: List<Event>,
)

val testTournament: Tournament = Tournament(
    0,
    "JP",
    Date(0),
    Date(1),
    true,
    true,
    "Test tournament",
    1,
    "Test owner",
    ActivityState.ACTIVE,
    "https://smashgg-images.s3.amazonaws.com/images/tournament/1035/image-10e39229043ff962dd367a516b0bc090.png",
    listOf(testEvent, testEvent),
)