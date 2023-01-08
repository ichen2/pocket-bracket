package com.ichen.pocketbracket.tournaments

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.ApolloResponse
import com.ichen.pocketbracket.GetTournamentsQuery
import com.ichen.pocketbracket.GetUserTournamentsQuery
import com.ichen.pocketbracket.models.*
import com.ichen.pocketbracket.utils.Field
import com.ichen.pocketbracket.utils.SITE_ENDPOINT
import com.ichen.pocketbracket.utils.Status
import com.ichen.pocketbracket.utils.convertBigDecimalToDate
import kotlinx.coroutines.launch

const val EVENTS_PER_PAGE = 10

open class MyTournamentsViewModel : ViewModel() {
    private val repository = MyTournamentsRepository()
    var hasMoreEvents = true
    private var page = 1
    open val tournaments: MutableState<Field<List<Tournament>>> = mutableStateOf(
        Field(
            listOf(),
            Status.NOT_STARTED
        )
    )

    open fun getEvents(context: Context) {
        page = 1
        tournaments.value = Field(listOf(), Status.LOADING)
        viewModelScope.launch {
            repository.getUserEvents(page, EVENTS_PER_PAGE, context) { response ->
                val parsedResponse = parseGetUserTournamentsResponse(response)
                tournaments.value = Field(
                    parsedResponse ?: listOf(),
                    if (parsedResponse == null) Status.ERROR else Status.SUCCESS
                )
                hasMoreEvents = parsedResponse?.size ?: 0 > 0
            }
        }
    }

    open fun getMoreEvents(context: Context) {
        if(hasMoreEvents) {
            page++
            tournaments.value = tournaments.value.withStatus(Status.LOADING)
            viewModelScope.launch {
                repository.getUserEvents(page, EVENTS_PER_PAGE, context) { response ->
                    val parsedResponse = parseGetUserTournamentsResponse(response)
                    tournaments.value = Field(
                        tournaments.value.data + (parsedResponse ?: listOf()),
                        if (parsedResponse == null) Status.ERROR else Status.SUCCESS
                    )
                    hasMoreEvents = parsedResponse?.size ?: 0 > 0
                }
            }
        }
    }

    private fun parseGetUserTournamentsResponse(response: ApolloResponse<GetUserTournamentsQuery.Data>?): List<Tournament>? {
        val nodes = response?.data?.currentUser?.tournaments?.nodes
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
                    events = (node.events?.filter { event ->
                        event?.id != null &&
                                event.name != null &&
                                event.slug != null
                    } as List<GetUserTournamentsQuery.Event>?)?.map { event ->
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
            }.sortedByDescending { tournament ->
                tournament.startAt?.time
            }
        }
    }

    open fun cleanup() {
        repository.currentJob?.cancel()
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
    override fun getMoreEvents(context: Context) {}
    override fun cleanup() {}
}