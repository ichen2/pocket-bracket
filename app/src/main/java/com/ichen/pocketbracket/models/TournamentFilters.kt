package com.ichen.pocketbracket.models

import com.google.android.gms.maps.model.LatLng
import com.ichen.pocketbracket.utils.isSameDay
import com.ichen.pocketbracket.utils.toPrettyString
import java.util.*

data class TournamentFilter(
    val name: String = "",
    val games: List<Videogame>? = null,
    val location: LocationRadius? = null,
    val dates: DateRange? = null,
    val type: TournamentType? = null,
    val price: TournamentPrice? = null,
    val registration: TournamentRegistrationStatus? = null,
)

enum class TournamentType(private val displayName: String) {
    NO_FILTER("Type"),
    IS_ONLINE("Online"),
    IS_OFFLINE("Offline"),;

    override fun toString(): String {
        return displayName
    }
}

enum class TournamentPrice(private val displayName: String) {
    NO_FILTER("Price"),
    PAID("Paid"),
    FREE("Free"),;

    override fun toString(): String {
        return displayName
    }
}

enum class TournamentRegistrationStatus(private val displayName: String) {
    NO_FILTER("Registration"),
    OPEN("Open Registration"),
    CLOSED("Closed Registration"),;

    override fun toString(): String {
        return displayName
    }
}

data class LocationRadius(
    var center: LatLng,
    var radius: Double,
)

data class DateRange(
    val start: Date,
    val end: Date,
) {
    override fun toString(): String {
        return if (start.isSameDay(end)) {
            start.toPrettyString()
        } else {
            "${start.toPrettyString()} - ${end.toPrettyString()}"
        }
    }
}