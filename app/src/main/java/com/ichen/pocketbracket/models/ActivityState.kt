package com.ichen.pocketbracket.models

enum class ActivityState {
    CREATED,
    ACTIVE,
    COMPLETED,
    READY,
    INVALID,
    CALLED,
    QUEUED,;

    override fun toString(): String {
        return when(this) {
            CREATED -> "Upcoming"
            ACTIVE -> "In Progress"
            COMPLETED -> "Finished"
            else -> throw Error("Invalid State")
        }
    }
}