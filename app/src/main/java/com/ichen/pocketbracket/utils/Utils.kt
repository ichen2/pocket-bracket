package com.ichen.pocketbracket.utils

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

const val METERS_IN_MILE = 1609.34

data class LocationRadius(
    var center: LatLng,
    var radius: Float,
)

fun LocationRadius.getCenterAsString() : String {
    return "(${center.latitude.roundToInt()}, ${center.longitude.roundToInt()})"
}

fun Date.toPrettyString(): String {
    return SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(this)
}

fun Date.isSameDay(other: Date): Boolean {
    return this.year == other.year && this.month == other.month && this.day == other.day
}

fun androidx.compose.ui.graphics.Color.toArgbInt() : Int {
    return this.toArgb()
}

fun combineDates(date1: Date, date2: Date): String {
    return if (date1.isSameDay(date2)) {
        date1.toPrettyString()
    } else {
        "${date1.toPrettyString()} - ${date2.toPrettyString()}"
    }
}

inline fun<reified T: Enum<T>> getNextEnumValue(curr: T): T {
    val nextIndex = curr.ordinal + 1
    return enumValues<T>()[if(nextIndex >= enumValues<T>().size) 0 else nextIndex]
}

typealias SetComposableFunction = (((@Composable BoxScope.() -> Unit)?) -> Unit)