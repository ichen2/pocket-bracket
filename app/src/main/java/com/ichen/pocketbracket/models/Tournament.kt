package com.ichen.pocketbracket.models

import android.os.Parcel
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import com.ichen.pocketbracket.utils.SECONDS_IN_DAY
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
    val imageUrl: String? = null,
    val events: List<Event>? = null,
) : Parcelable {
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
        null, // TODO: startAt
        null, // TODO: endAt
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        null, // TODO: state
        parcel.readString(),
        listOf() // TODO: events
    ) {
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
        parcel.writeValue(isOnline)
        parcel.writeValue(isRegistrationOpen)
        parcel.writeValue(numAttendees)
        parcel.writeString(imageUrl)
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
    addrState = "CA",
    city = "Los Angeles",
    countryCode = "US",
    location = LatLng(34.0522342, -118.2436849),
    primaryContact = "pocketbracket@gmail.com",
    slug = "test-tournament",
    venueAddress = "Los Angeles, CA, USA",
    venueName = "Test Tournament Venue",
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