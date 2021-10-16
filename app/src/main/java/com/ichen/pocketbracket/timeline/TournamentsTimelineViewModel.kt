package com.ichen.pocketbracket.timeline

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.api.Response
import com.ichen.pocketbracket.GetTournamentsQuery
import com.ichen.pocketbracket.models.*
import com.ichen.pocketbracket.utils.Field
import com.ichen.pocketbracket.utils.Status
import com.ichen.pocketbracket.utils.convertBigDecimalToDate
import kotlinx.coroutines.launch

class TournamentsTimelineViewModel : ViewModel() {

    private val repository = TournamentsTimelineRepository()
    private var hasMoreTournaments = true
    private var filter = TournamentFilter()
    val tournaments: MutableState<Field<List<Tournament>>> = mutableStateOf(
        Field(
            listOf(),
            Status.NOT_STARTED
        )
    )

    fun getTournaments(filter: TournamentFilter = TournamentFilter(), context: Context) {
        this.filter = filter
        tournaments.value = Field(listOf(), Status.LOADING)
        viewModelScope.launch {
            repository.getTournaments(filter, context) { response ->
                val parsedResponse = parseResponse(response)
                tournaments.value = Field(
                    parsedResponse ?: listOf(),
                    if (parsedResponse == null) Status.ERROR else Status.SUCCESS
                )
                hasMoreTournaments = parsedResponse?.size ?: 0 > 0
            }
        }
    }

    fun getMoreTournaments(context: Context) {
        if(hasMoreTournaments) {
            this.filter.page++
            tournaments.value = tournaments.value.withStatus(Status.LOADING)
            viewModelScope.launch {
                repository.getTournaments(filter, context) { response ->
                    val parsedResponse = parseResponse(response)
                    tournaments.value = Field(
                        tournaments.value.data + (parsedResponse ?: listOf()),
                        if (parsedResponse == null) Status.ERROR else Status.SUCCESS
                    )
                    hasMoreTournaments = parsedResponse?.size ?: 0 > 0
                }
            }
        }
    }

    fun parseResponse(response: Response<GetTournamentsQuery.Data>?): List<Tournament>? {
        val nodes = response?.data?.tournaments?.nodes
        if (nodes == null || nodes.isEmpty()) {
            return listOf()
        } else {
            // SUS
            return nodes.filter { node ->
                node?.id != null
                node?.name != null
            }.map { node ->
                Tournament(
                    id = node!!.id!!.toInt(),
                    name = node.name!!,
                    startAt = convertBigDecimalToDate(node.startAt),
                    endAt = convertBigDecimalToDate(node.endAt),
                    isOnline = node.isOnline,
                    isRegistrationOpen = node.isRegistrationOpen,
                    numAttendees = node.numAttendees,
                    state = when (node.state) {
                        0 -> ActivityState.CREATED
                        1 -> ActivityState.ACTIVE
                        2 -> ActivityState.COMPLETED
                        else -> null
                    },
                    imageUrl = node.images?.getOrNull(0)?.url,
                    events = node.events?.filter { event ->
                        event?.id != null
                        event?.name != null
                    }?.map { event ->
                        Event(
                            id = event!!.id!!,
                            name = event.name!!,
                            numEntrants = event.numEntrants,
                            startAt = convertBigDecimalToDate(node.startAt),
                            videogame = if (event.videogame?.id != null && videogamesMap.containsKey(
                                    event.videogame.id.toInt()
                                )
                            ) videogamesMap[event.videogame.id.toInt()] else null
                        )
                    }
                )
            }
        }
    }

    fun cleanup() {
        repository.jobs.forEach { pair ->
            pair.value?.cancel()
        }
    }
}