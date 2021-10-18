package com.ichen.pocketbracket.utils

import android.content.Context
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import com.apollographql.apollo.api.Response
import com.google.android.gms.maps.model.LatLng
import com.ichen.pocketbracket.BuildConfig
import com.ichen.pocketbracket.GetTournamentsQuery
import com.ichen.pocketbracket.models.*
import com.ichen.pocketbracket.timeline.components.RADIUS_MAX
import com.ichen.pocketbracket.timeline.components.RADIUS_MIN
import okhttp3.Interceptor
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

const val METERS_IN_MILE = 1609.34

fun LocationRadius.getCenterAsString() : String {
    return "(${center.latitude.roundToInt()}\u00B0, ${center.longitude.roundToInt()}\u00B0)"
}

fun getScaledRadius(sliderValue: Float) : Double {
    val scale = Math.log(RADIUS_MAX)-Math.log(RADIUS_MIN)
    return Math.exp(Math.log(RADIUS_MIN) + scale * (sliderValue))
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

enum class Status {
    NOT_STARTED,
    LOADING,
    SUCCESS,
    ERROR,
}

data class Field<T>(
    var data: T,
    var status: Status
) {
    fun withData(data: T) = Field(data, status)

    fun withStatus(status: Status) = Field(data, status)
}

fun convertBigDecimalToDate(date: Any?) : Date? {
    return if(date != null && date is BigDecimal) Date((date).toLong() * 1000) else null
}

class AuthorizationInterceptor(val context: Context, val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        return chain.proceed(request)
    }
}

const val API_ENDPOINT = "https://api.smash.gg/gql/alpha"
const val SITE_ENDPOINT = "https://smash.gg/"
const val SHARED_PREFERENCES_KEY = "POCKET_BRACKET"
const val API_KEY_STORAGE_KEY = "API_KEY"
const val CURRENT_TAB_STORAGE_KEY = "CURRENT_TAB"