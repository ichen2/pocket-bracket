package com.ichen.pocketbracket.utils

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import com.ichen.pocketbracket.browser.BrowserActivity
import com.ichen.pocketbracket.details.TournamentDetailsActivity
import com.ichen.pocketbracket.models.LocationRadius
import com.ichen.pocketbracket.models.Tournament
import com.ichen.pocketbracket.timeline.components.RADIUS_MAX
import com.ichen.pocketbracket.timeline.components.RADIUS_MIN
import okhttp3.Interceptor
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

const val METERS_IN_MILE = 1609.34
const val SECONDS_IN_DAY = 86400

fun openTournamentDetailsScreen(context: Context, tournament: Tournament) {
    val intent = Intent(context, TournamentDetailsActivity::class.java)
    intent.putExtra("tournament", tournament)
    ContextCompat.startActivity(context, intent, null)
}

fun openBrowser(context: Context, url: String) {
    val intent = Intent(context, BrowserActivity::class.java)
    intent.putExtra("url", url)
    ContextCompat.startActivity(context, intent, null)
}

fun LocationRadius.getCenterAsString() : String {
    return "(${center.latitude.roundToInt()}\u00B0, ${center.longitude.roundToInt()}\u00B0)"
}

fun getScaledRadius(sliderValue: Float) : Double {
    val scale = Math.log(RADIUS_MAX)-Math.log(RADIUS_MIN)
    return Math.exp(Math.log(RADIUS_MIN) + scale * (sliderValue))
}

fun Date.toPrettyString(): String {
    val sdf = SimpleDateFormat("MM/dd/yy", Locale.getDefault())
    sdf.timeZone = TimeZone.getDefault()
    return sdf.format(this)
}

fun Date.isSameDay(other: Date): Boolean {
    val date = this
    val c1 = Calendar.getInstance().apply { time = date }
    val c2 = Calendar.getInstance().apply { time = other }
    return c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR) && c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
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

fun BigDecimal.toDate(): Date {
    return Date((this).toLong() * 1000)
}

fun Date.addDays(numDays: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.DATE, numDays)
    return calendar.time
}

fun Date.addHours(numHours: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.HOUR, numHours)
    return calendar.time
}

class AuthorizationInterceptor(val context: Context, val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        return chain.proceed(request)
    }
}

fun mergeAddress(city: String?, state: String?, country: String?) : String? {
    val address: String = (if(city != null) "$city, " else "") + (if(state != null) "$state, " else "") + if(country != null) "$country" else ""
    return if(address.isEmpty()) null else address
}

const val Z_INDEX_TOP = 1000f
const val Z_INDEX_MID = 500f
const val Z_INDEX_BOT = 0f

const val API_ENDPOINT = "https://api.start.gg/gql/alpha"
const val SITE_ENDPOINT = "https://start.gg"
const val SHARED_PREFERENCES_KEY = "POCKET_BRACKET"
const val API_KEY_STORAGE_KEY = "API_KEY"
const val CURRENT_TAB_STORAGE_KEY = "CURRENT_TAB"