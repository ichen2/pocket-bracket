package com.ichen.pocketbracket.tournaments

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.ApolloResponse
import com.ichen.pocketbracket.GetUserTournamentsQuery
import com.ichen.pocketbracket.models.*
import com.ichen.pocketbracket.utils.*
import kotlinx.coroutines.launch

const val EVENTS_PER_PAGE = 10

open class MyTournamentsViewModel : LAVM() {
    private val repository = MyTournamentsRepository()
    private var hasMoreEvents = true
    private var page = 1
    open val tournaments: MutableState<Field<List<Tournament>>> = mutableStateOf(
        Field(emptyList(), Status.NOT_STARTED)
    )

    override fun onCreated(context: Context) {
        getEvents(context = context)
    }

    open fun getEvents(context: Context) {
        if (tournaments.value.status == Status.LOADING || !hasMoreEvents) return
        tournaments.value = tournaments.value.withStatus(Status.LOADING)
        viewModelScope.launch {
            val response = repository.getUserEvents(page, EVENTS_PER_PAGE, context)?.let {
                parseGetUserTournamentsResponse(it)
            }
            val newTournaments = response ?: emptyList()
            hasMoreEvents = newTournaments.size == EVENTS_PER_PAGE
            if (hasMoreEvents) { page++ }
            tournaments.value = Field(
                data = tournaments.value.data + newTournaments,
                status = if (response == null) Status.ERROR else Status.SUCCESS,
            )
        }
    }

    private fun parseGetUserTournamentsResponse(response: ApolloResponse<GetUserTournamentsQuery.Data>?): List<Tournament> {
        val nodes = response?.data?.currentUser?.tournaments?.nodes
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
            }.sortedByDescending { tournament ->
                tournament.startAt?.time
            }
        }
    }
}

class TestMyTournamentsViewModel : MyTournamentsViewModel() {
    override val tournaments: MutableState<Field<List<Tournament>>> = mutableStateOf(
        Field(
            listOf(testTournament1, testTournament2),
            Status.SUCCESS
        )
    )
    override fun getEvents(context: Context) {}
}