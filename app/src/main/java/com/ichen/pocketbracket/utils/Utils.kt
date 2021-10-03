package com.ichen.pocketbracket.utils

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import java.text.SimpleDateFormat
import java.util.*

fun Date.toPrettyString(): String {
    return SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(this)
}

fun Date.isSameDay(other: Date): Boolean {
    return this.year == other.year && this.month == other.month && this.day == other.day
}

fun combineDates(date1: Date, date2: Date): String {
    return if (date1.isSameDay(date2)) {
        date1.toPrettyString()
    } else {
        "${date1.toPrettyString()} - ${date2.toPrettyString()}"
    }
}

typealias SetComposableFunction = (((@Composable BoxScope.() -> Unit)?) -> Unit)