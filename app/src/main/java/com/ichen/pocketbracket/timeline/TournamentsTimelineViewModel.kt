package com.ichen.pocketbracket.timeline

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.api.Response
import com.ichen.pocketbracket.GetTournamentsQuery
import com.ichen.pocketbracket.GetVideogamesQuery
import com.ichen.pocketbracket.models.*
import com.ichen.pocketbracket.utils.Field
import com.ichen.pocketbracket.utils.SITE_ENDPOINT
import com.ichen.pocketbracket.utils.Status
import com.ichen.pocketbracket.utils.convertBigDecimalToDate
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class TournamentsTimelineViewModel : ViewModel() {

    private val repository = TournamentsTimelineRepository()
    private var hasMoreTournaments = true
    private var tournamentFilter = TournamentFilter()
    private var currentJob: Job? = null
    open val tournaments: MutableState<Field<List<Tournament>>> = mutableStateOf(
        Field(
            listOf(),
            Status.NOT_STARTED
        )
    )

    // TODO: why are these two seperate functions?? they do the same thing
    open fun getTournaments(tournamentFilter: TournamentFilter = TournamentFilter(), context: Context) {
        currentJob?.cancel()
        this.tournamentFilter = tournamentFilter
        tournaments.value = Field(listOf(), Status.LOADING)
        currentJob = viewModelScope.launch {
            repository.getTournaments(tournamentFilter, context) { response ->
                val parsedResponse = parseGetTournamentsResponse(response)
                //val sortedTournaments = if(parsedResponse == null) null else sortTournaments(parsedResponse, tournamentFilter)
                tournaments.value = Field(
                    parsedResponse ?: listOf(),
                    if (parsedResponse == null) Status.ERROR else Status.SUCCESS
                )
                hasMoreTournaments = parsedResponse?.size ?: 0 > 0 // this could probably be simplified to hasMoreTournaments = parsedResponse.size == tournamentFilter.perPage ??
            }
        }
    }

    open fun getMoreTournaments(context: Context) {
        currentJob?.cancel()
        if(hasMoreTournaments) {
            this.tournamentFilter.page++
            tournaments.value = tournaments.value.withStatus(Status.LOADING)
            currentJob = viewModelScope.launch {
                repository.getTournaments(tournamentFilter, context) { response ->
                    val parsedResponse = parseGetTournamentsResponse(response)
                    //val sortedTournaments = if(parsedResponse == null) null else sortTournaments(parsedResponse, tournamentFilter)
                    tournaments.value = Field(
                        tournaments.value.data + (parsedResponse ?: listOf()),
                        if (parsedResponse == null) Status.ERROR else Status.SUCCESS
                    )
                    hasMoreTournaments = parsedResponse?.size ?: 0 > 0
                }
            }
        }
    }

    private fun sortTournaments(tournaments: List<Tournament>, tournamentFilter: TournamentFilter): List<Tournament> {
        return tournaments.sortedWith { t1: Tournament, t2: Tournament ->
            if(t1.getRating(tournamentFilter) > t2.getRating(tournamentFilter)) 1 else -1
        }
    }

    private fun parseGetTournamentsResponse(response: Response<GetTournamentsQuery.Data>?): List<Tournament>? {
        val nodes = response?.data?.tournaments?.nodes
        if (nodes == null || nodes.isEmpty()) {
            return listOf()
        } else {
            return nodes.filter { node ->
                node?.id != null
                node?.name != null
                node?.slug != null
            }.map { node ->
                Tournament(
                    id = node!!.id!!.toInt(),
                    name = node.name!!,
                    url = SITE_ENDPOINT + node.slug!!,
                    startAt = convertBigDecimalToDate(node.startAt),
                    endAt = convertBigDecimalToDate(node.endAt),
                    isOnline = node.isOnline,
                    isRegistrationOpen = node.isRegistrationOpen,
                    numAttendees = node.numAttendees,
                    state = when (node.state) {
                        1 -> ActivityState.CREATED
                        2 -> ActivityState.ACTIVE
                        3 -> ActivityState.COMPLETED
                        else -> null
                    },
                    primaryImageUrl = node.images?.getOrNull(0)?.url,
                    secondaryImageUrl = node.images?.getOrNull(1)?.url,
                    events = (node.events?.filter { event ->
                        event?.id != null &&
                        event.name != null &&
                        event.slug != null
                    } as List<GetTournamentsQuery.Event>?)?.map { event ->
                        Event(event)
                    }?.toMutableList(),
                    addrState = node.addrState,
                    countryCode = node.countryCode,
                    // TODO: location
                    primaryContact = node.primaryContact,
                    venueAddress = node.venueAddress,
                    venueName = node.venueName,
                    slug = node.slug
                )
            }
        }
    }

    open fun cleanup() {
        currentJob?.cancel()
    }
}

class TestTournamentsTimelineViewModel : TournamentsTimelineViewModel() {
    override val tournaments: MutableState<Field<List<Tournament>>> = mutableStateOf(
        Field(
            listOf(testTournament1, testTournament2),
            Status.SUCCESS
        )
    )
    override fun getTournaments(tournamentFilter: TournamentFilter, context: Context) {}
    override fun getMoreTournaments(context: Context) {}
    override fun cleanup() {}
}