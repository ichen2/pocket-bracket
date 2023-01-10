package com.ichen.pocketbracket.timeline

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.ApolloResponse
import com.ichen.pocketbracket.GetTournamentsQuery
import com.ichen.pocketbracket.models.*
import com.ichen.pocketbracket.utils.*
import kotlinx.coroutines.launch

open class TournamentsTimelineViewModel : LAVM() {

    private val repository = TournamentsTimelineRepository()
    private var hasMoreTournaments = true
    var filter by mutableStateOf(TournamentFilter())
    open var tournaments: Field<List<Tournament>> by mutableStateOf(
        Field(emptyList(), Status.NOT_STARTED)
    )

    override fun onCreated(context: Context) {
        getTournaments(context = context)
    }

    open fun getTournaments(newFilter: TournamentFilter? = null, context: Context) {
        if (tournaments.status == Status.LOADING || !hasMoreTournaments) return
        val resetSearch = newFilter != null
        if (resetSearch) { filter = newFilter!!.copy(page = 1) }
        tournaments = if (resetSearch) Field(emptyList(), Status.LOADING) else tournaments.withStatus(Status.LOADING)
        viewModelScope.launch {
            val response = repository.getTournaments(filter, context)?.let {
                val parsedResponse = parseGetTournamentsResponse(it)
                sortTournaments(parsedResponse, filter)
            }
            val newTournaments = response ?: emptyList()
            hasMoreTournaments = newTournaments.size == filter.perPage
            if (hasMoreTournaments) { filter = filter.copy(page = filter.page + 1)}
            tournaments = Field(
                data = if (!resetSearch) tournaments.data + newTournaments else newTournaments,
                status = if (response == null) Status.ERROR else Status.SUCCESS,
            )
        }
    }

    private fun sortTournaments(tournaments: List<Tournament>, tournamentFilter: TournamentFilter): List<Tournament> {
        return tournaments.sortedWith { t1: Tournament, t2: Tournament ->
            if(t1.getRating(tournamentFilter) > t2.getRating(tournamentFilter)) 1 else -1
        }
    }

    private fun parseGetTournamentsResponse(response: ApolloResponse<GetTournamentsQuery.Data>?): List<Tournament> {
        val nodes = response?.data?.tournaments?.nodes
        if (nodes == null || nodes.isEmpty()) {
            return emptyList()
        } else {
            return nodes.filter { node ->
                node?.id != null
                node?.name != null
                node?.slug != null
            }.map { node ->
                Tournament(
                    id = node!!.id!!.toInt(),
                    name = node.name!!,
                    url ="${SITE_ENDPOINT}/${node.slug!!}",
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
                    events = (
                        // TODO: graphql response types would be nice so we could make this fun generic
                        (node.events ?: emptyList()).filter { event ->
                            event?.id != null &&
                            event.name != null &&
                            event.slug != null
                        }.map { event ->
                            Event(event!!)
                        }.toMutableList()
                    ),
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
}

//class TestTournamentsTimelineViewModel : TournamentsTimelineViewModel() {
//    override var tournaments: Field<List<Tournament>> by mutableStateOf(
//        Field(listOf(testTournament1, testTournament2), Status.SUCCESS)
//    )
//    override fun getTournaments(newFilter: TournamentFilter?, context: Context) {}
//}