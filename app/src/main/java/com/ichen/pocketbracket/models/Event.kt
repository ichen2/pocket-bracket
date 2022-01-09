package com.ichen.pocketbracket.models

import android.os.Parcel
import android.os.Parcelable
import com.ichen.pocketbracket.GetParticipantsQuery
import com.ichen.pocketbracket.GetTournamentsQuery
import com.ichen.pocketbracket.utils.SITE_ENDPOINT
import com.ichen.pocketbracket.utils.convertBigDecimalToDate
import java.util.*

data class Event(
    val id: String,
    val name: String,
    val url: String,
    val numEntrants: Int? = null,
    val startAt: Date? = null,
    val videogame: Videogame? = null,
) : Parcelable {
    constructor(event: GetTournamentsQuery.Event) : this(
        id = event.id!!,
        name = event.name!!,
        url = SITE_ENDPOINT + event.slug!!,
        numEntrants = event.numEntrants,
        startAt = convertBigDecimalToDate(event.startAt),
        videogame = if (event.videogame?.id != null && videogamesMap.containsKey(
                event.videogame.id.toInt()
            )
        ) videogamesMap[event.videogame.id.toInt()] else null
    )
    constructor(event: GetParticipantsQuery.Event) : this(
        id = event.id!!,
        name = event.name!!,
        url = SITE_ENDPOINT + event.slug!!,
    )
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        Date(parcel.readLong()),
        videogamesMap.getOrDefault(parcel.readInt(), videogamesMap[1])
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(url)
        parcel.writeValue(numEntrants)
        parcel.writeLong(startAt?.time ?: -1)
        parcel.writeInt(videogame?.id ?: -1)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }
}

val testEvent = Event(
    id = "0",
    name = "Test event",
    url = "",
    numEntrants = 1,
    startAt = Date(0),
    videogame = videogamesList[0]
)