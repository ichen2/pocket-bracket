package com.ichen.pocketbracket.models

import java.util.*

data class Event(
    val id: String,
    val name: String,
    val numEntrants: Int?,
    val startAt: Date?,
    val videogame: Videogame?
)

val testEvent = Event(
    id = "0",
    name = "Test event",
    numEntrants = 1,
    startAt = Date(0),
    videogame = videogamesList[0]
)