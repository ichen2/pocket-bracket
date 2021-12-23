package com.ichen.pocketbracket.tournaments

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ichen.pocketbracket.GetUserEventsQuery
import com.ichen.pocketbracket.models.*
import com.ichen.pocketbracket.utils.Field
import com.ichen.pocketbracket.utils.SITE_ENDPOINT
import com.ichen.pocketbracket.utils.Status
import com.ichen.pocketbracket.utils.convertBigDecimalToDate
import kotlinx.coroutines.launch

const val EVENTS_PER_PAGE = 10

class MyTournamentsViewModel : ViewModel() {
    private val repository = MyTournamentsRepository()
    var hasMoreEvents = true
    private var page = 1
    val tournaments: MutableState<Field<List<Tournament>>> = mutableStateOf(
        Field(
            listOf(),
            Status.NOT_STARTED
        )
    )

    fun getEvents(context: Context) {
        page = 1
        tournaments.value = Field(listOf(), Status.LOADING)
        viewModelScope.launch {
            repository.getUserEvents(page, EVENTS_PER_PAGE, context) { response ->
                val parsedResponse = parseGetUserEventsResponse(response)
                tournaments.value = Field(
                    parsedResponse ?: listOf(),
                    if (parsedResponse == null) Status.ERROR else Status.SUCCESS
                )
                hasMoreEvents = parsedResponse?.size ?: 0 > 0
            }
        }
    }

    fun getMoreEvents(context: Context) {
        if(hasMoreEvents) {
            page++
            tournaments.value = tournaments.value.withStatus(Status.LOADING)
            viewModelScope.launch {
                repository.getUserEvents(page, EVENTS_PER_PAGE, context) { response ->
                    val parsedResponse = parseGetUserEventsResponse(response)
                    tournaments.value = Field(
                        tournaments.value.data + (parsedResponse ?: listOf()),
                        if (parsedResponse == null) Status.ERROR else Status.SUCCESS
                    )
                    hasMoreEvents = parsedResponse?.size ?: 0 > 0
                }
            }
        }
    }

    private fun parseGetUserEventsResponse(response: com.apollographql.apollo.api.Response<GetUserEventsQuery.Data>?): List<Tournament>? {
        val nodes = response?.data?.currentUser?.events?.nodes ?: return null
        return nodes.filter { event ->
            event?.tournament?.id != null
            event?.tournament?.name != null
            event?.slug != null
        }.map { event ->
            val tournament = event!!.tournament
            Tournament(
                id = tournament!!.id!!.toInt(),
                name = tournament.name!!,
                url = SITE_ENDPOINT + event.slug!!,
                startAt = convertBigDecimalToDate(tournament.startAt),
                endAt = convertBigDecimalToDate(tournament.endAt),
                isOnline = tournament.isOnline,
                isRegistrationOpen = tournament.isRegistrationOpen,
                numAttendees = tournament.numAttendees,
                state = when (tournament.state) {
                    1 -> ActivityState.CREATED
                    2 -> ActivityState.ACTIVE
                    3 -> ActivityState.COMPLETED
                    else -> null
                },
                imageUrl = tournament.images?.getOrNull(0)?.url,
                events = mutableListOf(
                    Event(
                        id = event.id!!,
                        name = event.name!!,
                        url = SITE_ENDPOINT + event.slug,
                        numEntrants = event.numEntrants,
                        startAt = convertBigDecimalToDate(tournament.startAt),
                        videogame = if (event.videogame?.id != null && videogamesMap.containsKey(
                                event.videogame.id.toInt()
                            )
                        ) videogamesMap[event.videogame.id.toInt()] else null
                    )
                )
            )
        }
    }
    fun cleanup() {
        repository.jobs.forEach { pair ->
            pair.value?.cancel()
        }
    }
}