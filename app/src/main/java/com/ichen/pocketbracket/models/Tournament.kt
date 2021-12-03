package com.ichen.pocketbracket.models

import com.ichen.pocketbracket.utils.SECONDS_IN_DAY
import java.util.*

data class Tournament(
    val id: Int,
    val name: String,
    val url: String,
    val startAt: Date?,
    val endAt: Date?,
    val isOnline: Boolean?,
    val isRegistrationOpen: Boolean?,
    val numAttendees: Int?,
    val state: ActivityState?,
    val imageUrl: String?,
    val events: List<Event>?,
)

fun Tournament.getRating(filter: TournamentFilter): Float {
    var rating: Float = 0f
    if (numAttendees != null) rating += numAttendees / 500
    if (startAt != null && filter.dates?.start?.time != null) {
        val dateRangeInDays = (filter.dates.end.time - filter.dates.start.time) / SECONDS_IN_DAY
        rating += (dateRangeInDays - ((startAt.time - filter.dates.start.time) / (SECONDS_IN_DAY * 1000))) / dateRangeInDays
    }
    return rating
}

val testTournament: Tournament = Tournament(
    id = 0,
    startAt = Date(0),
    url = "",
    endAt = Date(1),
    isOnline = true,
    isRegistrationOpen = true,
    name = "Test tournament",
    numAttendees = 1,
    state = ActivityState.ACTIVE,
    imageUrl = "https://smashgg-images.s3.amazonaws.com/images/tournament/1035/image-10e39229043ff962dd367a516b0bc090.png",
    events = listOf(testEvent, testEvent),
)