package com.ichen.pocketbracket.models

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Event(
    val id: String,
    val name: String,
    val url: String,
    val numEntrants: Int?,
    val startAt: Date?,
    val videogame: Videogame?
) : Parcelable {
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