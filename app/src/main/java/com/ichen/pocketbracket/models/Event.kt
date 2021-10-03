package com.ichen.pocketbracket.models

import java.util.*

data class Event(
    val id: Int,
    val isOnline: Boolean,
    val name: String,
    val numEntrants: Int,
    val startAt: Date,
    val state: ActivityState,
    val videogame: Videogame
)

val testEvent = Event(
    0,
    true,
    "Test event",
    1,
    Date(0),
    ActivityState.ACTIVE,
    videogames[1]!!
)