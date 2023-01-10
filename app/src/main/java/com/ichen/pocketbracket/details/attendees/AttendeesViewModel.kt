package com.ichen.pocketbracket.details.attendees

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.ApolloResponse
import com.ichen.pocketbracket.GetParticipantsQuery
import com.ichen.pocketbracket.details.tournament
import com.ichen.pocketbracket.models.Attendee
import com.ichen.pocketbracket.models.Event
import com.ichen.pocketbracket.utils.Field
import com.ichen.pocketbracket.utils.Status
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AttendeesViewModel : ViewModel() {
    private val repository = AttendeesRepository()
    private var page = 0
    private var hasMoreAttendees = true
    private var currentJob: Job? = null
    val attendees: MutableState<Field<List<Attendee>>> = mutableStateOf(
        Field(
            emptyList(),
            Status.NOT_STARTED
        )
    )

    fun getAttendees(context: Context) {
        // TODO: All of this is kinda sus
        //currentJob?.cancel()
        if(hasMoreAttendees) {
            page++
            attendees.value = Field(attendees.value.data, Status.LOADING)
            currentJob = viewModelScope.launch {
                repository.getAttendees(tournament!!.id.toString(), page, context) { response ->
                    val parsedResponse = parseGetParticipantsResponse(response)
                    attendees.value = Field(
                        (attendees.value.data + (parsedResponse ?: emptyList())).distinctBy { attendee -> attendee.id },
                        if (parsedResponse == null) Status.ERROR else Status.SUCCESS
                    )
                    hasMoreAttendees = (parsedResponse?.size ?: 0) > 0
                    if(attendees.value.status != Status.ERROR) getAttendees(context)
                }
            }
        } else {
            attendees.value = Field(attendees.value.data, Status.SUCCESS)
        }
    }

    private fun parseGetParticipantsResponse(response: ApolloResponse<GetParticipantsQuery.Data>?): List<Attendee>? {
        val nodes = response?.data?.tournament?.participants?.nodes ?: return null
        return nodes.filter { node ->
            node?.id != null
            node?.gamerTag != null
        }.map { node ->
            val profileImageUrl: String? = node?.images?.filter { image ->
                image?.type == "profile"
            }?.getOrNull(0)?.url ?: node?.user?.images?.filter { image ->
                image?.type == "profile"
            }?.getOrNull(0)?.url
            Attendee(
                id = node!!.id!!,
                prefix = node.prefix,
                tag = node.gamerTag!!,
                imageUrl = profileImageUrl,
                // TODO: once attendee/profile page is implemented we can use these again
//                events = (
//                    (node.events ?: emptyList()).filter { event ->
//                        event?.id != null &&
//                        event.name != null &&
//                        event.slug != null
//                    }.map { event ->
//                        Event(event!!)
//                    }.toMutableList()
//                ),
            )
        }
    }

    fun cleanup() {
        currentJob?.cancel()
    }
}