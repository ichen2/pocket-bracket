package com.ichen.pocketbracket.models

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.ichen.pocketbracket.utils.CONTACT_EMAIL
import com.ichen.pocketbracket.utils.SECONDS_IN_DAY
import com.ichen.pocketbracket.utils.addDays
import java.util.*

data class Tournament(
    val id: Int,
    val addrState: String? = null,
    val city: String? = null,
    val countryCode: String? = null,
    val location: LatLng? = null,
    val primaryContact: String? = null,
    val slug: String? = null,
    val venueAddress: String? = null,
    val venueName: String? = null,
    val name: String,
    val url: String,
    val startAt: Date? = null,
    val endAt: Date? = null,
    val isOnline: Boolean? = null, // this is sus (should maybe be hasOnlineEvents?)
    val isRegistrationOpen: Boolean? = null,
    val numAttendees: Int? = null,
    val state: ActivityState? = null,
    val primaryImageUrl: String? = null,
    val secondaryImageUrl: String? = null,
    val events: MutableList<Event>? = null,
) : Parcelable {
    // TODO: write unit tests!!
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(LatLng::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        Date(parcel.readLong()),
        Date(parcel.readLong()),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        ActivityState.valueOf(parcel.readString() ?: "INVALID"),
        parcel.readString(),
        parcel.readString(),
        mutableListOf()
    ) {
        val listOfEvents = mutableListOf<Event>()
        parcel.readList(listOfEvents, Event::class.java.classLoader)
        events?.addAll(listOfEvents)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(addrState)
        parcel.writeString(city)
        parcel.writeString(countryCode)
        parcel.writeParcelable(location, flags)
        parcel.writeString(primaryContact)
        parcel.writeString(slug)
        parcel.writeString(venueAddress)
        parcel.writeString(venueName)
        parcel.writeString(name)
        parcel.writeString(url)
        parcel.writeLong(startAt?.time ?: -1)
        parcel.writeLong(endAt?.time ?: -1)
        parcel.writeValue(isOnline)
        parcel.writeValue(isRegistrationOpen)
        parcel.writeValue(numAttendees)
        parcel.writeString(state?.name)
        parcel.writeString(primaryImageUrl)
        parcel.writeString(secondaryImageUrl)
        parcel.writeList(events)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Tournament> {
        override fun createFromParcel(parcel: Parcel): Tournament {
            return Tournament(parcel)
        }

        override fun newArray(size: Int): Array<Tournament?> {
            return arrayOfNulls(size)
        }
    }
}

fun Tournament.getRating(filter: TournamentFilter): Float {
    var rating = 0f
    if (numAttendees != null) rating += numAttendees / 500
    if (startAt != null && filter.dates?.start?.time != null) {
        val dateRangeInDays = (filter.dates.end.time - filter.dates.start.time) / SECONDS_IN_DAY
        rating += (dateRangeInDays - ((startAt.time - filter.dates.start.time) / (SECONDS_IN_DAY * 1000))) / dateRangeInDays
    }
    return rating
}

val testTournament1: Tournament = Tournament(
    id = 0,
    addrState = "CA",
    city = "Los Angeles",
    countryCode = "US",
    location = LatLng(34.0522342, -118.2436849),
    primaryContact = CONTACT_EMAIL,
    slug = "test-tournament",
    venueAddress = "Los Angeles, CA, USA",
    venueName = "Test Tournament Venue",
    startAt = Date(),
    url = "",
    endAt = Date().addDays(2),
    isOnline = true,
    isRegistrationOpen = true,
    name = "Some Smash Tourney",
    numAttendees = 1,
    state = ActivityState.ACTIVE,
    primaryImageUrl = "https://i.ibb.co/SdTYfYc/placeholder-tournament-splash.png",
    events = mutableListOf(testEvent1),
)

val testTournament2: Tournament = Tournament(
    id = 1,
    addrState = "CA",
    city = "Los Angeles",
    countryCode = "US",
    location = LatLng(34.0522342, -118.2436849),
    primaryContact = CONTACT_EMAIL,
    slug = "test-tournament",
    venueAddress = "Los Angeles, CA, USA",
    venueName = "Test Tournament Venue",
    startAt = Date().addDays(2),
    url = "",
    endAt = Date().addDays(4),
    isOnline = true,
    isRegistrationOpen = true,
    name = "Some Smash Tourney",
    numAttendees = 1,
    state = ActivityState.ACTIVE,
    primaryImageUrl = "https://i.ibb.co/YhsdHRq/placeholder-tournament-splash.png",
    events = mutableListOf(testEvent2),
)