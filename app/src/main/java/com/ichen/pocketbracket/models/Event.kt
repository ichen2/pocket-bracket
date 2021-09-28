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