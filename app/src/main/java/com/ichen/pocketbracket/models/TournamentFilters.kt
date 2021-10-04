package com.ichen.pocketbracket.models

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